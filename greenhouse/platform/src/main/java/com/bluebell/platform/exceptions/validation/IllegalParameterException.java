package com.bluebell.platform.exceptions.validation;

/**
 * Custom exception for illegal method parameters
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class IllegalParameterException extends RuntimeException {

    public IllegalParameterException(final String message) {
        super(message);
    }

    public IllegalParameterException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
