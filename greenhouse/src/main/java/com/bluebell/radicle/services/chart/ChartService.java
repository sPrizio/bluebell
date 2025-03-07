package com.bluebell.radicle.services.chart;


import com.bluebell.platform.enums.chart.IntradayInterval;

import java.time.LocalDate;
import java.util.List;

/**
 * Service-layer for charting
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
public interface ChartService<D> {

    /**
     * Obtains the chart data for the given start and end date
     *
     * @param startDate start period
     * @param endDate end period
     * @param timeInterval {@link IntradayInterval}
     * @return {@link List} of {@link D}
     */
    List<D> getChartData(final LocalDate startDate, final LocalDate endDate, final IntradayInterval timeInterval);
}
