package com.bluebell.planter.core.exceptions.security;

import com.bluebell.planter.core.models.entities.security.User;

/**
 * Exception thrown when a {@link User} already has the same email
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
public class DuplicateUserEmailException extends RuntimeException {

    public DuplicateUserEmailException(final String message) {
        super(message);
    }

    public DuplicateUserEmailException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
