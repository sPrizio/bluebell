//+------------------------------------------------------------------+
//|                                                        Bloom.mq4 |
//|                                                   Stephen Prizio |
//|                                         https://www.bluebell.com |
//+------------------------------------------------------------------+
#property copyright "Stephen Prizio"
#property link      "https://www.bluebell.com"
#property version   "1.0"
#property strict

input double lotSize = 0.4;
input double longTakeProfit = 48.0;
input double longStopLoss = 23.0;
input double shortTakeProfit = 58.0;
input double shortStopSLoss = 22.0;
input int slippage = 10;
input bool allowBreakEvenStop = true;
input int breakEvenStopLevel = 30;

// GLOBALS
double varianceOffset = 2.25;
datetime globalTime;
double signalPrice;
bool isReadyToTrade = false;
int activeTradeId = -1;

//+------------------------------------------------------------------+
//| Expert initialization function                                   |
//+------------------------------------------------------------------+
int OnInit() {
   return(INIT_SUCCEEDED);
}
//+------------------------------------------------------------------+
//| Expert deinitialization function                                 |
//+------------------------------------------------------------------+
void OnDeinit(const int reason) {

}
//+------------------------------------------------------------------+
//| Expert tick function                                             |
//+------------------------------------------------------------------+
void OnTick(){

   SetBreakEvenStop();
   datetime currentTime = iTime(_Symbol, _Period, 0);
   if (globalTime != currentTime) {
      globalTime = currentTime;
      OnBar();
   }
}

//+------------------------------------------------------------------+
//| Expert bar function                                              |
//+------------------------------------------------------------------+
void OnBar() {

   CheckTrades();
   ClearTradesForDay();

   // check for start of trading
   if (TimeHour(globalTime) == 7 && TimeMinute(globalTime) == 5) {
      signalPrice = Close[1];
   }

   // set first stop order
   if (TimeHour(globalTime) == 11 && TimeMinute(globalTime) == 0) {
      isReadyToTrade = true;
      OpenBloomTrade();
   }

   // set second (optional) stop order
   if (isReadyToTrade && TimeHour(globalTime) == 19 && TimeMinute(globalTime) == 0) {
      OpenBloomTrade();
   }
}

//+------------------------------------------------------------------+
//| General Functions                                                |
//+------------------------------------------------------------------+

/*
   Opens a new stop order trade
*/
void OpenBloomTrade() {
   if (isReadyToTrade && activeTradeId == -1) {
      while (IsTradeContextBusy()) {
         Print("Trade context is busy. Waiting...");
         Sleep(50);
      }

      double localBuyStopLoss = signalPrice - longStopLoss;
      double localBuyTakeProfit = signalPrice + longTakeProfit;
      double localSellStopLoss = signalPrice + shortStopSLoss;
      double localSellTakeProfit = signalPrice - shortTakeProfit;

      if (Open[0] < signalPrice) {
         activeTradeId = OrderSend(_Symbol, OP_BUYSTOP, lotSize, signalPrice + varianceOffset, slippage, localBuyStopLoss, localBuyTakeProfit, "Bloom Buy Stop", 91);
      } else if (Open[0] > signalPrice) {
         activeTradeId = OrderSend(_Symbol, OP_SELLSTOP, lotSize, signalPrice - varianceOffset, slippage, localSellStopLoss, localSellTakeProfit, "Bloom Sell Stop", 91);
      }

      if (activeTradeId != -1) {
         isReadyToTrade = false;
      }
   }
}

void DeleteTrade() {
   if (OrderDelete(activeTradeId)) {
      Print(StringFormat("Order #%d was successfully deleted.", activeTradeId));
      activeTradeId = -1;
      isReadyToTrade = false;
   } else {
      Alert(StringFormat("Order #%d could not be deleted. Please review your terminal.", activeTradeId));
    }
}

/*
   In the even that a stop order is leftover at the end of the day, delete it
*/
void ClearTradesForDay() {
   if (activeTradeId != -1 && TimeDay(Time[0]) != TimeDay(Time[1])) {
      Print(StringFormat("Stale stop order detected. Deleting order #%d", activeTradeId));
      DeleteTrade();
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
         activeTradeId = -1;
         isReadyToTrade = (OrderProfit() < 0);
      } else if (TimeHour(globalTime) == 23) {
         DeleteTrade();
      }
   }
}

/*
   If the order moves by the breakEvenStopLevel in profit, set the stop loss to breakeven
*/
void SetBreakEvenStop() {
   if (activeTradeId != -1 && allowBreakEvenStop && breakEvenStopLevel > 0) {
      double currentPrice = -1;

      if (GetOrderType() == OP_BUY) {
         currentPrice = Bid;
      } else if (GetOrderType() == OP_SELL) {
         currentPrice = Ask;
      }

      if (currentPrice != -1 && OrderSelect(activeTradeId, SELECT_BY_TICKET)) {
         if (MathAbs(currentPrice - OrderOpenPrice()) > breakEvenStopLevel) {
            Print("Trade has moved far enough into profit, setting Stop Loss to breakeven.");

            if(OrderModify(OrderTicket(), OrderOpenPrice(), OrderOpenPrice(), OrderTakeProfit(), 0))  {
               Print("Break-even Stop Loss was successfully set.");
            } else {
               Print(StringFormat("Error during OrderModify(). Error code = %d", GetLastError()));
            }
         }
      }
   }
}