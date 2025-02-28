package com.bluebell.platform.models.core.nonentities.records.traderecord;

import lombok.Getter;

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
 * @version 0.1.0
 */
public record TradeRecordTotals(
        @Getter int count,
        @Getter int trades,
        @Getter int tradesWon,
        @Getter int tradesLost,
        @Getter int winPercentage,
        @Getter double netProfit,
        @Getter double netPoints
) { }
