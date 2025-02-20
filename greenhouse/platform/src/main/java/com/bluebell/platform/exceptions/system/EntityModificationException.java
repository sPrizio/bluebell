package com.bluebell.platform.exceptions.system;

/**
 * Exception thrown when an entity cannot be modified correctly
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class EntityModificationException extends RuntimeException {

    public EntityModificationException(final String message) {
        super(message);
    }

    public EntityModificationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
