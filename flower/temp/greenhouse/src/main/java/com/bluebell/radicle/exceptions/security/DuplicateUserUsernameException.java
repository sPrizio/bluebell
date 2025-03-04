package com.bluebell.radicle.exceptions.security;


import com.bluebell.platform.models.core.entities.security.User;

/**
 * Exception thrown when a {@link User} already has the same username
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class DuplicateUserUsernameException extends RuntimeException {

    public DuplicateUserUsernameException(final String message) {
        super(message);
    }

    public DuplicateUserUsernameException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}