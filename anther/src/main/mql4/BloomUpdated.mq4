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
input double buyTakeProfit = 150.0;
input double buyStopLoss = 24.0;
input double sellTakeProfit = 100.0;
input double sellStopSLoss = 22.0;
input int slippage = 10;
input int trailingBuyStop = 50;
input int trailingSellStop = 35;

// GLOBALS
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

   //handleTrailingStops();
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

   checkTrade();

   // check for start of trading
   if (TimeHour(globalTime) == 7 && TimeMinute(globalTime) == 5) {
      signalPrice = Close[1];
   }

   // set 8:00 stop order
   if (TimeHour(globalTime) == 15 && TimeMinute(globalTime) == 0) {
      isReadyToTrade = true;
      OpenBloomTrade();
   }

   // set 12:00 stop order
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
         Sleep(50);
      }

      double localBuyStopLoss = signalPrice - buyStopLoss;
      double localBuyTakeProfit = signalPrice + buyTakeProfit;
      double localSellStopLoss = signalPrice + sellStopSLoss;
      double localSellTakeProfit = signalPrice - sellTakeProfit;

      if (Open[0] < signalPrice) {
         activeTradeId = OrderSend(_Symbol, OP_BUYSTOP, lotSize, signalPrice + 2.25, slippage, localBuyStopLoss, localBuyTakeProfit, "Bloom Buy Stop", 91);
      } else if (Open[0] > signalPrice) {
         activeTradeId = OrderSend(_Symbol, OP_SELLSTOP, lotSize, signalPrice - 2.25, slippage, localSellStopLoss, localSellTakeProfit, "Bloom Sell Stop", 91);
      }

      if (activeTradeId != -1) {
         isReadyToTrade = false;
      }
   }
}

/*
   Checks if the currently active trade has been closed, if so update the state of the strategy
   so that it can be ready for another trade
*/
void checkTrade() {
   if (OrderSelect(activeTradeId, SELECT_BY_TICKET)) {
      if (OrderCloseTime() > 0) {
         activeTradeId = -1;
         isReadyToTrade = (OrderProfit() < 0);
      } else if (TimeHour(globalTime) == 23) {
         //close all trades
         OrderDelete(activeTradeId);
         activeTradeId = -1;
         isReadyToTrade = false;
      }
   }
}

void handleTrailingStops() {
   if (getOrderType() == OP_BUY) {
      updateTrailingBuyStopLoss();
   } else if (getOrderType() == OP_SELL) {
      updateTrailingSellStopLoss();
   }
}

void updateTrailingSellStopLoss() {
   if (trailingSellStop > 0 && activeTradeId != -1) {
      if (OrderSelect(activeTradeId, SELECT_BY_TICKET)) {
         if (OrderOpenTime() > 0 && OrderCloseTime() == 0) {
            // Trade is open
            double difference = (Ask + OrderOpenPrice());
            if (difference < trailingSellStop) {
               double newPrice = Ask + trailingSellStop;
               if (newPrice < OrderStopLoss()) {
                  bool res = OrderModify(OrderTicket(), OrderOpenPrice(), newPrice, OrderTakeProfit(), 0);
                  if(!res)  {
                     Print("Error in OrderModify. Error code=",GetLastError());
                  } else {
                     Print("Order modified successfully.");
                  }
               }
            }
         }
      }
   }
}

void updateTrailingBuyStopLoss() {
   if (trailingBuyStop > 0 && activeTradeId != -1) {
      if (OrderSelect(activeTradeId, SELECT_BY_TICKET)) {
         if (OrderOpenTime() > 0 && OrderCloseTime() == 0) {
            // Trade is open
            double difference = (Bid - OrderOpenPrice());
            if (difference > trailingBuyStop) {
               double newPrice = Bid - trailingBuyStop;
               if (newPrice > OrderStopLoss()) {
                  bool res = OrderModify(OrderTicket(), OrderOpenPrice(), newPrice, OrderTakeProfit(), 0);
                  if(!res)  {
                     Print("Error in OrderModify. Error code=",GetLastError());
                  } else {
                     Print("Order modified successfully.");
                  }
               }
            }
         }
      }
   }
}

int getOrderType() {
   if (OrderSelect(activeTradeId, SELECT_BY_TICKET)) {
      return OrderType();
   } else {
      return -1;
   }
}