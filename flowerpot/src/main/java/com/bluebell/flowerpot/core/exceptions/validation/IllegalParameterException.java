package com.bluebell.flowerpot.core.exceptions.validation;

/**
 * Custom exception for illegal method parameters
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public class IllegalParameterException extends RuntimeException {

    public IllegalParameterException(final String message) {
        super(message);
    }

    public IllegalParameterException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
