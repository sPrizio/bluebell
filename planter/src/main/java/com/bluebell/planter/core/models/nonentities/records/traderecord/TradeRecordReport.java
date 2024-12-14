package com.bluebell.planter.core.models.nonentities.records.traderecord;

import java.util.List;

/**
 * Represents a collection of {@link TradeRecord}s and some basic statistics about them
 *
 * @param tradeRecords {@link List} of {@link TradeRecord}s
 * @param tradeRecordTotals {@link TradeRecordTotals}
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record TradeRecordReport(List<TradeRecord> tradeRecords, TradeRecordTotals tradeRecordTotals) {
}
