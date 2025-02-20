package com.bluebell.platform.models.core.nonentities.records.tradelog;


import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecord;

import java.util.List;

/**
 * Class representation of a {@link TradeLog}, which is a collection of {@link TradeRecord}s for various accounts within a time span
 *
 * @param entries {@link List} of {@link TradeLogEntry}
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record TradeLog(List<TradeLogEntry> entries) {
}
