package com.bluebell.radicle.exceptions.account;

/**
 * Exception thrown when comparing account numbers
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class InvalidAccountNumberException extends RuntimeException {

    public InvalidAccountNumberException(final String message) {
        super(message);
    }

    public InvalidAccountNumberException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
