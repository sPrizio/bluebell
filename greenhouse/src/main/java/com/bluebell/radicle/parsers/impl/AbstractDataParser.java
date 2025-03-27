package com.bluebell.radicle.parsers.impl;


import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import com.bluebell.radicle.enums.DataSource;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Parent-level data parser offering re-usable data
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
public abstract class AbstractDataParser {

    /**
     * Collects aggregated market prices and turns them into a map of prices organized by their dates
     *
     * @param marketPrices {@link AggregatedMarketPrices}
     * @param interval {@link MarketPriceTimeInterval}
     * @param dataSource {@link DataSource}
     * @return {@link Map} of {@link AggregatedMarketPrices}
     */
    Map<LocalDate, AggregatedMarketPrices> generateMasterCollection(final AggregatedMarketPrices marketPrices, final MarketPriceTimeInterval interval, final DataSource dataSource) {
        final Map<LocalDate, AggregatedMarketPrices> masterCollection = new HashMap<>();
        marketPrices.marketPrices().forEach(marketPrice -> {
            final AggregatedMarketPrices mapPrices;
            if (masterCollection.containsKey(marketPrice.getDate().toLocalDate())) {
                mapPrices = masterCollection.get(marketPrice.getDate().toLocalDate());
            } else {
                mapPrices = AggregatedMarketPrices.builder().marketPrices(new TreeSet<>()).interval(interval).dataSource(dataSource).build();
            }

            mapPrices.marketPrices().add(marketPrice);
            masterCollection.put(marketPrice.getDate().toLocalDate(), mapPrices);
        });

        return masterCollection;
    }
}
