package com.bluebell.radicle.exceptions.parsing;

/**
 * Exception thrown during parsing FirstRateData
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class FirstRateDataParsingException extends RuntimeException {

    public FirstRateDataParsingException(final String message) {
        super(message);
    }

    public FirstRateDataParsingException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
