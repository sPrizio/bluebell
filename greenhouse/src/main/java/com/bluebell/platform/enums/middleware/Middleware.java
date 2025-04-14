package com.bluebell.platform.enums.middleware;

import com.bluebell.platform.enums.GenericEnum;
import lombok.Getter;

/**
 * Enumeration of the various middleware systems used by bluebell
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Getter
public enum Middleware implements GenericEnum<Middleware> {
    MT4_MARKET_DATA_MIDDLEWARE("MT4_MARKET_DATA_MIDDLEWARE", "MetaTrader 4 Market Data Middleware"),
    MT4_TRADE_DATA_MIDDLEWARE("MT4_TRADE_DATA_MIDDLEWARE", "MetaTrader 4 Trade Data Middleware"),;

    private final String code;

    private final String label;

    Middleware(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
