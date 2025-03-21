package com.bluebell.platform.enums.system;

import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.time.temporal.ChronoUnit;

/**
 * Enum representing various time intervals for {@link TradeRecord}s
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
@Schema(title = "TradeRecordTimeInterval", name = "TradeRecordTimeInterval", description = "The aggregate units that trade records can be separated, i.e. number of trades taken for this unit of time")
public enum TradeRecordTimeInterval implements GenericEnum<TradeRecordTimeInterval> {
    DAILY("DAILY", "Daily", ChronoUnit.DAYS, 1),
    WEEKLY("WEEKLY", "Weekly", ChronoUnit.WEEKS, 1),
    MONTHLY("MONTHLY", "Monthly", ChronoUnit.MONTHS, 1),
    YEARLY("YEARLY", "Yearly", ChronoUnit.YEARS, 1);

    private final String code;

    private final String label;

    private final ChronoUnit unit;

    private final int amount;

    TradeRecordTimeInterval(final String code, final String label, final ChronoUnit unit, final int amount) {
        this.code = code;
        this.label = label;
        this.unit = unit;
        this.amount = amount;
    }
}
