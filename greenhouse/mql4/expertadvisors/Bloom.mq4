/*
   +------------------------------------------------------------------+
   |                                                        Bloom.mq4 |
   |                                                   Stephen Prizio |
   |                                         https://www.bluebell.com |
   +------------------------------------------------------------------+
*/
#property copyright "Stephen Prizio"
#property link      "https://www.bluebell.com"
#property version   "1.0"
#property strict

input bool online = true;
input double lotSize = 0.4;
input double longTakeProfit = 48.0;
input double longStopLoss = 23.0;
input double shortTakeProfit = 58.0;
input double shortStopSLoss = 22.0;
input bool allowBreakEvenStop = true;
input int breakEvenStopLevel = 30;
input bool logTradeContext = false;
input double reversalWindow = 85.0;

// Global Variables
int slippage = 10;
double varianceOffset = 2.25;
datetime globalTime;
double signalPrice = -1.0;
int activeTradeId = -1;

/*
   +------------------------------------------------------------------+
   | Expert initialization function                                   |
   +------------------------------------------------------------------+
*/
int OnInit() {
   if (!online) {
      Print("Bloom has been turned off");
   } else {
      Print("Bloom is now online");
   }

   return(INIT_SUCCEEDED);
}

/*
   +------------------------------------------------------------------+
   | Expert deinitialization function                                 |
   +------------------------------------------------------------------+
*/
void OnDeinit(const int reason) {}

/*
   +------------------------------------------------------------------+
   | Expert tick function                                             |
   +------------------------------------------------------------------+
*/
void OnTick() {

   if (online) {
      TrackTime();
      if (HasActiveTrade()) {
         SetBreakEvenStop();
      }
   }
}

/*
   +------------------------------------------------------------------+
   | Expert bar function                                              |
   +------------------------------------------------------------------+
*/
void OnBar() {

   if (online) {
      LogTradeContext();
      if (IsTradeWindowOpen()) {
         Print("Trade Window opened. Bloom is now active.");
         OpenBloomTrade();
      } else {
         ProtectSelf();
         CheckTrades();
         ClearTradesForDay();
      }
   }
}

/*
   +------------------------------------------------------------------+
   | General Functions                                                |
   +------------------------------------------------------------------+
*/

/*
   Logs info about the current bar and trade context
*/
void LogTradeContext() {
   if (logTradeContext) {
      Print(StringFormat("Trade Window: %s, Signal Price: %.2f, Active Trade Id: %.d", IsTradeWindowOpen() ? "Open" : "Closed", signalPrice, activeTradeId));
      Print(StringFormat("Current Bar: %d:%.2d", TimeHour(globalTime), TimeMinute(globalTime)));
   }
}

/*
   Keeps track of the current time for each tick. When a new bar is detected,
   call OnBar();
*/
void TrackTime() {
   datetime currentTime = iTime(_Symbol, _Period, 0);
   if (globalTime != currentTime) {
      globalTime = currentTime;
      OnBar();
   }
}

/*
   Checks if the trading window for this strategy is open.

   @returns true if the window is open
*/
bool IsTradeWindowOpen() {
   return TimeHour(globalTime) == 11 && TimeMinute(globalTime) < 5;
}

/*
   Determines if a trade is currently active

   @returns true if a trade is active
*/
bool HasActiveTrade() {

   if (activeTradeId != -1) {
      return true;
   } else if (OrdersTotal() > 0 && OrderSelect(0, SELECT_BY_POS, MODE_TRADES)) {
      if (OrderMagicNumber() == 910) {
         activeTradeId = OrderTicket();
      }
   }

   return activeTradeId != -1;
}

/*
   Obtains the signal price
*/
void GetSignalPrice() {

   Print("Fetching signal price.");
   for (int i = 0; i < 300; i++) {
      if (TimeHour(Time[i]) == 7 && TimeMinute(Time[i]) == 0) {
         signalPrice = Close[i];
         Print(StringFormat("Signal price set to %.2f", signalPrice));
         return;
      }
   }

   Print("A fatal error occurred while fetching the signal price. Please review the terminal");
   Print(GetLastError());
}

/*
   Opens a new stop order trade
*/
void OpenBloomTrade() {
   if (!HasActiveTrade()) {
      while (IsTradeContextBusy()) {
         Print("Trade context is busy. Waiting...");
         Sleep(50);
      }

      GetSignalPrice();
      double localBuyStopLoss = signalPrice - longStopLoss;
      double localBuyTakeProfit = signalPrice + longTakeProfit;
      double localSellStopLoss = signalPrice + shortStopSLoss;
      double localSellTakeProfit = signalPrice - shortTakeProfit;

      if (Open[0] < signalPrice) {
         Print("Opening Buy Stop Order");
         activeTradeId = OrderSend(_Symbol, OP_BUYSTOP, lotSize, signalPrice + varianceOffset, slippage, localBuyStopLoss, localBuyTakeProfit, "Bloom Buy Stop", 910);
      } else if (Open[0] > signalPrice) {
         Print("Opening Sell Stop Order");
         activeTradeId = OrderSend(_Symbol, OP_SELLSTOP, lotSize, signalPrice - varianceOffset, slippage, localSellStopLoss, localSellTakeProfit, "Bloom Sell Stop", 910);
      } else {
         Print("Opening Buy Stop Order");
         activeTradeId = OrderSend(_Symbol, OP_BUYSTOP, lotSize, signalPrice + varianceOffset, slippage, localBuyStopLoss, localBuyTakeProfit, "Bloom Buy Stop", 910);
      }
   }
}

