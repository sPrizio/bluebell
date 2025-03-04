package com.bluebell.platform.enums.trade;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Enum representing different trading platforms
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
@Schema(title = "TradePlatform", name = "TradePlatform Enum", description = "List of Trade Platforms supported in bluebell.")
public enum TradePlatform {
    BLUEBELL("BLUEBELL_ANTHER", "Anther", ".csv"),
    CMC_MARKETS("CMC_MARKETS", "CMC Markets", ".csv"),
    METATRADER4("METATRADER4", "MetaTrader 4", ".html", ".htm"),
    METATRADER5("METATRADER5", "MetaTrader 5", ".html", ".htm"),
    CTRADER("CTRADER", "CTrader", ".csv"),
    UNDEFINED("N/A", "N/A");

    private final String code;

    private final String label;

    private final String[] formats;

    TradePlatform(final String code, final String label, final String... formats) {
        this.code = code;
        this.label = label;
        this.formats = formats;
    }


    //  METHODS

    /**
     * Get enum by code
     *
     * @param code input code
     * @return {@link TradePlatform}
     */
    public static TradePlatform getByCode(final String code) {
        return switch (code) {
            case "BLUEBELL_ANTHER" -> BLUEBELL;
            case "CMC_MARKETS" -> CMC_MARKETS;
            case "METATRADER4" -> METATRADER4;
            case "METATRADER5" -> METATRADER5;
            case "CTRADER" -> CTRADER;
            default -> UNDEFINED;
        };
    }
}
