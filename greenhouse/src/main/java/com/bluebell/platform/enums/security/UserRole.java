package com.bluebell.platform.enums.security;

import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.models.core.entities.security.User;
import lombok.Getter;

/**
 * Enumeration of a various roles that a {@link User} may possess
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Getter
public enum UserRole implements GenericEnum<UserRole> {
    ADMINISTRATOR("ADMINISTRATOR", "Admin"),
    SYSTEM("SYSTEM", "System"),
    TRADER("TRADER", "Trader");

    private final String code;

    private final String label;

    UserRole(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
