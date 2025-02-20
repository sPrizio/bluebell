package com.bluebell.platform.exceptions.validation;

/**
 * Custom exception for non unique items in the database
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class NonUniqueItemFoundException extends RuntimeException {

    public NonUniqueItemFoundException(final String message) {
        super(message);
    }

    public NonUniqueItemFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
