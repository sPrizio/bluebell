package com.bluebell.platform.enums.system;

import com.bluebell.platform.enums.GenericEnum;
import lombok.Getter;

/**
 * Enum representing a positive or negative value
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
public enum Parity implements GenericEnum<Parity> {
    POSITIVE("POSITIVE", "Positive"),
    NEGATIVE("NEGATIVE", "Negative");

    private final String code;

    private final String label;

    Parity(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
