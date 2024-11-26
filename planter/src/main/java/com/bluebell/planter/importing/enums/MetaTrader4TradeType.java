package com.bluebell.planter.importing.enums;

import lombok.Getter;

/**
 * Represents the type of trade entries that an MT4 report consists of
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@Getter
public enum MetaTrader4TradeType {
    BUY("Buy","buy"),
    SELL("Sell","sell"),
    TAKE_PROFIT("Take Profit","t/p"),
    STOP_LOSS("Stop Loss","s/l"),
    CLOSE("Close", "close"),
    CLOSE_AT_STOP("Close at Stop", "close at stop");

    private final String label;

    private final String code;

    MetaTrader4TradeType(final String label, final String code) {
        this.label = label;
        this.code = code;
    }


    //  METHODS

    /**
     * Returns true if the given code matches to an entry signal
     *
     * @param code test string
     * @return true if buy or sell
     */
    public static boolean isEntry(final String code) {
        return code.equals(BUY.code) || code.equals(SELL.code);
    }

    /**
     * Returns true if the given code matches to an exit signal
     *
     * @param code test string
     * @return true if s/l or t/p
     */
    public static boolean isExit(final String code) {
        return code.equals(TAKE_PROFIT.code) || code.equals(STOP_LOSS.code) || code.equals(CLOSE.code) || code.equals(CLOSE_AT_STOP.code);
    }
}
