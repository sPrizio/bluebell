package com.bluebell.planter.core.enums.strategy;

import lombok.Getter;

/**
 * Enum representing different strategy reporting software
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@Getter
public enum StrategyPlatform {
    BLUEBELL("BLUEBELL_ANTHER", "Bluebell - anther", ".csv"),
    METATRADER4("METATRADER4", "MetaTrader 4", ".html", ".htm"),
    UNDEFINED("N/A", "N/A");

    private final String code;

    private final String label;

    private final String[] formats;

    StrategyPlatform(final String code, final String label, final String... formats) {
        this.code = code;
        this.label = label;
        this.formats = formats;
    }
}
