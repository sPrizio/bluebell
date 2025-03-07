package com.bluebell.platform.models.core.nonentities.market;


import com.bluebell.platform.enums.time.PlatformTimeInterval;
import lombok.Builder;

import java.util.SortedSet;

/**
 * A convenient wrapper class for a collection of market prices and their aggregated time frame
 *
 * @param marketPrices {@link SortedSet} of {@link MarketPrice}
 * @param interval {@link PlatformTimeInterval}
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Builder
public record AggregatedMarketPrices(
        SortedSet<MarketPrice> marketPrices,
        PlatformTimeInterval interval
) { }
