//+------------------------------------------------------------------+
//|                                           MarketDataFetching.mq4 |
//|                                                   Stephen Prizio |
//|                                         https://www.bluebell.com |
//+------------------------------------------------------------------+
#property copyright "Stephen Prizio"
#property link      "https://www.bluebell.com"
#property version   "1.00"
#property strict

input bool online = true;
input string InpDirectoryName = "Data";

datetime globalTime;
bool write = true;

/*
   +------------------------------------------------------------------+
   | Expert initialization function                                   |
   +------------------------------------------------------------------+
*/
int OnInit() {

   if (!online) {
      Print("MarketDataFetching has been disabled");
   } else {
      Print("MarketDataFetching has now been turned on");
   }

   if (_Period == PERIOD_H1) {
      return(INIT_SUCCEEDED);
   }

   Print("MarketDataFetching is designed to function only on the 1-hour time frame");
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
      // market data is updated at the end of each day at 4pm Eastern
      if (TimeHour(globalTime) == 23) {
         GenerateAllFiles();
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
   Formats the period enum into a txt value for file naming
*/
string FormatPeriod(int period) {
   switch (period) {
      case PERIOD_M1:
         return "ONE_MINUTE";
      case PERIOD_M5:
         return "FIVE_MINUTE";
      case PERIOD_M15:
         return "FIFTEEN_MINUTE";
      case PERIOD_M30:
         return "THIRTY_MINUTE";
      case PERIOD_H1:
         return "ONE_HOUR";
      case PERIOD_H4:
         return "FOUR_HOUR";
      case PERIOD_D1:
         return "ONE_DAY";
      case PERIOD_W1:
         return "ONE_WEEK";
      case PERIOD_MN1:
         return "ONE_MONTH";
      default:
         return "NA";
   }
}

/*
   Computes the file name for output
*/
string GetFileName(int period) {

   string year = FormatNumber(TimeYear(TimeCurrent()));
   string month = FormatNumber(TimeMonth(TimeCurrent()));
   string day = FormatNumber(TimeDay(TimeCurrent()));

   string hour = FormatNumber(TimeHour(TimeCurrent()));
   string minute = FormatNumber(TimeMinute(TimeCurrent()));
   string second = FormatNumber(TimeSeconds(TimeCurrent()));

   return StringFormat("MarketData_%s_%s%s%s_%s%s%s_%s.csv", _Symbol, year, month, day, hour, minute, second, FormatPeriod(period));
}

/*
   Generates the data file with market data
*/
void GenerateFile(int period) {

   int lookback;

   double open[];
   double high[];
   double low[];
   double close[];
   long volume[];
   datetime dateTimes[];

   switch (period) {
      case PERIOD_M1:
         lookback = 45000;
         break;
      case PERIOD_M5:
         lookback = 9000;
         break;
      case PERIOD_M15:
         lookback = 3000;
         break;
      case PERIOD_M30:
         lookback = 1500;
         break;
      case PERIOD_H1:
         lookback = 750;
         break;
      case PERIOD_H4:
         lookback = 185;
         break;
      case PERIOD_D1:
         lookback = 32;
         break;
      case PERIOD_W1:
         lookback = 5;
         break;
      case PERIOD_MN1:
         lookback = 2;
         break;
      default:
         lookback = 0;
         break;
   }

   int copied = CopyTime(_Symbol, period, 0, lookback, dateTimes);
   if(copied <= 0) {
      PrintFormat("Failed to copy time values. Error code = %d", GetLastError());
      return;
   }

   ResetLastError();
   copied = CopyOpen(_Symbol, period, 0, lookback, open);
   if (copied <= 0) {
      PrintFormat("Failed to copy open prices. Error code = %d", GetLastError());
      return;
   }

   ResetLastError();
   copied = CopyHigh(_Symbol, period, 0, lookback, high);
   if (copied <= 0) {
      PrintFormat("Failed to copy high prices. Error code = %d", GetLastError());
      return;
   }

   ResetLastError();
   copied = CopyLow(_Symbol, period, 0, lookback, low);
   if (copied <= 0) {
      PrintFormat("Failed to copy low prices. Error code = %d", GetLastError());
      return;
   }

   ResetLastError();
   copied = CopyClose(_Symbol, period, 0, lookback, close);
   if (copied <= 0) {
      PrintFormat("Failed to copy close prices. Error code = %d", GetLastError());
      return;
   }

   copied = CopyTickVolume(_Symbol, period, 0, lookback, volume);
   if (copied <= 0) {
      PrintFormat("Failed to copy volumes. Error code = %d", GetLastError());
      return;
   }

   ResetLastError();

   datetime currentTime = iTime(_Symbol, period, 0);
   string fileName = GetFileName(period);
   int file_handle = FileOpen(InpDirectoryName + "//" + fileName, FILE_READ | FILE_WRITE | FILE_CSV);
   if (file_handle != INVALID_HANDLE) {
      for (int i = 0; i < lookback; i++) {
         FileWrite(file_handle, FormatDateTime(dateTimes[i]), open[i], high[i], low[i], close[i], volume[i]);
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

      result = datePart + ";" + timePart;
   }

   return result;
}

/*
   Generates market price data files for each period
*/
void GenerateAllFiles() {
   GenerateFile(PERIOD_M1);
   GenerateFile(PERIOD_M5);
   GenerateFile(PERIOD_M15);
   GenerateFile(PERIOD_M30);
   GenerateFile(PERIOD_H1);
   GenerateFile(PERIOD_H4);
   GenerateFile(PERIOD_D1);
   GenerateFile(PERIOD_W1);
   GenerateFile(PERIOD_MN1);
}