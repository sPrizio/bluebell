package com.bluebell.flowerpot.core.exceptions.validation;

/**
 * Exception for a missing json property
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public class JsonMissingPropertyException extends RuntimeException {

    public JsonMissingPropertyException(final String message) {
        super(message);
    }

    public JsonMissingPropertyException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
