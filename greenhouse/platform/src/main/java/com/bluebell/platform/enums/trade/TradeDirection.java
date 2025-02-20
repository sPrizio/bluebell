package com.bluebell.platform.enums.trade;

import com.bluebell.platform.models.core.entities.trade.Trade;
import lombok.Getter;

/**
 * Enumeration representing the direction of a {@link Trade}
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
public enum TradeDirection {

    BULLISH("Bullish", "Upward"),
    BEARISH("Bearish", "Downward");

    private final String label;

    private final String description;

    TradeDirection(final String label, final String description) {
        this.label = label;
        this.description = description;
    }
}
