package com.bluebell.anther.simulation;

import com.bluebell.anther.models.parameter.strategy.impl.BasicStrategyParameters;
import com.bluebell.anther.models.parameter.strategy.impl.BloomStrategyParameters;
import com.bluebell.anther.models.simulation.SimulationResult;
import com.bluebell.anther.strategies.Strategy;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.parsers.impl.FirstRateDataParser;
import com.bluebell.radicle.parsers.impl.TradingViewDataParser;
import org.apache.commons.collections4.MapUtils;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

/**
 * Class representation of a simulation. A simulation refers to executing a {@link Strategy} over a given period of time
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
public interface Simulation<P extends BasicStrategyParameters> {

    /**
     * Simulates a {@link Strategy} for the given time period, aggregated by the given unit of time
     *
     * @return {@link SimulationResult}
     */
    SimulationResult<P> simulate();


    //  HELPERS

    /**
     * Correctly obtains {@link BloomStrategyParameters} for the given date
     *
     * @param map preset values
     * @param localDate date to lookup
     * @return {@link BloomStrategyParameters}
     */
    default P resolveStrategyParameters(final Map<LocalDate, P> map, final LocalDate localDate) {

        if (MapUtils.isNotEmpty(map)) {
            if (map.containsKey(localDate)) {
                return map.get(localDate);
            } else {
                LocalDate test = localDate.with(TemporalAdjusters.firstDayOfMonth());
                if (map.containsKey(test)) {
                    return map.get(test);
                } else {
                    test = localDate.with(TemporalAdjusters.firstDayOfYear());
                    if (map.containsKey(test)) {
                        return map.get(test);
                    }
                }
            }

            throw new UnsupportedOperationException("No valid parameter configuration was found!");
        }

        throw new UnsupportedOperationException("No parameters found");
    }

    /**
     * Obtains the market data for the simulation
     *
     * @return {@link Map} of {@link AggregatedMarketPrices}
     */
    default Map<LocalDate, AggregatedMarketPrices> getMarketData(final MarketPriceTimeInterval timeInterval, final DataSource dataSource, final String symbol) {

        if (dataSource == DataSource.FIRST_RATE_DATA) {
            return new FirstRateDataParser(false, "NDX").parseMarketPricesByDate(timeInterval);
        } else {
            return new TradingViewDataParser(false, symbol).parseMarketPricesByDate(timeInterval);
        }
    }
}
