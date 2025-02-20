package com.bluebell.platform.enums.chart;


/**
 * Class representation of an intraday interval
 *
 * @author Stephen Prizio
 * @version 0.0.6
 */
public enum IntradayInterval {
    ONE_MINUTE("One Minute", "one-minute"),
    FIVE_MINUTES("Five Minutes", "five-minute"),
    TEN_MINUTES("Ten Minutes", "ten-minute"),
    FIFTEEN_MINUTES("Fifteen Minutes", "fifteen-minute"),
    THIRTY_MINUTES("Thirty Minutes", "thirty-minute"),
    ONE_HOUR("One Hour", "one-hour"),
    ONE_DAY("One Day", "one-day");

    private final String label;

    private final String code;

    IntradayInterval(final String label, final String code) {
        this.label = label;
        this.code = code;
    }


    //  METHODS

    /**
     * Converts a code to an {@link IntradayInterval}
     *
     * @param label label
     * @return {@link IntradayInterval}
     */
    public static IntradayInterval getByLabel(final String label) {
        return switch (label.trim()) {
            case "one-minute" -> ONE_MINUTE;
            case "five-minute" -> FIVE_MINUTES;
            case "ten-minute" -> TEN_MINUTES;
            case "fifteen-minute" -> FIFTEEN_MINUTES;
            case "thirty-minute" -> THIRTY_MINUTES;
            case "one-hour" -> ONE_HOUR;
            case "one-day" -> ONE_DAY;
            default -> throw new IllegalArgumentException("Unknown label: " + label);
        };
    }
}
