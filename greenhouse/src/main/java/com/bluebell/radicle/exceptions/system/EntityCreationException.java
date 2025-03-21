package com.bluebell.radicle.exceptions.system;

/**
 * Exception thrown when an entity cannot be created properly
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class EntityCreationException extends RuntimeException {

    public EntityCreationException(final String message) {
        super(message);
    }

    public EntityCreationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
