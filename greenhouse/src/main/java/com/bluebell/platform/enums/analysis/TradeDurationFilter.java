package com.bluebell.platform.enums.analysis;

import lombok.Getter;

/**
 * Enumeration of types of trade durations to filter by
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
public enum TradeDurationFilter {
    ALL("ALL", "All Trades"),
    WINS("WINS", "Winning Trades"),
    LOSSES("LOSSES", "Losing Trades");

    private final String code;

    private final String name;

    TradeDurationFilter(final String code, final String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Gets the {@link TradeDurationFilter} for the given code
     *
     * @param code code
     * @return {@link TradeDurationFilter}
     */
    public static TradeDurationFilter getTradeDurationFilter(final String code) {
        return switch (code) {
            case "WINS" -> WINS;
            case "LOSSES" -> LOSSES;
            default -> ALL;
        };
    }
}
