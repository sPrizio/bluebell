package com.bluebell.planter.core.enums.system;

import com.bluebell.planter.core.models.nonentities.records.tradeRecord.TradeRecord;
import org.apache.commons.lang3.StringUtils;

import java.time.temporal.ChronoUnit;

/**
 * Enum representing various time intervals for {@link TradeRecord}s
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
public enum FlowerpotTimeInterval {
    DAILY(ChronoUnit.DAYS, 1),
    WEEKLY(ChronoUnit.WEEKS, 1),
    MONTHLY(ChronoUnit.MONTHS, 1),
    YEARLY(ChronoUnit.YEARS, 1);

    public final ChronoUnit unit;

    public final int amount;

    FlowerpotTimeInterval(final ChronoUnit unit, final int amount) {
        this.unit = unit;
        this.amount = amount;
    }


    //  METHODS

    /**
     * Converts a string value into a {@link FlowerpotTimeInterval}
     *
     * @param code string
     * @return {@link FlowerpotTimeInterval}
     */
    public static FlowerpotTimeInterval getInterval(final String code) {

        if (StringUtils.isEmpty(code)) {
            throw new IllegalArgumentException("code cannot be empty");
        }

        return switch (code) {
            case "DAILY" -> DAILY;
            case "WEEKLY" -> WEEKLY;
            case "MONTHLY" -> MONTHLY;
            default -> YEARLY;
        };
    }
}
