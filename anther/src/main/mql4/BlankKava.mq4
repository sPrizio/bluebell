//+------------------------------------------------------------------+
//|                                                    BlankKava.mq4 |
//|                                                   Stephen Prizio |
//|                                         https://www.bluebell.com |
//+------------------------------------------------------------------+
#property copyright "Stephen Prizio"
#property link      "https://www.bluebell.com"
#property version   "1.00"
#property strict

input double lotSize = 0.28;
input double buyTakeProfit = 10.0;
input double buyStopLoss = 10.0;
input double sellTakeProfit = 10.0;
input double sellStopSLoss = 10.0;


//+------------------------------------------------------------------+
//| Expert initialization function                                   |
//+------------------------------------------------------------------+
int OnInit()
  {
//---
   PrintSpread();
//---
   return(INIT_SUCCEEDED);
  }
//+------------------------------------------------------------------+
//| Expert deinitialization function                                 |
//+------------------------------------------------------------------+
void OnDeinit(const int reason)
  {
//---
   
  }
//+------------------------------------------------------------------+
//| Expert tick function                                             |
//+------------------------------------------------------------------+
void OnTick()
  {
//---
   PrintSpread();
  }
//+------------------------------------------------------------------+




//+------------------------------------------------------------------+
//| General Functions                                                |
//+------------------------------------------------------------------+

/*
   Calculates the spread by subtracting the bid from the ask
   
   @param bid - bid price
   @param ask - ask price
   @returns ask - bid
*/
double CalculateSpread(double ask, double bid) {
   
   double spread = ask - bid;
   double pointsSpread = spread / _Point;
   
   // truncates the value to 0 digits after the decimal
   pointsSpread = NormalizeDouble(spread, 2);

   return spread;
}

/*
   Prints the current spread
*/
void PrintSpread() {

   double spread = CalculateSpread(Ask, Bid); 
   string suffix;
   
   if (spread == 1.0) {
      suffix = "point";
   } else {
      suffix = "points";
   }
   
   string message = StringFormat("The current spread for %s is: %s %s", _Symbol, DoubleToStr(spread, 0), suffix);
   Comment(message);
}