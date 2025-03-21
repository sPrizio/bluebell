package com.bluebell.platform.enums.system;

import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;

import java.time.temporal.ChronoUnit;

/**
 * Enum representing various time intervals for {@link TradeRecord}s
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Schema(title = "TradeRecordTimeInterval", name = "TradeRecordTimeInterval", description = "The aggregate units that trade records can be separated, i.e. number of trades taken for this unit of time")
public enum TradeRecordTimeInterval {
    DAILY(ChronoUnit.DAYS, 1),
    WEEKLY(ChronoUnit.WEEKS, 1),
    MONTHLY(ChronoUnit.MONTHS, 1),
    YEARLY(ChronoUnit.YEARS, 1);

    public final ChronoUnit unit;

    public final int amount;

    TradeRecordTimeInterval(final ChronoUnit unit, final int amount) {
        this.unit = unit;
        this.amount = amount;
    }


    //  METHODS

    /**
     * Converts a string value into a {@link TradeRecordTimeInterval}
     *
     * @param code string
     * @return {@link TradeRecordTimeInterval}
     */
    public static TradeRecordTimeInterval getInterval(final String code) {

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
