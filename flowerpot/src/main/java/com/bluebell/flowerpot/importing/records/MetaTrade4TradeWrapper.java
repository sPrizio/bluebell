package com.bluebell.flowerpot.importing.records;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * A wrapper class for MetaTrader4 trades
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public record MetaTrade4TradeWrapper(
        String ticketNumber,
        @Getter LocalDateTime openTime,
        @Getter LocalDateTime closeTime,
        String type,
        double size,
        String item,
        double openPrice,
        double stopLoss,
        double takeProfit,
        double closePrice,
        double profit
) {
}
