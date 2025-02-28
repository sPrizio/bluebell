package com.bluebell.platform.constants;

import java.time.LocalDate;
import java.time.Year;

/**
 * Constants defined for the bluebell platform
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
public class CorePlatformConstants {

    /**
     * Generic message used when displaying an exception thrown from a class that should not have been instantiated
     */
    public static final String NO_INSTANTIATION = "%s classes should not be instantiated";

    private CorePlatformConstants() {
        throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
    }

    /**
     * Represents a value that when encountered will basically act as a non-factor when returning a limited collection of entries. This value is akin
     * to asking the collection to not have a size limit, i.e. show me all results
     */
    public static final int MAX_RESULT_SIZE = -1;

    /**
     * Represents the lowest supported date in the system
     */
    public static final LocalDate MIN_DATE = LocalDate.of(1970, 1, 1);

    /**
     * Represents the highest supported date in the system
     */
    public static final LocalDate MAX_DATE = LocalDate.of(2201, 1, 1);

    /**
     * Represents the maximum allowable calendar year value
     */
    public static final int MAX_CALENDAR_YEAR = Year.MAX_VALUE;

    /**
     * The default date format
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * The default short time format
     */
    public static final String SHORT_TIME_FORMAT = "HH:mm";

    /**
     * The default time format
     */
    public static final String TIME_FORMAT = "HH:mm:ss";

    /**
     * The default date & time format
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * The default Eastern Timezone
     */
    public static final String EASTERN_TIMEZONE = "America/Toronto";

    /**
     * MetaTrader4 uses a specific timezone which is the Eastern European Timezone
     */
    public static final String METATRADER4_TIMEZONE = "EET";

    /**
     * Default size for trade records
     */
    public static final int DEFAULT_TRADE_RECORD_COLLECTION_SIZE = 10;

    /**
     * The current risk-free rate based on the 10-yr Government of Canada bonds
     */
    public static final double RISK_FREE_RATE_CANADA = 3.26;

    /**
     * The base back-end java module name
     */
    public static final String BASE_BACKEND_MODULE = "greenhouse";

    /**
     * Validation messages used throughout the system, organized according to the entity packages
     */
    public static class Validation {

        private Validation() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static class Account {

            private Account() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String ACCOUNT_CANNOT_BE_NULL = "account cannot be null";

            public static final String ACCOUNT_NOT_FOUND = "Account not found";
        }

        public static class Security {

            private Security() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String API_TOKEN_CANNOT_BE_NULL = "apiToken cannot be null";

            public static class User {

                private User() {
                    throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
                }

                public static final String USER_CANNOT_BE_NULL = "user cannot be null";

                public static final String USERNAME_CANNOT_BE_NULL = "username cannot be null";

                public static final String EMAIL_CANNOT_BE_NULL = "email cannot be null";

                public static final String USER_DATE_REGISTERED_CANNOT_BE_NULL = "user's registration date cannot be null";
            }
        }

        public static class System {

            private System() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String TIME_INTERVAL_CANNOT_BE_NULL = "time interval cannot be null";

            public static class PhoneNumber {

                private PhoneNumber() {
                    throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
                }

                public static final String PHONE_NUMBER_CANNOT_BE_NULL = "phone number cannot be null";

                public static final String PHONE_TYPE_CANNOT_BE_NULL = "phone type cannot be null";

                public static final String COUNTRY_CODE_CANNOT_BE_NEGATIVE = "country code cannot be a negative number";

                public static final String TELEPHONE_NUMBER_CANNOT_BE_NEGATIVE = "telephone number cannot be a negative number";

            }
        }

        public static class Trade {

            private Trade() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String TRADE_CANNOT_BE_NULL = "trade cannot be null";

            public static final String TRADE_TYPE_CANNOT_BE_NULL = "tradeType cannot be null";

            public static final String TRADE_ID_CANNOT_BE_NULL = "tradeId cannot be null";

            public static final String IMPORT_STREAM_CANNOT_BE_NULL = "import stream cannot be null";
        }

        public static class DataIntegrity {

            private DataIntegrity() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String INTERVAL_CANNOT_BE_NULL = "interval cannot be null";

            public static final String UID_CANNOT_BE_NULL = "uid cannot be null";

            public static final String INVALID_INTERVAL = "%s was not a valid interval";

            public static final String INVALID_FILTER = "%s was not a valid filter";

            public static final String INVALID_WEEKDAY = "%s was not a valid weekday";

            public static final String BAD_LOCALE_ENUM = "One of the given locales was not a valid Currency or Country";
        }

        public static class DateTime {

            private DateTime() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String START_DATE_CANNOT_BE_NULL = "start date cannot be null";

            public static final String END_DATE_CANNOT_BE_NULL = "end date cannot be null";

            public static final String DATE_CANNOT_BE_NULL = "date cannot be null";

            public static final String START_DATE_INVALID_FORMAT = "The start date %s was not of the expected format %s";

            public static final String END_DATE_INVALID_FORMAT = "The end date %s was not of the expected format %s";

            public static final String MUTUALLY_EXCLUSIVE_DATES = "start date was after end date or vice versa";
        }
    }

    /**
     * Regex strings & expressions
     */
    public static class Regex {

        private Regex() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        /**
         * Global Phone number regex
         * (1) = country, (2) = area code, (3) = exchange, (4) = subscriber, (5) = extension where (x) indicates matcher group
         */
        public static final String PHONE_NUMBER_REGEX = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";

        public static class Import {

            private Import() {
                throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
            }

            public static final String MT4_HTML_TABLE_CELL = "<td.*?>(.*?)<\\/td>";

            public static final String MT4_HTML_TABLE_CELL_START = "<td.*?>";

            public static final String MT4_HTML_TABLE_CELL_END = "</td>";

            public static final String MT4_HTML_TABLE_ROW = "<tr.*?>(.*?)<\\/tr>";

            public static final String MT4_HTML_TABLE_ROW_START = "<tr.*?>";

            public static final String MT4_HTML_TABLE_ROW_END = "</tr>";
        }
    }
}
