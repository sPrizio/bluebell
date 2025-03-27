package com.bluebell.radicle.exceptions.parsing;

/**
 * Exception thrown during parsing TradingView market data
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
public class TradingViewDataParsingException extends RuntimeException {

    public TradingViewDataParsingException(final String message) {
        super(message);
    }

    public TradingViewDataParsingException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
