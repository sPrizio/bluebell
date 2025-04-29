package com.bluebell.radicle.importing.models.wrapper.trade;

import com.bluebell.radicle.importing.models.wrapper.ImportedWrapper;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * A wrapper class for FTMO trades
 *
 * @author Stephen Prizio
 * @version 0.1.8
 */
@Builder
public record FTMOTradeWrapper(
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
) implements ImportedWrapper<FTMOTradeWrapper> {

    /**
     * Merges 2 {@link FTMOTradeWrapper}s
     *
     * @param wrapper {@link FTMOTradeWrapper}
     * @return merged {@link FTMOTradeWrapper}
     */
    public FTMOTradeWrapper merge(final FTMOTradeWrapper wrapper) {
        return FTMOTradeWrapper
                .builder()
                .ticketNumber(this.ticketNumber == null ? wrapper.ticketNumber() : this.ticketNumber)
                .openTime(this.openTime == null ? wrapper.openTime() : this.openTime())
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
