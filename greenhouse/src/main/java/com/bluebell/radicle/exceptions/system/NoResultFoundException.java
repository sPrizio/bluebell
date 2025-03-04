package com.bluebell.radicle.exceptions.system;

/**
 * Custom exception for empty result sets
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class NoResultFoundException extends RuntimeException {

    public NoResultFoundException(final String message) {
        super(message);
    }

    public NoResultFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
