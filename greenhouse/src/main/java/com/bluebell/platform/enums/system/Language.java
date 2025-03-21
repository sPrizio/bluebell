package com.bluebell.platform.enums.system;

import com.bluebell.platform.enums.GenericEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Enumeration of various languages
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
@Schema(title = "Language", name = "Language", description = "Languages supported by bluebell")
public enum Language implements GenericEnum<Language> {
    ENGLISH("EN", "EN", "English"),
    FRENCH("FR", "FR", "French"),
    SPANISH("ES", "ES", "Spanish"),
    GERMAN("DE", "DE", "German"),
    CHINESE("ZH", "ZH", "Chinese"),
    ITALIAN("IT", "IT", "Italian"),
    PORTUGUESE("PT", "PT", "Portuguese"),
    RUSSIAN("RU", "RU", "Russian");

    private final String code;

    private final String isoCode;

    private final String label;

    Language(final String code, final String isoCode, final String label) {
        this.code = code;
        this.isoCode = isoCode;
        this.label = label;
    }
}
