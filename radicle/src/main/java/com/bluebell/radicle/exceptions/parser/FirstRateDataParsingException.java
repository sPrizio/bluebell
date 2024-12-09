package com.bluebell.radicle.exceptions.parser;

/**
 * Exception thrown during parsing FirstRateData
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
public class FirstRateDataParsingException extends RuntimeException {

    public FirstRateDataParsingException(final String message) {
        super(message);
    }

    public FirstRateDataParsingException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
