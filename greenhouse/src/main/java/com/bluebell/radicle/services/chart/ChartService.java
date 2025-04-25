package com.bluebell.radicle.services.chart;


import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.radicle.enums.DataSource;

import java.time.LocalDate;
import java.util.List;

/**
 * Service-layer for charting
 *
 * @author Stephen Prizio
 * @version 0.1.7
 */
public interface ChartService<D> {

    /**
     * Obtains the chart data for the given start and end date
     *
     * @param startDate start period
     * @param endDate end period
     * @param timeInterval {@link MarketPriceTimeInterval}
     * @param symbol symbol
     * @param dataSource {@link DataSource}
     * @return {@link List} of {@link D}
     */
    List<D> getChartData(final LocalDate startDate, final LocalDate endDate, final MarketPriceTimeInterval timeInterval, final String symbol, final DataSource dataSource);
}
