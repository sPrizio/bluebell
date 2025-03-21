package com.bluebell.platform.enums.market;

import com.bluebell.platform.enums.GenericEnum;
import lombok.Getter;

/**
 * Representation of the market's direction
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
public enum MarketDirection implements GenericEnum<MarketDirection> {
    BULLISH("BULLISH", "Bullish"),
    BEARISH("BEARISH", "Bearish");

    private final String code;

    private final String label;

    MarketDirection(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
