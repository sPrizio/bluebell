package com.bluebell.radicle.security.exceptions;

/**
 * Exception thrown when the given API token is invalid
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class InvalidApiTokenException extends RuntimeException {

    public InvalidApiTokenException(final String message) {
        super(message);
    }

    public InvalidApiTokenException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
