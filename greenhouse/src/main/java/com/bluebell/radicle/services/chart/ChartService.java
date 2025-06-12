package com.bluebell.radicle.services.chart;


import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.entities.trade.Trade;

import java.util.List;

/**
 * Service-layer for charting
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
public interface ChartService<D> {

    /**
     * Obtains the chart data for the given start and end date
     *
     * @param trade {@link Trade}
     * @param timeInterval {@link MarketPriceTimeInterval}
     * @return {@link List} of {@link D}
     */
    List<D> getChartDataForTrade(final Trade trade, final MarketPriceTimeInterval timeInterval);
}
