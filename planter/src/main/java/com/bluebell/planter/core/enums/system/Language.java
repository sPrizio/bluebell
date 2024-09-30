package com.bluebell.planter.core.enums.system;

import lombok.Getter;

/**
 * Enumeration of various languages
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
public enum Language {

    ENGLISH("EN", "English"),
    FRENCH("FR", "French"),
    SPANISH("ES", "Spanish"),
    GERMAN("DE", "German"),
    CHINESE("ZH", "Chinese"),
    ITALIAN("IT", "Italian"),
    PORTUGUESE("PT", "Portuguese"),
    RUSSIAN("RU", "Russian");

    private final String isoCode;

    private final String label;

    Language(final String isoCode, final String label) {
        this.isoCode = isoCode;
        this.label = label;
    }
}
