package com.bluebell.processing.enums;

import lombok.Getter;

/**
 * Enum representing that various types for module-info.java dependencies
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
public enum DependencyType {
    EXPORTS("exports"),
    OPENS("opens"),
    REQUIRES("requires"),
    REQUIRES_STATIC("requires static"),;

    private final String codeSyntax;

    DependencyType(final String codeSyntax) {
        this.codeSyntax = codeSyntax;
    }
}
