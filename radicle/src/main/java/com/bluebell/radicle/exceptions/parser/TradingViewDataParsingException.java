package com.bluebell.radicle.exceptions.parser;

/**
 * Exception thrown during parsing TradingView data
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
public class TradingViewDataParsingException extends RuntimeException {

    public TradingViewDataParsingException(final String message) {
        super(message);
    }

    public TradingViewDataParsingException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
