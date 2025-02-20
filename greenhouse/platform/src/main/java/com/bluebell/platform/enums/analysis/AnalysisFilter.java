package com.bluebell.platform.enums.analysis;

import lombok.Getter;

/**
 * Enumeration of that various data filters that can be applied to the results of an analysis
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@Getter
public enum AnalysisFilter {
    POINTS("POINTS", "Points"),
    PROFIT("PROFIT", "Profit"),
    WIN_PERCENTAGE("PERCENTAGE", "Win %");

    private final String code;

    private final String name;

    AnalysisFilter(final String code, final String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Gets the {@link AnalysisFilter} for the given code
     *
     * @param code code
     * @return {@link AnalysisFilter}
     */
    public static AnalysisFilter getAnalysisFilter(final String code) {
        return switch (code) {
            case "POINTS" -> POINTS;
            case "PROFIT" -> PROFIT;
            case "PERCENTAGE" -> WIN_PERCENTAGE;
            default -> null;
        };
    }
}
