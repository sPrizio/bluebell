//+------------------------------------------------------------------+
//|                                                      Blossom.mq4 |
//|                                                   Stephen Prizio |
//|                                         https://www.bluebell.com |
//+------------------------------------------------------------------+
#property copyright "Stephen Prizio"
#property link      "https://www.bluebell.com"
#property version   "1.00"
#property strict

input int period = 200;
input double lotSize = 0.28;
input double buyTakeProfit = 52.04;
input double buyStopLoss = 26.02;
input double sellTakeProfit = 45.61;
input double sellStopSLoss = 22.81;
input int slippage = 10;

// GLOBALS
bool isTradeActive = false;
int globalTicket = -1;
datetime globalTime;
int tradeCountLimit = 1;
int tradeCount = 0;

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
   if (TimeHour(globalTime) == 16 && TimeMinute(globalTime) == 30) {
      tradeCount = 0;
   }

   if (TimeHour(globalTime) >= 16 && TimeHour(globalTime) <= 23) {
      double ema = iMA(_Symbol, _Period, period, 0, MODE_EMA, PRICE_CLOSE, 1);

      // check crossover on open of new bar
      if (hasCrossedAbove(ema)) {
         OpenBlossomTrade("buy");
      } else if (hasCrossedBelow(ema)) {
         OpenBlossomTrade("sell");
      }
   }
}

//+------------------------------------------------------------------+
//| General Functions                                                |
//+------------------------------------------------------------------+
void OpenBlossomTrade(string direction) {

   while (IsTradeContextBusy()) {
      Sleep(50);
   }

   double localBuyStopLoss = Ask - buyStopLoss;
   double localBuyTakeProfit = Ask + buyTakeProfit;
   double localSellStopLoss = Bid + sellStopSLoss;
   double localSellTakeProfit = Bid - sellTakeProfit;

   //if (OrdersTotal() < 3) {
   if (tradeCount <= tradeCountLimit) {
      if (direction == "buy") {
         globalTicket = OrderSend(_Symbol, OP_BUY, lotSize, Ask, slippage, localBuyStopLoss, localBuyTakeProfit, "Blossom Buy", 91);
         tradeCount = tradeCount + 1;
      } else if (direction == "sell") {
         globalTicket = OrderSend(_Symbol, OP_SELL, lotSize, Bid, slippage, localSellStopLoss, localSellTakeProfit, "Blossom Sell", 91);
         tradeCount = tradeCount + 1;
      }
   }
}

bool hasCrossedAbove(double ema) {
   return Open[1] < ema && Close[1] > ema;
}

bool hasCrossedBelow(double ema) {
   return Open[1] > ema && Close[1] < ema;
}