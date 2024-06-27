package com.bluebell.anther.models.strategy;

import java.time.LocalDateTime;

/**
 * Representation of a cumulative entry when computing strategy results
 *
 * @param points cumulative points
 * @param netProfit cumulative profit
 * @param trades cumulative trades
 * @param modified last modified time
 * @author Stephen Prizio
 * @version 0.0.1
 */
public record CumulativeStrategyReportEntry(double points, double netProfit, int trades, LocalDateTime modified) {
}
