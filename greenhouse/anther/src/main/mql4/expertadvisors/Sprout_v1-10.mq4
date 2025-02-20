//+------------------------------------------------------------------+
//|                                                       Sprout.mq4 |
//|                                                   Stephen Prizio |
//|                                         https://www.bluebell.com |
//+------------------------------------------------------------------+
#property copyright "Stephen Prizio"
#property link      "https://www.bluebell.com"
#property version   "1.10"
#property strict

enum tradeSignal {
   BUY_SIGNAL,
   SELL_SIGNAL,
   DOUBLE_SIGNAL,
   NO_SIGNAL
};

input bool online = true;
input double lotSize = 0.25;
input int tradesLimit = 3;

// Global Variables
int activeTradeId = -1;
int slippage = 10;
datetime globalTime;
bool canTrade = false;
int tradesToday = 0;
double activeAtr = -1.0;

/*
   +------------------------------------------------------------------+
   | Expert initialization function                                   |
   +------------------------------------------------------------------+
*/
int OnInit() {

   if (!online) {
      Print("Sprout has been turned off");
   } else {
      Print("Sprout is now online");
   }

   if (_Period == PERIOD_M30) {
      return(INIT_SUCCEEDED);
   }

   Print("Sprout is designed to function only on the 30-minute time frame");
   return(INIT_AGENT_NOT_SUITABLE);
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
      if (canTrade) {
         LookForTradeSignals();
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
      if (IsNewDay()) {
         canTrade = false;
         tradesToday = 0;
      }

      if (IsTradeWindowOpen()) {
         canTrade = true;
      }

      if (IsEndOfDay() && HasOpenTrades()) {
         CloseDay();
      }

      if (!HasOpenTrades()) {
         activeAtr = -1.0;
      }

      if (activeTradeId != -1.0 && activeAtr != -1.0 && iATR(_Symbol, _Period, 14, 1) < activeAtr) {
         CloseDay();
      }
   }
}

/*
   +------------------------------------------------------------------+
   | General Functions                                                |
   +------------------------------------------------------------------+
*/

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
   Checks for a new day

   @return true if a new day has started
*/
bool IsNewDay() {
   return TimeHour(globalTime) == 1 && TimeMinute(globalTime) == 0;
}

/*
   Checks for end of the day

   @return true if the day has ended
*/
bool IsEndOfDay() {
   return TimeHour(globalTime) > 22 && TimeHour(globalTime) <= 23;
}

/*
   Checks if the trading window for this strategy is open

   @return true if the trading window has opened
*/
bool IsTradeWindowOpen() {

   bool isAfterOpen = TimeHour(globalTime) >= 17;
   bool isBeforeClose = TimeHour(globalTime) < 23;
   bool limitNotReached = tradesToday < tradesLimit;

   return isAfterOpen && isBeforeClose && limitNotReached && !HasOpenTrades();
}

/*
   Looks for any open trades

   @return true if any trades are open
*/
bool HasOpenTrades() {

   for (int pos = 0; pos < OrdersTotal(); pos++) {
      if (OrderSelect(pos, SELECT_BY_POS)) {
         if (StringFind(OrderComment(), "Sprout") != -1) {
            return true;
        }
      }
   }

   return false;
}

/*
   Closes all open orders
*/
void CloseDay() {

   int total = OrdersTotal();
   if (total > 0) {
      for (int i = total - 1; i >= 0; i--) {
         // if the order cannot be selected, throw and log an error.
         if (OrderSelect(i, SELECT_BY_POS) == false) {
            Print("ERROR - Unable to select the order - ", GetLastError());
            break;
         }

         bool result = false;

         // update the exchange rates before closing the orders.
         RefreshRates();

         double bidPrice = MarketInfo(OrderSymbol(), MODE_BID);
         double askPrice = MarketInfo(OrderSymbol(), MODE_ASK);

         if (OrderType() == OP_BUY)  {
            result = OrderClose(OrderTicket(), OrderLots(), bidPrice, slippage);
         } else if (OrderType() == OP_SELL) {
            result = OrderClose(OrderTicket(), OrderLots(), askPrice, slippage);
         }

         // if there was an error, log it.
         if (!result) {
            Print("ERROR - Unable to close the order - ", OrderTicket(), " - Error ", GetLastError());
         }

         activeTradeId = -1;
         activeAtr = -1.0;
      }
   }
}

/*
   A hammer bar has its body in the top half of the overall bar's length. Typically a bullish indication,
   we do not distinguish between a bullish or bearish hammer, simply its overall shape

   @return true if it is a hammer
*/
bool IsHammer() {
   double topArea = High[1] - (GetFullSize()  / 2.0);
   return Open[1] >= topArea && Close[1] >= topArea;
}

/*
   A tombstone bar has its body in the bottom half of the overall bar's length, exactly opposite of a hammer bar.
   Typically a bearishindication, we do not distinguish between a bullish or bearish tombstone, simply its overall shape

   @return true if it is a tombstone
*/
bool IsTombstone() {
   double bottomArea = Low[1] + (GetFullSize() / 2.0);
   return Open[1] <= bottomArea && Close[1] <= bottomArea;
}

