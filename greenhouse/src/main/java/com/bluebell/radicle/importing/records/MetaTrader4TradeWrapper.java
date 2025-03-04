package com.bluebell.radicle.importing.records;

import java.time.LocalDateTime;

import lombok.Getter;

/**
 * A wrapper class for MetaTrader4 trades
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public record MetaTrader4TradeWrapper(
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

    /**
     * Merges 2 {@link MetaTrader4TradeWrapper}s
     *
     * @param wrapper {@link MetaTrader4TradeWrapper}
     * @return merged {@link MetaTrader4TradeWrapper}
     */
    public MetaTrader4TradeWrapper merge(final MetaTrader4TradeWrapper wrapper) {
        return new MetaTrader4TradeWrapper(
                ticketNumber == null ? wrapper.ticketNumber() : ticketNumber,
                openTime == null ? wrapper.openTime() : openTime,
                closeTime == null ? wrapper.closeTime() : closeTime,
                wrapper.type(),
                size == 0.0 ? wrapper.size() : size,
                item == null ? wrapper.item() : item,
                openPrice == 0.0 ? wrapper.openPrice() : openPrice,
                stopLoss == 0.0 ? wrapper.stopLoss() : stopLoss,
                takeProfit == 0.0 ? wrapper.takeProfit() : takeProfit,
                closePrice == 0.0 ? wrapper.closePrice() : closePrice,
                profit == 0.0 ? wrapper.profit() : profit
        );
    }
}
