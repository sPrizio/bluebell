package com.bluebell.planter.core.models.nonentities.records.traderecord;

import java.util.List;

/**
 * Represents a simple statistical analysis of a {@link List} of {@link TradeRecord}s
 *
 * @param count         number of records
 * @param trades        number of trades
 * @param tradesWon     number of trades won
 * @param tradesLost    number of trades lost
 * @param winPercentage win percentage
 * @param netProfit     net profit
 * @param netPoints     net points
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record TradeRecordTotals(int count, int trades, int tradesWon, int tradesLost, int winPercentage, double netProfit, double netPoints) {
}
