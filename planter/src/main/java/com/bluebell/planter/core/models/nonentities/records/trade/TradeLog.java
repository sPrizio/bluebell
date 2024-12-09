package com.bluebell.planter.core.models.nonentities.records.trade;

import com.bluebell.planter.core.models.nonentities.records.tradeRecord.TradeRecord;

import java.time.LocalDate;
import java.util.List;

/**
 * Class representation of a {@link TradeLog}, which is a collection of {@link TradeRecord}s for a time span
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record TradeLog(LocalDate start, LocalDate end, List<TradeRecord> records) {
}