/*
   Deletes the current pending order on command.

   @returns true if the order was successfully deleted
*/
bool DeleteTrade() {
   if (HasActiveTrade()) {
      if (OrderDelete(activeTradeId)) {
         Print(StringFormat("Order #%d was successfully deleted.", activeTradeId));
         activeTradeId = -1;
         signalPrice = -1.0;

         return true;
      } else {
         Alert(StringFormat("Order #%d could not be deleted. Please review your terminal.", activeTradeId));
         Print(GetLastError());

         return false;
    }
   }

   return false;
}

/*
   In the even that a stop order is leftover at the end of the day, delete it
*/
void ClearTradesForDay() {
   if (HasActiveTrade() && TimeDay(Time[0]) != TimeDay(Time[1])) {
      Print(StringFormat("Stale stop order detected. Deleting order #%d", activeTradeId));
      DeleteTrade();
   }
}

/*
   Looks for large differences in pending orders and market highs/lows. If the market is moving to far, remove the order
   just to be safe
*/
void ProtectSelf() {
   if (HasActiveTrade()) {
      if (GetOrderType() == OP_SELLSTOP) {
         double high = High[iHighest(_Symbol, _Period, MODE_HIGH, 100, 0)];
         if (OrderSelect(activeTradeId, SELECT_BY_TICKET)) {
            if (MathAbs(high - OrderOpenPrice()) > reversalWindow && DeleteTrade()) {
               Print("Flipping the switch.");
               GetSignalPrice();
               double localBuyStopLoss = signalPrice - (longStopLoss * 1.5);
               double localBuyTakeProfit = signalPrice + (longTakeProfit * 1.5);

               activeTradeId = OrderSend(_Symbol, OP_BUYLIMIT, (lotSize * 0.75), signalPrice - varianceOffset, slippage, localBuyStopLoss, localBuyTakeProfit, "Bloom Buy Stop", 91);
            }
         }
      } else if (GetOrderType() == OP_BUYSTOP) {
         double low = Low[iLowest(_Symbol, _Period, MODE_LOW, 100, 0)];
         if (OrderSelect(activeTradeId, SELECT_BY_TICKET)) {
            if (MathAbs(OrderOpenPrice() - low) > reversalWindow && DeleteTrade()) {
               Print("Flipping the switch.");
               GetSignalPrice();
               double localSellStopLoss = signalPrice + (shortStopSLoss * 1.5);
               double localSellTakeProfit = signalPrice - (shortTakeProfit * 1.5);

               activeTradeId = OrderSend(_Symbol, OP_SELLLIMIT, (lotSize * 0.75), signalPrice + varianceOffset, slippage, localSellStopLoss, localSellTakeProfit, "Bloom Sell Stop", 91);
            }
         }
      }
   }
}

/*
    Returns the order type

    @return order type enum
*/
int GetOrderType() {
   if (OrderSelect(activeTradeId, SELECT_BY_TICKET)) {
      return OrderType();
   } else {
      return -1;
   }
}

/*
   Checks if the currently active trade has been closed, if so update the state of the strategy
   so that it can be ready for another trade
*/
void CheckTrades() {
   if (OrderSelect(activeTradeId, SELECT_BY_TICKET)) {
      if (OrderCloseTime() > 0) {
         Print("Active trade has closed. Resetting context.");
         activeTradeId = -1;
         signalPrice = -1.0;
      } else if (TimeHour(globalTime) == 23) {
         Print("End of Trading Window reached. Closing all trades.");
         DeleteTrade();
      }
   }
}

/*
   If the order moves by the breakEvenStopLevel in profit, set the stop loss to breakeven
*/
void SetBreakEvenStop() {
   if (HasActiveTrade() && allowBreakEvenStop && breakEvenStopLevel > 0) {
      double currentPrice = -1.0;

      if (GetOrderType() == OP_BUY) {
         currentPrice = Bid;
      } else if (GetOrderType() == OP_SELL) {
         currentPrice = Ask;
      }

      if (currentPrice != -1 && OrderSelect(activeTradeId, SELECT_BY_TICKET)) {
         if (MathAbs(OrderStopLoss() - OrderOpenPrice()) > 0 && MathAbs(currentPrice - OrderOpenPrice()) > breakEvenStopLevel) {
            Print("Trade has moved far enough into profit, setting Stop Loss to breakeven.");

            if (OrderModify(OrderTicket(), OrderOpenPrice(), OrderOpenPrice(), OrderTakeProfit(), 0))  {
               Print("Break-even Stop Loss was successfully set.");
            }
         }
      }
   }
}

/*
   Control Statistics. Bloom Version 1.0
   As of July 17th, 2024 (exclusive)
   Parameters:
      online = true;
      lotSize = 0.4;
      longTakeProfit = 48.0;
      longStopLoss = 23.0;
      shortTakeProfit = 58.0;
      shortStopSLoss = 22.0;
      allowBreakEvenStop = true;
      breakEvenStopLevel = 30;
      logTradeContext = false;
      reversalWindow = 85.0;

      // GLOBALS
      slippage = 10;
      varianceOffset = 2.25;
      globalTime;
      signalPrice = -1.0;
      activeTradeId = -1;
*/
/*
   +------------------------------------------------------------------+
   | Trades                                                        111 |
   | Net Profit                                             $10,297.50 |
   | Profitability                                                2.32 |
   | Win %                                                      63.96% |
   | Max Drawdown                                    $1,102.80 (2.70%) |
   | Relative Drawdown                                 $929.52 (2.94%) |
   +------------------------------------------------------------------+
*/