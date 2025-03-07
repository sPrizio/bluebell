package com.bluebell.anther.models.strategy;

import lombok.Builder;
import lombok.Getter;

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
 * @version 0.1.1
 */
@Builder
public record CumulativeStrategyReportEntry(
        @Getter double points,
        @Getter double netProfit,
        @Getter int trades,
        @Getter String tradeType,
        @Getter LocalDateTime opened,
        @Getter LocalDateTime closed,
        @Getter double pointsForTrade,
        @Getter double profitForTrade
) { }
