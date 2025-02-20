package com.bluebell.planter.core.exceptions.security;

import com.bluebell.planter.core.models.entities.security.User;

/**
 * Exception thrown when a {@link User} already has the same username
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
public class DuplicateUserUsernameException extends RuntimeException {

    public DuplicateUserUsernameException(final String message) {
        super(message);
    }

    public DuplicateUserUsernameException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}