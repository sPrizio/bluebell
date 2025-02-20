package com.bluebell.platform.exceptions.system;

/**
 * A generic system exception that can be thrown from anywhere
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class GenericSystemException extends RuntimeException {

    public GenericSystemException(final String message) {
        super(message);
    }

    public GenericSystemException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
