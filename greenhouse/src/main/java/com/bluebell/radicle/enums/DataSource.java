package com.bluebell.radicle.enums;

import com.bluebell.platform.enums.GenericEnum;
import lombok.Getter;

/**
 * Enum representing the different data sources for historical trading data
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
public enum DataSource implements GenericEnum<DataSource> {
    FIRST_RATE_DATA("FIRST_RATE_DATA", "First Rate Data"),
    METATRADER4("METATRADER4", "Metatrader 4"),
    TRADING_VIEW("TRADING_VIEW", "Trading View");

    private final String code;

    private final String label;

    DataSource(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
