package com.bluebell.platform.models.core.nonentities.records.trade;


import java.time.LocalDateTime;

import com.bluebell.platform.models.core.entities.trade.Trade;

/**
 * Record representing a cumulative {@link Trade}, i.e. a trade with previous trade information in it
 *
 * @param tradeCloseTime trade close time
 * @param count count of trades
 * @param singleProfit individual trade profit/loss
 * @param singlePoints individual trade points
 * @param netProfit cumulative profit
 * @param netPoints cumulative points
 * @author Stephen Prizio
 * @version 0.0.9
 */
public record CumulativeTrade(LocalDateTime tradeCloseTime, int count, double singleProfit, double singlePoints, double netProfit, double netPoints) {
}
