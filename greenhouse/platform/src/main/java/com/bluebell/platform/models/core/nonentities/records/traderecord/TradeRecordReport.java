package com.bluebell.platform.models.core.nonentities.records.traderecord;


import java.util.List;

/**
 * Represents a collection of {@link TradeRecord}s and some basic statistics about them
 *
 * @param tradeRecords {@link List} of {@link TradeRecord}s
 * @param tradeRecordTotals {@link TradeRecordTotals}
 * @author Stephen Prizio
 * @version 0.0.9
 */
public record TradeRecordReport(List<TradeRecord> tradeRecords, TradeRecordTotals tradeRecordTotals) {
}
