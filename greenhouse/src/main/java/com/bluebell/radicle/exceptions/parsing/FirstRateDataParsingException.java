package com.bluebell.radicle.exceptions.parsing;

/**
 * Exception thrown during parsing FirstRateData market data
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
public class FirstRateDataParsingException extends RuntimeException {

    public FirstRateDataParsingException(final String message) {
        super(message);
    }

    public FirstRateDataParsingException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
