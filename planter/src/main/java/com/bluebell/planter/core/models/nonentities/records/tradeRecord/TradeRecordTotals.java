package com.bluebell.planter.core.models.nonentities.records.tradeRecord;

import java.util.List;

/**
 * Represents a simple statistical analysis of a {@link List} of {@link TradeRecord}s
 *
 * @param count         number of records
 * @param trades        number of trades
 * @param winPercentage win percentage
 * @param netProfit     net profit
 * @param netPoints     net points
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record TradeRecordTotals(int count, int trades, int winPercentage, double netProfit, double netPoints) {
}
