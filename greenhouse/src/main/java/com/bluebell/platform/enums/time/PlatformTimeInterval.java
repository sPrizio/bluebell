package com.bluebell.platform.enums.time;

import com.bluebell.platform.models.core.nonentities.market.MarketPrice;
import lombok.Getter;

import java.time.temporal.ChronoUnit;

/**
 * Representation of an interval of time for a {@link MarketPrice}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
public enum PlatformTimeInterval {
    ONE_MINUTE(ChronoUnit.MINUTES, 1),
    FIVE_MINUTE(ChronoUnit.MINUTES, 5),
    TEN_MINUTE(ChronoUnit.MINUTES, 10),
    FIFTEEN_MINUTE(ChronoUnit.MINUTES, 15),
    THIRTY_MINUTE(ChronoUnit.MINUTES, 30),
    ONE_HOUR(ChronoUnit.HOURS, 1),
    ONE_DAY(ChronoUnit.DAYS, 1),
    ONE_WEEK(ChronoUnit.WEEKS, 1),
    ONE_MONTH(ChronoUnit.MONTHS, 1),
    ONE_YEAR(ChronoUnit.YEARS, 1);

    private final ChronoUnit unit;

    private final int amount;

    PlatformTimeInterval(final ChronoUnit unit, final int amount) {
        this.unit = unit;
        this.amount = amount;
    }
}
