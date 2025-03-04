package com.bluebell.platform.models.core.nonentities.records.traderecord;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Represents a collection of {@link TradeRecord}s and some basic statistics about them
 *
 * @param tradeRecords {@link List} of {@link TradeRecord}s
 * @param tradeRecordTotals {@link TradeRecordTotals}
 * @author Stephen Prizio
 * @version 0.1.0
 */
@Schema(title = "TradeRecordReport", name = "TradeRecordReport", description = "A data representation of a list of trade records and a summary of trading activity for all of the records (aggregation)")
public record TradeRecordReport(
        @Getter @Schema(description = "List of trade records") List<TradeRecord> tradeRecords,
        @Getter @Schema(description = "Summary of trading activity for the list of records") TradeRecordTotals tradeRecordTotals
) { }
