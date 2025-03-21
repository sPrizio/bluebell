package com.bluebell.platform.enums.time;

import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.models.core.nonentities.market.MarketPrice;
import lombok.Getter;

import java.time.temporal.ChronoUnit;

/**
 * Representation of an interval of time for a {@link MarketPrice}
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
public enum PlatformTimeInterval implements GenericEnum<PlatformTimeInterval> {
    ONE_MINUTE("ONE_MINUTE", "1-minute", ChronoUnit.MINUTES, 1),
    FIVE_MINUTE("FIVE_MINUTE", "5-minute", ChronoUnit.MINUTES, 5),
    TEN_MINUTE("TEN_MINUTE", "10-minute", ChronoUnit.MINUTES, 10),
    FIFTEEN_MINUTE("FIFTEEN_MINUTE", "15-minute", ChronoUnit.MINUTES, 15),
    THIRTY_MINUTE("THIRTY_MINUTE", "30-minute", ChronoUnit.MINUTES, 30),
    ONE_HOUR("ONE_HOUR", "1-hour", ChronoUnit.HOURS, 1),
    ONE_DAY("ONE_DAY", "1-day", ChronoUnit.DAYS, 1),
    ONE_WEEK("ONE_WEEK", "1-week", ChronoUnit.WEEKS, 1),
    ONE_MONTH("ONE_MONTH", "1-month", ChronoUnit.MONTHS, 1),
    ONE_YEAR("ONE_YEAR", "1-year", ChronoUnit.YEARS, 1);

    private final String code;

    private final String label;

    private final ChronoUnit unit;

    private final int amount;

    PlatformTimeInterval(final String code, final String label, final ChronoUnit unit, final int amount) {
        this.code = code;
        this.label = label;
        this.unit = unit;
        this.amount = amount;
    }
}
