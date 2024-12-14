package com.bluebell.planter.core.models.nonentities.records.tradelog;

import java.util.List;

/**
 * Class representation of a total summing of a {@link List} of {@link TradeLogEntryRecord}s
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record TradeLogEntryRecordTotals(int accountsTraded, double netProfit, double netPoints, int trades, int winPercentage) {
}
