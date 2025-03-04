package com.bluebell.radicle.security.exceptions;


import com.bluebell.platform.models.core.entities.security.User;

/**
 * Exception thrown when the API key does not map to a valid {@link User}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class NoValidUserForTokenException extends RuntimeException {

    public NoValidUserForTokenException(final String message) {
        super(message);
    }

    public NoValidUserForTokenException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
