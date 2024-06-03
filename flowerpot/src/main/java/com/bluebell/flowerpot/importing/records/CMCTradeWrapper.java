package com.bluebell.flowerpot.importing.records;

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
 * @version 0.0.2
 */
public record CMCTradeWrapper(
        LocalDateTime dateTime,
        String type,
        String orderNumber,
        String relatedOrderNumber,
        String product,
        double units,
        double price,
        double amount
) {
}
