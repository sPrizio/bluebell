package com.bluebell.flowerpot.core.enums.news;

import lombok.Getter;

/**
 * Enum representing possible severity danger/warning levels for news, indicating their overall likelihood
 * of moving the market in a significant direction
 *
 * @author Stephen Prizio
 * @version 0.0.4
 */
@Getter
public enum MarketNewsSeverity {

    NONE("None", 0),
    DANGEROUS("Dangerous", 1),
    MODERATE("Moderate", 2),
    LOW("Low", 3),
    HOLIDAY("Holiday", -1);

    private final String description;

    private final int level;

    MarketNewsSeverity(final String description, final int level) {
        this.description = description;
        this.level = level;
    }

    /**
     * Converts a level to a {@link MarketNewsSeverity}
     *
     * @param level level
     * @return {@link MarketNewsSeverity}
     */
    public static MarketNewsSeverity get(final int level) {
        return switch (level) {
            case 1 -> DANGEROUS;
            case 2 -> MODERATE;
            case 3 -> LOW;
            default -> NONE;
        };
    }

    /**
     * Converts a description into a {@link MarketNewsSeverity}
     *
     * @param description description
     * @return {@link MarketNewsSeverity}
     */
    public static MarketNewsSeverity getFromDescription(final String description) {
        return switch (description) {
            case "Low" -> LOW;
            case "Medium" -> MODERATE;
            case "High" -> DANGEROUS;
            case "Holiday" -> HOLIDAY;
            default -> NONE;
        };
    }
}
