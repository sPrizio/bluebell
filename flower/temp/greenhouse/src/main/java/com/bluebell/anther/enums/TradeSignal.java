package com.bluebell.anther.enums;

import com.bluebell.platform.enums.trade.TradeType;
import lombok.Getter;

/**
 * An enumeration of possible trade signals
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
public enum TradeSignal {
    BUY_SIGNAL(TradeType.BUY),
    SELL_SIGNAL(TradeType.SELL),
    NO_SIGNAL(TradeType.NA);

    private final TradeType tradeType;

    TradeSignal(final TradeType tradeType) {
        this.tradeType = tradeType;
    }
}
