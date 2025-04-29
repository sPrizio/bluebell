package com.bluebell.radicle.importing.models.wrapper.trade;

import com.bluebell.radicle.importing.models.wrapper.ImportedWrapper;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * A wrapper class for CMC trades
 *
 * @param dateTime           trade date & time
 * @param type               type of trade
 * @param orderNumber        order number
 * @param relatedOrderNumber related order number (specifically relating the closing of a trade to it's open)
 * @param product            symbol traded
 * @param units              units traded
 * @param price              price at time of trade
 * @param amount             net profit amount
 * @author Stephen Prizio
 * @version 0.1.8
 */
@Builder
public record CMCTradeWrapper(
        @Getter LocalDateTime dateTime,
        @Getter String type,
        @Getter String orderNumber,
        @Getter String relatedOrderNumber,
        @Getter String product,
        @Getter double units,
        @Getter double price,
        @Getter double amount
) implements ImportedWrapper<CMCTradeWrapper> {

    @Override
    public CMCTradeWrapper merge(final CMCTradeWrapper wrapper) {
        return wrapper;
    }
}
