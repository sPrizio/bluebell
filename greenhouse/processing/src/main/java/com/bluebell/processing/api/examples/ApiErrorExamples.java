package com.bluebell.processing.api.examples;

import java.lang.String;

public final class ApiErrorExamples {
  public static final String UNAUTHORIZED_REQUEST = "{\"success\":false,\"data\":null,\"message\":\"Unauthorized Request.\",\"internalMessage\":\"\"}";

  public static final String ACCOUNT_NOT_FOUND_EXAMPLE = "{\"success\":false,\"data\":null,\"message\":\"No account was found for account number <bad_account_number>\",\"internalMessage\":\"\"}";

  public static final String BAD_FILTER_EXAMPLE = "{\"success\":false,\"data\":null,\"message\":\"<bad_value> was not a valid filter.\",\"internalMessage\":\"\"}";

  public static final String BAD_WEEKDAY_EXAMPLE = "{\"success\":false,\"data\":null,\"message\":\"<bad_value> was not a valid weekday.\",\"internalMessage\":\"\"}";

  public static final String BAD_DATE_EXAMPLE = "{\"success\":false,\"data\":null,\"message\":\"The date <bad_date> was not of the expected format <expected_format>.\",\"internalMessage\":\"\"}";

  public static final String BAD_START_INTERVAL_EXAMPLE = "{\"success\":false,\"data\":null,\"message\":\"The start date <bad_start_date> was not of the expected format <expected_format>.\",\"internalMessage\":\"\"}";

  public static final String BAD_END_INTERVAL_EXAMPLE = "{\"success\":false,\"data\":null,\"message\":\"The end date <bad_start_date> was not of the expected format <expected_format>.\",\"internalMessage\":\"\"}";

  public static final String BAD_QUERY_EXAMPLE = "{\"success\":false,\"data\":null,\"message\":\"The request object did not contain one of the required keys : <required_keys_list>\",\"internalMessage\":\"\"}";

  public static final String NO_USER_EXAMPLE = "{\"success\":false,\"data\":null,\"message\":\"No user found for username : <username>\",\"internalMessage\":\"\"}";

  public static final String BAD_TRADE_TYPE_EXAMPLE = "{\"success\":false,\"data\":null,\"message\":\"<bad_trade_type> is not a valid trade type\",\"internalMessage\":\"\"}";

  public static final String BAD_TRADE_EXAMPLE = "{\"success\":false,\"data\":null,\"message\":\"No trade was found with trade id: <bad_trade_id>\",\"internalMessage\":\"\"}";

  public static final String NO_TRADES_FOR_TYPE_EXAMPLE = "{\"success\":false,\"data\":null,\"message\":\"No trades were found for type <trade_type>\",\"internalMessage\":\"\"}";

  public static final String NO_TRADES_FOR_INTERVAL_EXAMPLE = "{\"success\":false,\"data\":null,\"message\":\"No trades were found within interval [start, end]\",\"internalMessage\":\"\"}";

  public static final String BAD_FILE_FORMAT_EXAMPLE = "{\"success\":false,\"data\":null,\"message\":\"The given file <bad_file> was not of a valid format\",\"internalMessage\":\"\"}";

  public static final String BAD_TRADE_RECORD_TIME_INTERVAL_EXAMPLE = "{\"success\":false,\"data\":null,\"message\":\"<bad_trade_record_time_interval> was not a valid interval.\",\"internalMessage\":\"\"}";
}
