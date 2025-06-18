//+------------------------------------------------------------------+
//|                                                      BloomV2.mq4 |
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
input double lotSize = 0.01;
input double stopLoss = 3.0;
input bool allowMultipleTrades = false;

// Global Variables
int activeTradeId = -1;
int slippage = 10;
datetime globalTime;
bool canTrade = false;

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

   if (_Period == PERIOD_D1) {
      return(INIT_SUCCEEDED);
   }

   Print("Sprout is designed to function only on the 1-Day time frame");
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
   }
}

/*
   +------------------------------------------------------------------+
   | Expert bar function                                              |
   +------------------------------------------------------------------+
*/
void OnBar() {

   if (online) {
      TrailStopLoss();

      // TODO: look into stagnating bars as an exit criteria. Use the Dow Jones as an example

      if (allowMultipleTrades) {
         Trade();
      } else if (!HasOpenTrades()) {
         activeTradeId = -1.0;
         Trade();
      }
   }
}

/*
   +------------------------------------------------------------------+
   | General Functions                                                |
   +------------------------------------------------------------------+
*/

void Trade() {
   double current4 = iMA(_Symbol, _Period, 4, 0, MODE_EMA, PRICE_CLOSE, 1);
   double current8 = iMA(_Symbol, _Period, 8, 0, MODE_EMA, PRICE_CLOSE, 1);
   double current9 = iMA(_Symbol, _Period, 9, 0, MODE_EMA, PRICE_CLOSE, 1);

   double prev4 = iMA(_Symbol, _Period, 4, 0, MODE_EMA, PRICE_CLOSE, 2);
   double prev8 = iMA(_Symbol, _Period, 8, 0, MODE_EMA, PRICE_CLOSE, 2);
   double prev9 = iMA(_Symbol, _Period, 9, 0, MODE_EMA, PRICE_CLOSE, 2);

   if (prev4 < prev8 && prev4 < prev9) {
      if (current4 > current8 && current4 > current9) {
         OpenStandardTrade(GetFullSize(), BUY_SIGNAL);
      }
   } else if (prev4 > prev8 && prev4 > prev9) {
      if (current4 < current8 && current4 < current9) {
         OpenStandardTrade(GetFullSize(), SELL_SIGNAL);
      }
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
   Opens a standard market order, depending the on given signal. The window acts as the stop loss and the window * profit multiplier acts
   as the take profit
*/
void OpenStandardTrade(double window, int signal) {
   double temp = 500.0;
   if (signal == BUY_SIGNAL) {
      int val = OrderSend(_Symbol, OP_BUY, lotSize, Ask, slippage, Low[2], Bid * 10, "Bloom Buy", 912);
      if (val == -1) {
         Print(StringFormat("An error occurred while trying to open a Buy at %d:%.2d", TimeHour(globalTime), TimeMinute(globalTime)));
         Print(GetLastError());
      } else {
         activeTradeId = val;
      }
   } else if (signal == SELL_SIGNAL) {
      int val = OrderSend(_Symbol, OP_SELL, lotSize, Bid, slippage, High[2], Ask * 0.10, "Bloom Sell", 912);
      if (val == -1) {
         Print(StringFormat("An error occurred while trying to open a Sell at %d:%.2d", TimeHour(globalTime), TimeMinute(globalTime)));
         Print(GetLastError());
      } else {
         activeTradeId = val;
      }
   }
}

/*
   Calculates the stop loss based on the previous candle

   @return stop loss price
*/
double CalculateStopLoss(bool buy) {

   double diff = GetFullSize();
   double point = 0.0;

   if (buy) {
      point = Low[1];
   } else {
      point = High[1];
   }

   return buy ? (point + diff) : (point - diff);
}

/*
   Returns the size of the bar from its high to its low in absolute terms (positive-only)

   @return absolute valud of high - low
*/
double GetFullSize() {
   return MathAbs(High[1] - Low[1]);
}

/*
   Updates the stop loss for a trade in profit by the input percentage delta
*/
void TrailStopLoss() {

   for (int i = OrdersTotal() - 1; i >= 0; i--) {
      if (!OrderSelect(i, SELECT_BY_POS, MODE_TRADES)) {
         continue;
      }

      int type = OrderType();
      if ((type != OP_BUY && type != OP_SELL) || OrderSymbol() != Symbol()) {
         continue;
      }

      double entryPrice = OrderOpenPrice();
      double currentPrice = (type == OP_BUY) ? Bid : Ask;
      bool inProfit = (type == OP_BUY && currentPrice > entryPrice) || (type == OP_SELL && currentPrice < entryPrice);

      if (!inProfit) {
         continue;
      }

      double slDistance = currentPrice * (stopLoss / 100.0);
      double newSL;

      if (type == OP_BUY) {
         newSL = NormalizeDouble(currentPrice - slDistance, Digits);
      } else {
         newSL = NormalizeDouble(currentPrice + slDistance, Digits);
      }

      // Only tighten SL
      double currentSL = OrderStopLoss();
      //bool shouldUpdate = (type == OP_BUY && (currentSL == 0 || newSL > currentSL)) || (type == OP_SELL && (currentSL == 0 || newSL < currentSL));
      if (/*shouldUpdate && */MathAbs(OrderStopLoss() - newSL) > Point) {
         bool modified = OrderModify(OrderTicket(), OrderOpenPrice(), newSL, OrderTakeProfit(), 0, clrRed);
         if (!modified) {
            Print("Failed to update SL for ticket #", OrderTicket(), ": ", GetLastError());
         }
      }
    }
}

/*
   Looks for any open trades

   @return true if any trades are open
*/
bool HasOpenTrades() {

   for (int pos = 0; pos < OrdersTotal(); pos++) {
      if (OrderSelect(pos, SELECT_BY_POS)) {
         if (StringFind(OrderComment(), "Bloom") != -1) {
            return true;
        }
      }
   }

   return false;
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