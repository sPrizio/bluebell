package com.bluebell.platform.exceptions.calculator;

/**
 * Exception thrown when a zero value appears in an invalid location
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class UnexpectedZeroValueException extends RuntimeException {

    public UnexpectedZeroValueException(final String message) {
        super(message);
    }

    public UnexpectedZeroValueException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
