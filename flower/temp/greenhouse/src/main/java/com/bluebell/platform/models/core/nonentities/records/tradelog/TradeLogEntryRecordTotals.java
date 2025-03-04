package com.bluebell.platform.models.core.nonentities.records.tradelog;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

/**
 * Class representation of a total summing of a {@link List} of {@link TradeLogEntryRecord}s
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Schema(title = "TradeLogEntryRecordTotals", name = "TradeLogEntryRecordTotals", description = "Summary of trade log entries for a time span")
public record TradeLogEntryRecordTotals(
        @Getter @Schema(description = "Number of accounts traded") int accountsTraded,
        @Getter @Schema(description = "Net profit for period") double netProfit,
        @Getter @Schema(description = "Net points for period") double netPoints,
        @Getter @Schema(description = "Number of trades taken in period") int trades,
        @Getter @Schema(description = "Win percentage for time period") int winPercentage
) { }
