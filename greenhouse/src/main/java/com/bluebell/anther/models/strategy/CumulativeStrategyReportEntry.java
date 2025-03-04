package com.bluebell.anther.models.strategy;

import java.time.LocalDateTime;

/**
 * Representation of a cumulative entry when computing strategy results
 *
 * @param points cumulative points
 * @param netProfit cumulative profit
 * @param trades cumulative trades
 * @param tradeType type of trade
 * @param opened open time
 * @param closed close time
 * @param pointsForTrade points only for this trade, not cumulative
 * @param profitForTrade profit only for this trade, not cumulative
 * @author Stephen Prizio
 * @version 0.0.9
 */
public record CumulativeStrategyReportEntry(double points, double netProfit, int trades, String tradeType, LocalDateTime opened, LocalDateTime closed, double pointsForTrade, double profitForTrade) {
}
