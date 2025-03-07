package com.bluebell.platform.models.core.nonentities.records.trade;


import com.bluebell.platform.models.core.entities.trade.Trade;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
 * @version 0.1.1
 */
@Builder
public record CumulativeTrade(
        @Getter LocalDateTime tradeCloseTime,
        @Getter int count,
        @Getter double singleProfit,
        @Getter double singlePoints,
        @Getter double netProfit,
        @Getter double netPoints
) { }
