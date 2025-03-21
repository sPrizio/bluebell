package com.bluebell.platform.enums.analysis;

import com.bluebell.platform.enums.GenericEnum;
import lombok.Getter;

/**
 * Enumeration of types of trade durations to filter by
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
public enum TradeDurationFilter implements GenericEnum<TradeDurationFilter> {
    ALL("ALL", "All Trades"),
    WINS("WINS", "Winning Trades"),
    LOSSES("LOSSES", "Losing Trades");

    private final String code;

    private final String label;

    TradeDurationFilter(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
