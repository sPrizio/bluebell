package com.bluebell.platform.enums.chart;


import com.bluebell.platform.enums.GenericEnum;
import lombok.Getter;

/**
 * Class representation of an intraday interval
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
public enum IntradayInterval implements GenericEnum<IntradayInterval> {
    ONE_MINUTE("one-minute", "One Minute"),
    FIVE_MINUTES("five-minute", "Five Minutes"),
    TEN_MINUTES("ten-minute", "Ten Minutes"),
    FIFTEEN_MINUTES("fifteen-minute", "Fifteen Minutes"),
    THIRTY_MINUTES("thirty-minute", "Thirty Minutes"),
    ONE_HOUR("one-hour", "One Hour"),
    ONE_DAY("one-day", "One Day");

    private final String code;

    private final String label;

    IntradayInterval(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
