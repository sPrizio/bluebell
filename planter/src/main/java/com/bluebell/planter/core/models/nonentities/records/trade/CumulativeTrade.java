package com.bluebell.planter.core.models.nonentities.records.trade;

import com.bluebell.planter.core.models.entities.trade.Trade;

import java.time.LocalDateTime;

/**
 * Record representing a cumulative {@link Trade}, i.e. a trade with previous trade information in it
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record CumulativeTrade(LocalDateTime tradeCloseTime, int count, double netProfit, double netPoints) {
}