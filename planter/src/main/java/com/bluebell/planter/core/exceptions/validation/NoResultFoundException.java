package com.bluebell.planter.core.exceptions.validation;

/**
 * Custom exception for empty result sets
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public class NoResultFoundException extends RuntimeException {

    public NoResultFoundException(final String message) {
        super(message);
    }

    public NoResultFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
