package com.bluebell.planter.exceptions;

/**
 * Exception thrown when an incoming value cannot be converted to a valid enum
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class InvalidEnumException extends RuntimeException {

    public InvalidEnumException(final String message) {
        super(message);
    }

    public InvalidEnumException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
