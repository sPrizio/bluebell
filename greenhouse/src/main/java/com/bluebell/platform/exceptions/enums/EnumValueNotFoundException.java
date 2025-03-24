package com.bluebell.platform.exceptions.enums;

/**
 * Exception when an enum is not found by the given code
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
public class EnumValueNotFoundException extends RuntimeException {

    public EnumValueNotFoundException(final String message) {
        super(message);
    }

    public EnumValueNotFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}

