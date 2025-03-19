//+------------------------------------------------------------------+
//|                                                        Bloom.mq4 |
//|                                                   Stephen Prizio |
//|                                         https://www.bluebell.com |
//+------------------------------------------------------------------+
#property copyright "Stephen Prizio"
#property link      "https://www.bluebell.com"
#property version   "1.0"
#property strict

input double lotSize = 0.28;
input double buyTakeProfit = 52.71;
input double buyStopLoss = 26.02;
input double sellTakeProfit = 56.02;
input double sellStopSLoss = 22.81;
input int slippage = 10;
input datetime signalBar = D'16:55:00';

// GLOBALS
int globalTicketBuy = -1;
int globalTicketSell = -1;
datetime globalTime;

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

   if (TimeHour(globalTime) == TimeHour(signalBar) && TimeMinute(globalTime) == TimeMinute(signalBar)) {
      OpenBloomTrades();
   }

   if (TimeHour(globalTime) == 23 && TimeMinute(globalTime) == 0) {
      CloseBloomTrade(globalTicketBuy, true);
      CloseBloomTrade(globalTicketSell, false);
   }
}

//+------------------------------------------------------------------+
//| General Functions                                                |
//+------------------------------------------------------------------+

/*
   Opens both a Buy & Sell trade based on the pre-defined risk:reward ratios
*/
void OpenBloomTrades() {

   while (IsTradeContextBusy()) {
      Sleep(50);
   }

   double localBuyStopLoss = Ask - buyStopLoss;
   double localBuyTakeProfit = Ask + buyTakeProfit;
   double localSellStopLoss = Bid + sellStopSLoss;
   double localSellTakeProfit = Bid - sellTakeProfit;

   if (globalTicketBuy == -1) {
      globalTicketBuy = OrderSend(_Symbol, OP_BUY, lotSize, Ask, slippage, localBuyStopLoss, localBuyTakeProfit, "Bloom Buy", 91);
   }

   if (globalTicketSell == -1) {
      globalTicketSell = OrderSend(_Symbol, OP_SELL, lotSize, Bid, slippage, localSellStopLoss, localSellTakeProfit, "Bloom Sell", 91);
   }
}

/**
   Closes the specified trade

   @param ticket trade number/id
   @param isBuy determines which of the 2 global straddles to close
*/
void CloseBloomTrade(int ticket, bool isBuy) {

   while (IsTradeContextBusy()) {
      Sleep(50);
   }

   if (OrderSelect(ticket, SELECT_BY_TICKET)) {
      if (OrderClosePrice() > 0) {
         if (isBuy) {
            globalTicketBuy = -1;
         } else {
            globalTicketSell = -1;
         }
      } else {
         // close the trade
      }
   }
}
