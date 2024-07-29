//+------------------------------------------------------------------+
//|                                                       Sprout.mq4 |
//|                                                   Stephen Prizio |
//|                                         https://www.bluebell.com |
//+------------------------------------------------------------------+
#property copyright "Stephen Prizio"
#property link      "https://www.bluebell.com"
#property version   "1.00"
#property strict

enum tradeSignal {
   BUY_SIGNAL,
   SELL_SIGNAL,
   DOUBLE_SIGNAL,
   NO_SIGNAL
};

input double lotSize = 0.2;
input double allowableRisk = 60.0;
input double allowableReward = 110.0;
input double minimumRisk = 25.0;
input double minimumReward = 40.0;
input double profitMultiplier = 2.0;
input int tradesLimit = 2;
input bool allowConcurrentTrades = false;

// Global Variables
int slippage = 10;
datetime globalTime;
bool canTrade = false;
int tradesToday = 0;


/*
   +------------------------------------------------------------------+
   | Expert initialization function                                   |
   +------------------------------------------------------------------+
*/
int OnInit() { return(INIT_SUCCEEDED); }

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
   TrackTime();
   if (canTrade) {
      LookForTradeSignals();
   }
}

/*
   +------------------------------------------------------------------+
   | Expert bar function                                              |
   +------------------------------------------------------------------+
*/
void OnBar() {

   if (IsNewDay()) {
      tradesToday = 0;
   }

   if (IsTradeWindowOpen()) {
      canTrade = true;
   }

   if (IsEndOfDay() && HasOpenTrades()) {
      CloseDay();
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
   return TimeHour(globalTime) == 17 && TimeMinute(globalTime) == 0;
}

bool IsEndOfDay() {
   return TimeHour(globalTime) > 22 && TimeHour(globalTime) <= 23;
}

/*
   Checks if the trading window for this strategy is open
*/
bool IsTradeWindowOpen() {

   bool isAfterOpen = TimeHour(globalTime) >= 17;
   bool isBeforeClose = TimeHour(globalTime) <= 23;
   bool limitNotReached = tradesToday < tradesLimit;

   if (allowConcurrentTrades) {
      return isAfterOpen && isBeforeClose && limitNotReached;
   } else {
      return isAfterOpen && isBeforeClose && limitNotReached && !HasOpenTrades();
   }
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
   Specialized function to calculate sl and tp

   @return sl and tp prices
*/
double CalculateActualLimit(double price, double window, bool shouldAdd, bool includeMultiplier) {

   if (!includeMultiplier) {
      if (window < minimumRisk) {
         return CalculateLimit(price, minimumRisk, shouldAdd);
      } else {
         return CalculateLimit(price, window, shouldAdd);
      }
   }

   double profitWindow = window * profitMultiplier;
   if (profitWindow < minimumReward) {
      return CalculateLimit(price, minimumReward, shouldAdd);
   } else if (profitWindow > allowableReward) {
      return CalculateLimit(price, allowableReward, shouldAdd);
   } else {
      return CalculateLimit(price, profitWindow, shouldAdd);
   }
}

/*
   Used to calculate sl and tp

   @return sl or tp price
*/
double CalculateLimit(double price, double increment, bool shouldAdd) {

   if (shouldAdd) {
      return price + increment;
   } else {
      return price - increment;
   }
}

/*
   Opens a standard market order, depending the on given signal. The window acts as the stop loss and the window * profit multiplier acts
   as the take profit
*/
void OpenStandardTrade(double window, int signal) {
   if (signal == BUY_SIGNAL) {
      int val = OrderSend(_Symbol, OP_BUY, lotSize, Ask, slippage, CalculateActualLimit(Bid, window, false, false), CalculateActualLimit(Bid, window, true, true), "Sprout Buy", 91);
      if (val == -1) {
         Print(StringFormat("An error occurred while trying to open a Buy at %d:%.2d", TimeHour(globalTime), TimeMinute(globalTime)));
         Print(GetLastError());
      } else {
         tradesToday +=1;
      }
   } else if (signal == SELL_SIGNAL) {
      int val = OrderSend(_Symbol, OP_SELL, lotSize, Bid, slippage, CalculateActualLimit(Ask, window, true, false), CalculateActualLimit(Ask, window, false, true), "Sprout Sell", 91);
      if (val == -1) {
         Print(StringFormat("An error occurred while trying to open a Sell at %d:%.2d", TimeHour(globalTime), TimeMinute(globalTime)));
         Print(GetLastError());
      } else {
         tradesToday +=1;
      }
   }
}