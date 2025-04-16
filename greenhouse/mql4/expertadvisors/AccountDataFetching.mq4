//+------------------------------------------------------------------+
//|                                          AccountDataFetching.mq4 |
//|                                                   Stephen Prizio |
//|                                         https://www.bluebell.com |
//+------------------------------------------------------------------+
#property copyright "Stephen Prizio"
#property link      "https://www.bluebell.com"
#property version   "1.00"
#property strict
#property description "Please be sure to toggle the option 'All-Time History' for the history terminal"

input bool online = true;
input string InpDirectoryName = "Account";

datetime globalTime;
bool write = true;

/*
   +------------------------------------------------------------------+
   | Expert initialization function                                   |
   +------------------------------------------------------------------+
*/
int OnInit() {

   if (!online) {
      Print("AccountDataFetching has been disabled");
   } else {
      Print("AccountDataFetching has now been turned on");
   }

   if (_Period == PERIOD_H1) {
      GenerateFile();
      return(INIT_SUCCEEDED);
   }

   Print("AccountDataFetching is designed to function only on the 1-hour time frame");
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
      // market data is updated at the end of each day at 1am Eastern
      if (TimeHour(globalTime) == 8) {
         GenerateFile();
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
   Formats the given interger for display
*/
string FormatNumber(int intVal) {
   if (intVal < 10) {
      return "0" + IntegerToString(intVal);
   } else {
      return IntegerToString(intVal);
   }
}

/*
   Generates the data file with market data
*/
void GenerateFile() {

   // retrieving info from trade history
   int i, hstTotal = OrdersHistoryTotal();
   string fileName = "AccountTradeHistory.csv";
   int file_handle = FileOpen(InpDirectoryName + "//" + fileName, FILE_READ | FILE_WRITE | FILE_CSV);

   if (file_handle != INVALID_HANDLE) {
      for(i = 0; i < hstTotal; i++) {
         if(OrderSelect(i, SELECT_BY_POS, MODE_HISTORY) == false) {
            Print("Access to history failed with error (", GetLastError(), ")");
            break;
         }

         Print("Order history found");
         FileWrite(
            file_handle,
            OrderTicket(),
            FormatDateTime(OrderOpenTime()),
            OrderType(),
            OrderLots(),
            OrderSymbol(),
            DoubleToString(OrderOpenPrice()),
            OrderStopLoss(),
            OrderTakeProfit(),
            FormatDateTime(OrderCloseTime()),
            DoubleToString(OrderClosePrice()),
            DoubleToString(OrderProfit()),
            OrderComment()
         );
      }

      FileClose(file_handle);
      PrintFormat("Data is written, %s file is closed", fileName);
   } else {
      PrintFormat("Failed to open %s file, Error code = %d", fileName, GetLastError());
   }
}

/*
   Formats a datetime entry into a valid string pattern
*/
string FormatDateTime(datetime dtr) {
   string result = TimeToString(dtr, TIME_DATE|TIME_MINUTES);
   StringReplace(result, " ", ";");

   // Extract the time part after the semicolon
   int pos = StringFind(result, ";");
   if (pos != -1) {
      string datePart = StringSubstr(result, 0, pos);
      string timePart = StringSubstr(result, pos + 1);

      // Remove leading zero from the time part if present
      if (StringSubstr(timePart, 0, 1) == "0") {
         timePart = StringSubstr(timePart, 1);
      }

      result = datePart + " " + timePart;
   }

   return result;
}
