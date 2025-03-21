package com.bluebell.radicle.importing.models;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * A wrapper class for MetaTrader4 trades
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Builder
public record MetaTrader4TradeWrapper(
        @Getter String ticketNumber,
        @Getter LocalDateTime openTime,
        @Getter LocalDateTime closeTime,
        @Getter String type,
        @Getter double size,
        @Getter String item,
        @Getter double openPrice,
        @Getter double stopLoss,
        @Getter double takeProfit,
        @Getter double closePrice,
        @Getter double profit
) implements ImportedTradeWrapper<MetaTrader4TradeWrapper> {

    /**
     * Merges 2 {@link MetaTrader4TradeWrapper}s
     *
     * @param wrapper {@link MetaTrader4TradeWrapper}
     * @return merged {@link MetaTrader4TradeWrapper}
     */
    public MetaTrader4TradeWrapper merge(final MetaTrader4TradeWrapper wrapper) {
        return MetaTrader4TradeWrapper
                .builder()
                .ticketNumber(this.ticketNumber == null ? wrapper.ticketNumber() : this.ticketNumber)
                .openTime(this.openTime == null ? wrapper.openTime() : this.openTime)
                .closeTime(this.closeTime == null ? wrapper.closeTime() : this.closeTime)
                .type(wrapper.type())
                .size(this.size == 0.0 ? wrapper.size() : this.size)
                .item(this.item == null ? wrapper.item() : this.item)
                .openPrice(this.openPrice == 0.0 ? wrapper.openPrice() : this.openPrice)
                .stopLoss(this.stopLoss == 0.0 ? wrapper.stopLoss() : this.stopLoss)
                .takeProfit(this.takeProfit == 0.0 ? wrapper.takeProfit() : this.takeProfit)
                .closePrice(this.closePrice == 0.0 ? wrapper.closePrice() : this.closePrice)
                .profit(this.profit == 0.0 ? wrapper.profit() : this.profit)
                .build();
    }
}
