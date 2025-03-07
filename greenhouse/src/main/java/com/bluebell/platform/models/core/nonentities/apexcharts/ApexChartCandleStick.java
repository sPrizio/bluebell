package com.bluebell.platform.models.core.nonentities.apexcharts;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * Class representation of an ApexChart candlestick. For further information refer to the
 * Apex Charts documentation here: <a href="https://apexcharts.com/docs/installation/">apexcharts</a>
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
@Builder
@Schema(title = "ApexChartCandleStick", name = "ApexChartCandleStick", description = "An individual point on a candlestick chart, expected by ApexCharts")
public class ApexChartCandleStick {

    /**
     * Timestamp in seconds
     */
    @Schema(description = "The x-coordinate, typically some measure of time or time period")
    private long x;

    /**
     * Array containing open, high, low & close in that order
     */
    @Schema(description = "The y-coordinate, typically an array of data points in this format: Open, High, Low, Close")
    private double[] y;
}
