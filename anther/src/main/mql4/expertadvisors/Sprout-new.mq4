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
input double allowableRisk = 65.0;
input double allowableReward = 85.0;
input double minimumRisk = 35.0;
input double minimumReward = 65.0;
input double profitMultiplier = 2.0;
input int tradesLimit = 3;
input bool allowBreakEvenStop = true;
input int breakEvenStopLevel = 55.0;
input double stopLevelOffset = 0.0;

// Global Variables
int activeTradeId = -1;
int slippage = 10;
datetime globalTime;
bool canTrade = false;
int tradesToday = 0;
double todaysProfit = 0.0;
double openingBalance = AccountBalance();

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
      if (IsNewDay()) {
         canTrade = false;
         tradesToday = 0;
         todaysProfit = 0.0;
         openingBalance = AccountBalance();
      }

      if (IsTradeWindowOpen()) {
         canTrade = true;
      }

      if (IsEndOfDay() && HasOpenTrades()) {
         CloseDay();
      }

      if (openingBalance != AccountBalance()) {
         todaysProfit = AccountBalance() - openingBalance;
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

bool IsEndOfDay() {
   return TimeHour(globalTime) > 22 && TimeHour(globalTime) <= 23;
}

/*
   Checks if the trading window for this strategy is open
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

bool ShouldTrade(int signal) {
   /*if (signal == BUY_SIGNAL) {
      return todaysProfit <= 0.0;
   } else if (signal == SELL_SIGNAL) {
      return todaysProfit <= 0.0;
   }

   return false;*/
   return true;
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

   if (sigLow < refLow && Bid > sigHigh && HasTradeConfirmation(BUY_SIGNAL) && ShouldTrade(BUY_SIGNAL)) {
      canTrade = false;
      OpenStandardTrade(GetFullSize(), BUY_SIGNAL);
      return BUY_SIGNAL;
   } else if (sigHigh > refHigh && Ask < sigLow && HasTradeConfirmation(SELL_SIGNAL) && ShouldTrade(SELL_SIGNAL)) {
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
      } else if (window > allowableRisk) {
         return CalculateLimit(price, allowableRisk, shouldAdd);
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
      //int val = OrderSend(_Symbol, OP_BUY, lotSize, Ask, slippage, CalculateActualLimit(Bid, window, false, false), CalculateActualLimit(Bid, window, true, true), "Sprout Buy", 911);
      //int val = OrderSend(_Symbol, OP_BUY, lotSize, Ask, slippage, Low[1], CalculateActualLimit(Bid, window, true, true), "Sprout Buy", 911);
      int val = OrderSend(_Symbol, OP_BUY, lotSize, Ask, slippage, TEMP(true), CalculateActualLimit(Bid, window, true, true), "Sprout Buy", 911);
      if (val == -1) {
         Print(StringFormat("An error occurred while trying to open a Buy at %d:%.2d", TimeHour(globalTime), TimeMinute(globalTime)));
         Print(GetLastError());
      } else {
         activeTradeId = val;
         tradesToday +=1;
      }
   } else if (signal == SELL_SIGNAL) {
      //int val = OrderSend(_Symbol, OP_SELL, lotSize, Bid, slippage, CalculateActualLimit(Ask, window, true, false), CalculateActualLimit(Ask, window, false, true), "Sprout Sell", 911);
      //int val = OrderSend(_Symbol, OP_SELL, lotSize, Bid, slippage, High[1], CalculateActualLimit(Ask, window, false, true), "Sprout Sell", 911);
      int val = OrderSend(_Symbol, OP_SELL, lotSize, Bid, slippage, TEMP(false), CalculateActualLimit(Ask, window, false, true), "Sprout Sell", 911);
      if (val == -1) {
         Print(StringFormat("An error occurred while trying to open a Sell at %d:%.2d", TimeHour(globalTime), TimeMinute(globalTime)));
         Print(GetLastError());
      } else {
         activeTradeId = val;
         tradesToday +=1;
      }
   }
}

/*
TODO
*/
double TEMP(bool buy) {

   double diff = MathAbs(High[1] - Low[1]) * 0.05;
   double point = 0.0;

   if (buy) {
      point = Low[1];
   } else {
      point = High[1];
   }

   return buy ? (point + diff) : (point - buy);
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
   Determines if a trade is currently active

   @returns true if a trade is active
*/
bool HasActiveTrade() {

   if (activeTradeId != -1) {
      return true;
   } else if (HasOpenTrades()) {
      for (int pos = 0; pos < OrdersTotal(); pos++) {
         if (OrderSelect(pos, SELECT_BY_POS)) {
            if (StringFind(OrderComment(), "Sprout") != -1) {
               activeTradeId = OrderTicket();
            }
         }
      }
   }

   return activeTradeId != -1;
}

/*
   Handle break even stops
*/
void SetBreakEvenStop() {
   if (HasActiveTrade() && allowBreakEvenStop && breakEvenStopLevel > 0) {
      double currentPrice = -1.0;
      double difference = 0.0;

      if (GetOrderType() == OP_BUY) {
         currentPrice = Bid;
         difference = stopLevelOffset * -1.0;
      } else if (GetOrderType() == OP_SELL) {
         currentPrice = Ask;
         difference = stopLevelOffset;
      }

      if (currentPrice != -1 && OrderSelect(activeTradeId, SELECT_BY_TICKET)) {
         if (MathAbs(OrderStopLoss() - OrderOpenPrice()) > 0 && MathAbs(currentPrice - OrderOpenPrice()) > breakEvenStopLevel) {
            Print("Trade has moved far enough into profit, setting Stop Loss to breakeven.");

            if (OrderModify(OrderTicket(), OrderOpenPrice(), OrderOpenPrice() + difference, OrderTakeProfit(), 0))  {
               Print("Break-even Stop Loss was successfully set.");
            }
         }
      }
   }
}

/*
   Control Statistics. Sprout Version 1.1
   As of January 1st, 2025 (exclusive)
   Parameters:
      online = true;
      lotSize = 0.25;
      allowableRisk = 65.0;
      allowableReward = 85.0;
      minimumRisk = 35.0;
      minimumReward = 65.0;
      profitMultiplier = 2.0;
      tradesLimit = 3;
      allowBreakEvenStop = false;
      breakEvenStopLevel = 55;
      stopLevelOffset = 0.0;

      // Global Variables
      activeTradeId = -1;
      slippage = 10;
      globalTime;
      canTrade = false;
      tradesToday = 0;
      todaysProfit = 0.0;
      openingBalance = AccountBalance();
*/
/*
   +------------------------------------------------------------------+
   | Trades                                                        440 |
   | Net Profit                                             $19,367.15 |
   | Profitability                                                1.35 |
   | Win %                                                      52.50% |
   | Max Drawdown                                    $3,285.30 (8.95%) |
   | Relative Drawdown                               $3,285.30 (8.95%) |
   +------------------------------------------------------------------+
*/