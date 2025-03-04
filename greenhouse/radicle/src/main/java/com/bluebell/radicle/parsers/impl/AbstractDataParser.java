package com.bluebell.radicle.parsers.impl;


import com.bluebell.platform.enums.time.PlatformTimeInterval;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Parent-level data parser offering re-usable data
 *
 * @author Stephen Prizio
 * @version 0.02
 */
public abstract class AbstractDataParser {

    /**
     * Collects aggregated market prices and turns them into a map of prices organized by their dates
     *
     * @param marketPrices {@link AggregatedMarketPrices}
     * @param interval {@link PlatformTimeInterval}
     * @return {@link Map} of {@link AggregatedMarketPrices}
     */
    Map<LocalDate, AggregatedMarketPrices> generateMasterCollection(final AggregatedMarketPrices marketPrices, final PlatformTimeInterval interval) {
        final Map<LocalDate, AggregatedMarketPrices> masterCollection = new HashMap<>();
        marketPrices.marketPrices().forEach(marketPrice -> {
            final AggregatedMarketPrices mapPrices;
            if (masterCollection.containsKey(marketPrice.date().toLocalDate())) {
                mapPrices = masterCollection.get(marketPrice.date().toLocalDate());
            } else {
                mapPrices = new AggregatedMarketPrices(new TreeSet<>(), interval);
            }

            mapPrices.marketPrices().add(marketPrice);
            masterCollection.put(marketPrice.date().toLocalDate(), mapPrices);
        });

        return masterCollection;
    }
}
