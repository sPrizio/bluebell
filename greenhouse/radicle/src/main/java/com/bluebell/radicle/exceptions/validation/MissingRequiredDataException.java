package com.bluebell.radicle.exceptions.validation;

/**
 * Exception thrown when required data is not available or is missing
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class MissingRequiredDataException extends RuntimeException {

    public MissingRequiredDataException(String message) {
        super(message);
    }

    public MissingRequiredDataException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
