package com.bluebell.platform.enums.news;

import com.bluebell.platform.enums.GenericEnum;
import lombok.Getter;

/**
 * Enum representing possible severity danger/warning levels for news, indicating their overall likelihood
 * of moving the market in a significant direction
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
public enum MarketNewsSeverity implements GenericEnum<MarketNewsSeverity> {

    NONE("NONE", "None", 0),
    DANGEROUS("DANGEROUS", "Dangerous", 1),
    MODERATE("MODERATE", "Moderate", 2),
    LOW("LOW", "Low", 3),
    HOLIDAY("HOLIDAY", "Holiday", -1);

    private final String code;

    private final String label;

    private final int level;

    MarketNewsSeverity(final String code, final String label, final int level) {
        this.code = code;
        this.label = label;
        this.level = level;
    }

    /**
     * Converts a level to a {@link MarketNewsSeverity}
     *
     * @param level level
     * @return {@link MarketNewsSeverity}
     */
    public static MarketNewsSeverity getByLevel(final int level) {
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
     * @param label label
     * @return {@link MarketNewsSeverity}
     */
    public static MarketNewsSeverity getFromLabel(final String label) {
        return switch (label) {
            case "Low" -> LOW;
            case "Medium" -> MODERATE;
            case "High" -> DANGEROUS;
            case "Holiday" -> HOLIDAY;
            default -> NONE;
        };
    }
}
