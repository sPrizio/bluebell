package com.bluebell.platform.exceptions.security;

import com.bluebell.platform.models.core.entities.security.User;

/**
 * Exception when a {@link User} is not found
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(final String message) {
        super(message);
    }

    public UserNotFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
