package com.bluebell.platform.enums.time;

import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import lombok.Getter;

import java.time.temporal.ChronoUnit;

/**
 * Representation of an interval of time for a {@link MarketPrice}
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@Getter
public enum MarketPriceTimeInterval implements GenericEnum<MarketPriceTimeInterval> {
    ONE_MINUTE("ONE_MINUTE", "1-minute", ChronoUnit.MINUTES, 1),
    FIVE_MINUTE("FIVE_MINUTE", "5-minute", ChronoUnit.MINUTES, 5),
    TEN_MINUTE("TEN_MINUTE", "10-minute", ChronoUnit.MINUTES, 10),
    FIFTEEN_MINUTE("FIFTEEN_MINUTE", "15-minute", ChronoUnit.MINUTES, 15),
    THIRTY_MINUTE("THIRTY_MINUTE", "30-minute", ChronoUnit.MINUTES, 30),
    ONE_HOUR("ONE_HOUR", "1-hour", ChronoUnit.HOURS, 1),
    FOUR_HOUR("FOUR_HOUR", "4-hour", ChronoUnit.HOURS, 4),
    ONE_DAY("ONE_DAY", "1-day", ChronoUnit.DAYS, 1),
    ONE_WEEK("ONE_WEEK", "1-week", ChronoUnit.WEEKS, 1),
    ONE_MONTH("ONE_MONTH", "1-month", ChronoUnit.MONTHS, 1),
    ONE_YEAR("ONE_YEAR", "1-year", ChronoUnit.YEARS, 1);

    private final String code;

    private final String label;

    private final ChronoUnit unit;

    private final int amount;

    MarketPriceTimeInterval(final String code, final String label, final ChronoUnit unit, final int amount) {
        this.code = code;
        this.label = label;
        this.unit = unit;
        this.amount = amount;
    }


    //  METHODS


    /**
     * Calculates the {@link MarketPriceTimeInterval} for a specific label from firstratedata
     *
     * @param label label
     * @return {@link MarketPriceTimeInterval}
     */
    public static MarketPriceTimeInterval getForFirstRateDataLabel(final String label) {
        return switch (label) {
            case "1day" -> ONE_DAY;
            case "1hour" -> ONE_HOUR;
            case "1min" -> ONE_MINUTE;
            case "5min" -> FIVE_MINUTE;
            case "30min" -> THIRTY_MINUTE;
            default -> throw new IllegalArgumentException("Unknown label: " + label);
        };
    }
}
