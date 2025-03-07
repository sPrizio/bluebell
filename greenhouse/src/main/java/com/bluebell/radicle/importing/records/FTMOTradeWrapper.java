package com.bluebell.radicle.importing.records;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * A wrapper class for FTMO trades
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
public record FTMOTradeWrapper(
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
     * Merges 2 {@link FTMOTradeWrapper}s
     *
     * @param wrapper {@link FTMOTradeWrapper}
     * @return merged {@link FTMOTradeWrapper}
     */
    public FTMOTradeWrapper merge(final FTMOTradeWrapper wrapper) {
        return new FTMOTradeWrapper(
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
