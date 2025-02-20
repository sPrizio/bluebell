package com.bluebell.platform.constants;

import java.time.LocalDate;
import java.time.Year;

/**
 * Constants defined for the bluebell platform
 *
 * @author Stephen Prizio
 * @version 0.0.9
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
     * The current risk-free rate based on the 10-yr Government of Canada bonds
     */
    public static final double RISK_FREE_RATE_CANADA = 3.26;
}
