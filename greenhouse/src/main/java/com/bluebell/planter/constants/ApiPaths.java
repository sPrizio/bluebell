package com.bluebell.planter.constants;

import static com.bluebell.platform.constants.CorePlatformConstants.NO_INSTANTIATION;

/**
 * Defines all api paths used by the endpoints and testing module
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
public class ApiPaths {

    private ApiPaths() {
        throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
    }

    public static class Account {

        private Account() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static final String BASE = "/account";

        public static final String CURRENCIES = "/currencies";

        public static final String ACCOUNT_TYPES = "/account-types";

        public static final String BROKERS = "/brokers";

        public static final String TRADE_PLATFORMS = "/trade-platforms";

        public static final String GET_DETAILS = "/get-details";

        public static final String CREATE_ACCOUNT = "/create-account";

        public static final String UPDATE_TRADE_DATA = "/update-trade-data";

        public static final String UPDATE_ACCOUNT = "/update-account";

        public static final String DELETE_ACCOUNT = "/delete-account";
    }

    public static class Analysis {

        private Analysis() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static final String BASE = "/analysis";

        public static final String TIME_BUCKETS = "/time-buckets";

        public static final String WEEKDAYS = "/weekdays";

        public static final String WEEKDAYS_TIME_BUCKETS = "/weekdays-time-buckets";

        public static final String TRADE_DURATIONS = "/trade-durations";
    }

    public static class Chart {

        private Chart() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static final String BASE = "/chart";

        public static final String APEX_DATA = "/apex-data";
    }

    public static class Job {

        private Job() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static final String BASE = "/job";

        public static final String GET_BY_ID = "/get-by-id";

        public static final String GET_STATUS_PAGED = "/get-status-paged";

        public static final String GET_TYPE_PAGED = "/get-type-paged";

        public static final String GET_PAGED = "/get-status-type-paged";

        public static final String GET_JOB_TYPES = "/get-job-types";
    }

    public static class MarketPrice {

        private MarketPrice() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static final String BASE = "/market-price";

        public static final String INGEST = "/ingest";

        public static final String TIME_INTERVALS = "/time-intervals";
    }

    public static class MarketNews {

        private MarketNews() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static final String BASE = "/news";

        public static final String GET = "/get";

        public static final String GET_FOR_INTERVAL = "/get-for-interval";

        public static final String FETCH_NEWS = "/fetch-news";
    }

    public static class Portfolio {

        private Portfolio() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static final String BASE = "/portfolio";

        public static final String GET = "/get";

        public static final String CREATE_PORTFOLIO = "/create-portfolio";

        public static final String UPDATE_PORTFOLIO = "/update-portfolio";

        public static final String DELETE_PORTFOLIO = "/delete-portfolio";
    }

    public static class PortfolioRecord {

        private PortfolioRecord() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static final String BASE = "/portfolio-record";

        public static final String GET = "/get";

        public static final String GET_COMPREHENSIVE = "/get-comprehensive";
    }

    public static class Symbol {

        private Symbol() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static final String BASE = "/symbol";

        public static final String GET_TRADED_SYMBOLS = "/get-traded-symbols";
    }

    public static class User {

        private User() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static final String BASE = "/user";

        public static final String GET = "/get";

        public static final String COUNTRY_CODES = "/country-codes";

        public static final String CURRENCIES = "/currencies";

        public static final String COUNTRIES = "/countries";

        public static final String LANGUAGES = "/languages";

        public static final String RECENT_TRANSACTIONS = "/recent-transactions";

        public static final String CREATE = "/create";

        public static final String UPDATE = "/update";
    }

    public static class System {

        private System() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static final String BASE = "/system";

        public static final String HEALTHCHECK = "/healthcheck";

        public static final String CONTACT = "/contact";

        public static final String REPORT = "/report";

        public static final String ACKNOWLEDGE = "/acknowledge";
    }

    public static class Trade {

        private Trade() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static final String BASE = "/trade";

        public static final String GET_FOR_TYPE = "/get-for-type";

        public static final String GET_FOR_INTERVAL = "/get-for-interval";

        public static final String GET_FOR_INTERVAL_PAGED = "/get-for-interval-paged";

        public static final String GET_FOR_TRADE_ID = "/get-for-trade-id";

        public static final String GET_TRADE_INSIGHTS = "/get-trade-insights";

        public static final String IMPORT_TRADES = "/import-trades";

        public static final String CREATE_TRADE = "/create-trade";

        public static final String UPDATE_TRADE = "/update-trade";

        public static final String DELETE_TRADE = "/delete-trade";
    }

    public static class TradeRecord {

        private TradeRecord() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static final String BASE = "/trade-record";

        public static final String GET_FOR_INTERVAL = "/get-for-interval";

        public static final String GET_RECENT = "/get-recent";

        public static final String GET_TRADE_RECORD_CONTROLS = "/get-trade-record-controls";

        public static final String TRADE_LOG = "/trade-log";
    }

    public static class Transaction {

        private Transaction() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static final String BASE = "/transaction";

        public static final String GET_RECENT_FOR_ACCOUNT = "/get-recent-for-account";

        public static final String GET_FOR_ACCOUNT = "/get-for-account";

        public static final String GET_BY_TYPE_FOR_ACCOUNT = "/get-by-type-for-account";

        public static final String GET_BY_STATUS_FOR_ACCOUNT = "/get-by-status-for-account";

        public static final String GET_WITHIN_TIMESPAN_FOR_ACCOUNT = "/get-within-timespan-for-account";

        public static final String GET_BY_NAME_FOR_ACCOUNT = "/get-by-name-for-account";

        public static final String CREATE_TRANSACTION = "/create-transaction";

        public static final String UPDATE_TRANSACTION = "/update-transaction";

        public static final String DELETE_TRANSACTION = "/delete-transaction";
    }
}
