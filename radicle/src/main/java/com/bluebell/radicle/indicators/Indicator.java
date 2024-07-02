package com.bluebell.radicle.indicators;

import com.bluebell.radicle.models.MarketPrice;

import java.util.Collection;

/**
 * An indicator is some kind of metadata/formula/computation that can added to a price chart to provide insight
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
public interface Indicator {

    String getName();

    void computeValue(final Collection<MarketPrice> previousPrices, final MarketPrice currentPrice);
}
