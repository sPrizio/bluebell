package com.bluebell.flowerpot.core.enums.security;

import com.bluebell.flowerpot.core.models.entities.security.User;
import lombok.Getter;

/**
 * Enumeration of a various roles that a {@link User} may possess
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
public enum UserRole {
    ADMINISTRATOR("Admin"),
    TRADER("Trader");

    private final String label;

    UserRole(final String label) {
        this.label = label;
    }
}
