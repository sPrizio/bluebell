package com.bluebell.aurora.simulation;

import com.bluebell.aurora.models.parameter.strategy.impl.BasicStrategyParameters;
import com.bluebell.aurora.models.strategy.StrategyResult;
import com.bluebell.aurora.strategies.Strategy;
import com.bluebell.radicle.models.AggregatedMarketPrices;
import com.bluebell.radicle.models.MarketPrice;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Class representation of a simulation. A simulation refers to executing a {@link Strategy} over a given period of time
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public interface Simulation<P extends BasicStrategyParameters> {

    /**
     * Simulates a {@link Strategy} for the given time period, aggregated by the given unit of time
     *
     * @param marketData parsed market data
     * @param unit {@link ChronoUnit}
     * @param startDate start
     * @param endDate end
     * @return map of strategy results organized by their date
     */
    Map<LocalDate, List<StrategyResult<P>>> simulate(final Map<LocalDate, AggregatedMarketPrices> marketData, final ChronoUnit unit, final LocalDate startDate, final LocalDate endDate);
}
