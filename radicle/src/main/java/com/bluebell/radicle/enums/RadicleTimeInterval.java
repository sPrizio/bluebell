package com.bluebell.radicle.enums;


import com.bluebell.radicle.models.MarketPrice;
import lombok.Getter;

import java.time.temporal.ChronoUnit;

/**
 * Representation of an interval of time for a {@link MarketPrice}
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
public enum RadicleTimeInterval {
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

    RadicleTimeInterval(final ChronoUnit unit, final int amount) {
        this.unit = unit;
        this.amount = amount;
    }
}
