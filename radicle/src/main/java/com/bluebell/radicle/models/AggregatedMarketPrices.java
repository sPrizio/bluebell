package com.bluebell.radicle.models;

import com.bluebell.radicle.enums.RadicleTimeInterval;

import java.util.TreeSet;

/**
 * A convenient wrapper class for a collection of market prices and their aggregated time frame
 *
 * @param marketPrices {@link TreeSet} of {@link MarketPrice}
 * @param interval {@link RadicleTimeInterval}
 * @author Stephen Prizio
 * @version 0.0.1
 */
public record AggregatedMarketPrices(TreeSet<MarketPrice> marketPrices, RadicleTimeInterval interval) {
}
