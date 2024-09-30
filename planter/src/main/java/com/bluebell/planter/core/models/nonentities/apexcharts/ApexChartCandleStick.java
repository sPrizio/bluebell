package com.bluebell.planter.core.models.nonentities.apexcharts;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Class representation of an ApexChart candlestick. For further information refer to the
 * Apex Charts documentation here: <a href="https://apexcharts.com/docs/installation/">apexcharts</a>
 *
 * @author Stephen Prizio
 * @version 0.0.6
 */
@Getter
@AllArgsConstructor
public class ApexChartCandleStick {

    /**
     * Timestamp in seconds
     */
    private long x;

    /**
     * Array containing open, high, low & close in that order
     */
    private double[] y;
}
