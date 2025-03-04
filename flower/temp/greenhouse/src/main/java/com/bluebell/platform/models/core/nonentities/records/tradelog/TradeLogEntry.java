package com.bluebell.platform.models.core.nonentities.records.tradelog;


import java.time.LocalDate;
import java.util.List;

import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Class representation of line item entry inside a {@link TradeLog}, consisting of a collection of {@link TradeRecord}s for an {@link Account} within a time span and a sum of their totals
 *
 * @param start start of time span
 * @param end end of time span
 * @param records {@link List} of {@link TradeLogEntryRecord}s
 * @param totals {@link TradeLogEntryRecordTotals}
 * @author Stephen Prizio
 * @version 0.1.0
 */
@Schema(title = "TradeLogEntry", name = "TradeLogEntry", description = "An entry of trade logs for a period of time")
public record TradeLogEntry(
        @Getter @Schema(description = "Start date") LocalDate start,
        @Getter @Schema(description = "End date") LocalDate end,
        @Getter @Schema(description = "List of records, trades taken and their associated accounts") List<TradeLogEntryRecord> records,
        @Getter @Schema(description = "Summary of trade log records for this period") TradeLogEntryRecordTotals totals
) { }
