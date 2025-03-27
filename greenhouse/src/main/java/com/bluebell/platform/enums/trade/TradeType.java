package com.bluebell.platform.enums.trade;

import com.bluebell.platform.enums.GenericEnum;
import lombok.Getter;

/**
 * Representation of a buy or sell
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
@Getter
public enum TradeType implements GenericEnum<TradeType> {
    BUY("BUY", "Buy"),
    SELL("SELL", "Sell"),
    NA("NA", "Not Available"),
    PROMOTIONAL_PAYMENT("PROMOTIONAL_PAYMENT", "Promotional Payment"),;

    private final String code;

    private final String label;

    TradeType(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
