package com.bluebell.platform.models.core.nonentities.market;


import java.util.SortedSet;

import com.bluebell.platform.enums.time.PlatformTimeInterval;

/**
 * A convenient wrapper class for a collection of market prices and their aggregated time frame
 *
 * @param marketPrices {@link SortedSet} of {@link MarketPrice}
 * @param interval {@link PlatformTimeInterval}
 * @author Stephen Prizio
 * @version 0.0.9
 */
public record AggregatedMarketPrices(SortedSet<MarketPrice> marketPrices, PlatformTimeInterval interval) {
}
