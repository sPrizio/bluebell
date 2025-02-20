package com.bluebell.planter.core.exceptions.validation;

/**
 * Custom exception for non unique items in the database
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public class NonUniqueItemFoundException extends RuntimeException {

    public NonUniqueItemFoundException(final String message) {
        super(message);
    }

    public NonUniqueItemFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