/*
   Returns the size of the bar from its high to its low in absolute terms (positive-only)

   @return absolute valud of high - low
*/
double GetFullSize() {
   return MathAbs(High[1] - Low[1]);
}


/*
   Returns the size of the bar from its close to its open in absolute terms (positive-only)

   @return absolute valud of close - open
*/
double GetBodySize() {
   return MathAbs(Close[1] - Open[1]);
}

/*
   Calculates bullish indication. Bullish indication as calculated as the bar having a close higher than its open
   or having the shape of a hammer bar

   @return true if either of the above criteria is satisfied
*/
bool HasBullishIndication() {
   return Close[1] > Open[1] || IsHammer();
}

/*
   Calculates bearish indication. Bearish indication as calculated as the bar having a close lower than its open
   or having the shape of a tombstone bar

   @return true if either of the above criteria is satisfied
*/
bool HasBearishIndication() {
   return Close[1] < Open[1] || IsTombstone();
}

/*
   Used with a trade signal to determine whether we should enter a position. Acts as the confirmation of a trade.
   Basically, when a trade signal is generated, we confirm it by matching the signal with the appropriate bullish/bearish
   indication. If there is a math, we are ready to enter a trade.

   @return true if the trade is confirmed and ready to be opened
*/
bool HasTradeConfirmation(int signal) {
   if (signal == BUY_SIGNAL) {
      return HasBullishIndication();
   } else if (signal == SELL_SIGNAL) {
      return HasBearishIndication();
   }

   return false;
}

/*
   Looks for trade signals on each tick

   @return trade signal
*/
int LookForTradeSignals() {

   double refHigh = High[2];
   double refLow = Low[2];

   double sigHigh = High[1];
   double sigLow = Low[1];

   if (sigLow < refLow && Bid > sigHigh && HasTradeConfirmation(BUY_SIGNAL)) {
      canTrade = false;
      OpenStandardTrade(GetFullSize(), BUY_SIGNAL);

      return BUY_SIGNAL;
   } else if (sigHigh > refHigh && Ask < sigLow && HasTradeConfirmation(SELL_SIGNAL)) {
      canTrade = false;
      OpenStandardTrade(GetFullSize(), SELL_SIGNAL);
      return SELL_SIGNAL;
   } else {
      return NO_SIGNAL;
   }
}

/*
   Opens a standard market order, depending the on given signal. The window acts as the stop loss and the window * profit multiplier acts
   as the take profit
*/
void OpenStandardTrade(double window, int signal) {

   double risk = MathAbs(High[1] - Low[1]) * 0.95;
   if (((risk / Open[1]) * 100.0) > 0.65) {
      return;
   }

   if (signal == BUY_SIGNAL) {
      int val = OrderSend(_Symbol, OP_BUY, lotSize, Ask, slippage, CalculateStopLoss(true), Bid + (Bid * (0.5 / 100.0)), "Sprout Buy", 911);
      if (val == -1) {
         Print(StringFormat("An error occurred while trying to open a Buy at %d:%.2d", TimeHour(globalTime), TimeMinute(globalTime)));
         Print(GetLastError());
      } else {
         activeTradeId = val;
         activeAtr = iATR(_Symbol, _Period, 14, 1);
         tradesToday +=1;
      }
   } else if (signal == SELL_SIGNAL) {
      int val = OrderSend(_Symbol, OP_SELL, lotSize, Bid, slippage, CalculateStopLoss(false), Ask - (Ask * (0.5 / 100.0)), "Sprout Sell", 911);
      if (val == -1) {
         Print(StringFormat("An error occurred while trying to open a Sell at %d:%.2d", TimeHour(globalTime), TimeMinute(globalTime)));
         Print(GetLastError());
      } else {
         activeTradeId = val;
         activeAtr = iATR(_Symbol, _Period, 14, 1);
         tradesToday +=1;
      }
   }
}

/*
   Calculates the stop loss based on the previous candle

   @return stop loss price
*/
double CalculateStopLoss(bool buy) {

   double diff = MathAbs(High[1] - Low[1]) * 0.05;
   double point = 0.0;

   if (buy) {
      point = Low[1];
   } else {
      point = High[1];
   }

   return buy ? (point + diff) : (point - diff);
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
   Control Statistics. Sprout Version 1.0
   As of January 1st, 2025 (exclusive)
   Parameters:
      online = true;
      lotSize = 0.25;
      tradesLimit = 3;

      // Global Variables
      activeTradeId = -1;
      slippage = 10;
      globalTime;
      canTrade = false;
      tradesToday = 0;
      activeAtr = -1.0;
*/
/*
   +------------------------------------------------------------------+
   | Trades                                                        428 |
   | Net Profit                                             $23,981.70 |
   | Profitability                                                1.53 |
   | Win %                                                      51.17% |
   | Max Drawdown                                   $3,950.80 (10.92%) |
   | Relative Drawdown                              $3,950.80 (10.92%) |
   +------------------------------------------------------------------+
*/