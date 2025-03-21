package com.bluebell.anther.enums;

import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.enums.trade.TradeType;
import lombok.Getter;

/**
 * An enumeration of possible trade signals
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
public enum TradeSignal implements GenericEnum<TradeSignal> {
    BUY_SIGNAL("BUY_SIGNAL", "Buy Signal", TradeType.BUY),
    SELL_SIGNAL("SELL_SIGNAL", "Sell Signal", TradeType.SELL),
    NO_SIGNAL("NO_SIGNAL", "No Signal", TradeType.NA);

    private final String code;

    private final String label;

    private final TradeType tradeType;

    TradeSignal(final String code, final String label, final TradeType tradeType) {
        this.code = code;
        this.label = label;
        this.tradeType = tradeType;
    }
}
