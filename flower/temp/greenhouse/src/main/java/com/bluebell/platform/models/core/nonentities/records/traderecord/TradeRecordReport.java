package com.bluebell.platform.models.core.nonentities.records.traderecord;

import java.util.List;

import lombok.Getter;

/**
 * Represents a collection of {@link TradeRecord}s and some basic statistics about them
 *
 * @param tradeRecords {@link List} of {@link TradeRecord}s
 * @param tradeRecordTotals {@link TradeRecordTotals}
 * @author Stephen Prizio
 * @version 0.1.0
 */
public record TradeRecordReport(
        @Getter List<TradeRecord> tradeRecords,
        @Getter TradeRecordTotals tradeRecordTotals
) { }
