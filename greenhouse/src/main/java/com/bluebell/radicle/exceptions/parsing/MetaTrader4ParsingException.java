package com.bluebell.radicle.exceptions.parsing;

/**
 * Exception thrown during parsing MetaTrader4 market data
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
public class MetaTrader4ParsingException extends RuntimeException {

    public MetaTrader4ParsingException(final String message) {
        super(message);
    }

    public MetaTrader4ParsingException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
