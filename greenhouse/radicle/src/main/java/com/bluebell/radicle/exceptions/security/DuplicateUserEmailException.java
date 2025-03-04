package com.bluebell.radicle.exceptions.security;

import com.bluebell.platform.models.core.entities.security.User;

/**
 * Exception thrown when a {@link User} already has the same email
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class DuplicateUserEmailException extends RuntimeException {

    public DuplicateUserEmailException(final String message) {
        super(message);
    }

    public DuplicateUserEmailException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
