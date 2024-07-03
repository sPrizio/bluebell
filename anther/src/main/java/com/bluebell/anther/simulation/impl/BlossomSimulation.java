package com.bluebell.anther.simulation.impl;

import com.bluebell.anther.enums.TradeType;
import com.bluebell.anther.models.parameter.LimitParameter;
import com.bluebell.anther.models.parameter.strategy.impl.BasicStrategyParameters;
import com.bluebell.anther.models.parameter.strategy.impl.BloomStrategyParameters;
import com.bluebell.anther.models.parameter.strategy.impl.BlossomStrategyParameters;
import com.bluebell.anther.models.simulation.SimulationResult;
import com.bluebell.anther.models.strategy.StrategyResult;
import com.bluebell.anther.simulation.Simulation;
import com.bluebell.anther.strategies.impl.Bloom;
import com.bluebell.anther.strategies.impl.Blossom;
import com.bluebell.radicle.indicators.impl.ExponentialMovingAverageIndicator;
import com.bluebell.radicle.models.AggregatedMarketPrices;
import org.apache.commons.collections4.MapUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simulates instances of the {@link Blossom} strategy
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
public class BlossomSimulation implements Simulation<BlossomStrategyParameters> {

    private static final String DESCRIPTION = "%d EMA";


    //  METHODS

    @Override
    public SimulationResult<BlossomStrategyParameters> simulate(Map<LocalDate, AggregatedMarketPrices> marketData, ChronoUnit unit, LocalDate startDate, LocalDate endDate) {

        LocalDate compare = startDate;
        final Map<LocalDate, List<StrategyResult<BlossomStrategyParameters>>> map = new HashMap<>();
        final List<StrategyResult<BlossomStrategyParameters>> entries = new ArrayList<>();

        while (compare.isBefore(endDate)) {
            Blossom blossom = new Blossom(resolveStrategyParameters(getParameters(100), compare));
            entries.add(blossom.executeStrategy(compare, compare.plus(1, unit), marketData));

            map.put(compare, new ArrayList<>(entries));
            compare = compare.plus(1, unit);
            entries.clear();
        }

        return new SimulationResult<>(map);
    }


    //  HELPERS

    /**
     * Correctly obtains {@link BlossomStrategyParameters} for the given date
     *
     * @param map preset values
     * @param localDate date to lookup
     * @return {@link BlossomStrategyParameters}
     */
    private BlossomStrategyParameters resolveStrategyParameters(final Map<LocalDate, BlossomStrategyParameters> map, final LocalDate localDate) {

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
     * Dynamically obtains the parameters for simulating a strategy
     *
     * @return Map of parameters
     */
    private Map<LocalDate, BlossomStrategyParameters> getParameters(final int period) {

        final double lotSize = 0.28;
        final double pricePerPoint = 5.6;
        final boolean scaleProfits = false;
        final double initialBalance = 30000.0;

        final Map<LocalDate, BlossomStrategyParameters> map = new HashMap<>();
        final ExponentialMovingAverageIndicator ema100 = new ExponentialMovingAverageIndicator(period);

        map.put(LocalDate.of(2013, 1, 1), new BlossomStrategyParameters(ema100, new BasicStrategyParameters(String.format(DESCRIPTION, 100), new LimitParameter(TradeType.BUY, 6.1, 3.1), new LimitParameter(TradeType.SELL, 6.83, 3.42), -1, -1, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        map.put(LocalDate.of(2014, 1, 1), new BlossomStrategyParameters(ema100, new BasicStrategyParameters(String.format(DESCRIPTION, 100), new LimitParameter(TradeType.BUY, 8.0, 4.0), new LimitParameter(TradeType.SELL, 9.11, 4.56), -1, -1, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        map.put(LocalDate.of(2015, 1, 1), new BlossomStrategyParameters(ema100, new BasicStrategyParameters(String.format(DESCRIPTION, 100), new LimitParameter(TradeType.BUY, 14.89, 7.45), new LimitParameter(TradeType.SELL, 12.88, 6.44), -1, -1, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        map.put(LocalDate.of(2016, 1, 1), new BlossomStrategyParameters(ema100, new BasicStrategyParameters(String.format(DESCRIPTION, 100), new LimitParameter(TradeType.BUY, 12.29, 6.15), new LimitParameter(TradeType.SELL, 9.79, 4.9), -1, -1, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        map.put(LocalDate.of(2017, 1, 1), new BlossomStrategyParameters(ema100, new BasicStrategyParameters(String.format(DESCRIPTION, 100), new LimitParameter(TradeType.BUY, 9.88, 4.94), new LimitParameter(TradeType.SELL, 8.2, 4.1), -1, -1, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        map.put(LocalDate.of(2018, 1, 1), new BlossomStrategyParameters(ema100, new BasicStrategyParameters(String.format(DESCRIPTION, 100), new LimitParameter(TradeType.BUY, 20.81, 10.41), new LimitParameter(TradeType.SELL, 25.47, 12.74), -1, -1, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        map.put(LocalDate.of(2019, 1, 1), new BlossomStrategyParameters(ema100, new BasicStrategyParameters(String.format(DESCRIPTION, 100), new LimitParameter(TradeType.BUY, 18.37, 9.19), new LimitParameter(TradeType.SELL, 17.12, 8.56), -1, -1, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        map.put(LocalDate.of(2020, 1, 1), new BlossomStrategyParameters(ema100, new BasicStrategyParameters(String.format(DESCRIPTION, 100), new LimitParameter(TradeType.BUY, 49.53, 24.77), new LimitParameter(TradeType.SELL, 55.98, 28.0), -1, -1, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        map.put(LocalDate.of(2021, 1, 1), new BlossomStrategyParameters(ema100, new BasicStrategyParameters(String.format(DESCRIPTION, 100), new LimitParameter(TradeType.BUY, 48.22, 24.11), new LimitParameter(TradeType.SELL, 39.03, 19.52), -1, -1, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        map.put(LocalDate.of(2022, 1, 1), new BlossomStrategyParameters(ema100, new BasicStrategyParameters(String.format(DESCRIPTION, 100), new LimitParameter(TradeType.BUY, 70.35, 35.18), new LimitParameter(TradeType.SELL, 64.13, 32.07), -1, -1, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        map.put(LocalDate.of(2023, 1, 1), new BlossomStrategyParameters(ema100, new BasicStrategyParameters(String.format(DESCRIPTION, 100), new LimitParameter(TradeType.BUY, 41.84, 20.92), new LimitParameter(TradeType.SELL, 48.93, 24.47), -1, -1, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        map.put(LocalDate.of(2024, 1, 1), new BlossomStrategyParameters(ema100, new BasicStrategyParameters(String.format(DESCRIPTION, 100), new LimitParameter(TradeType.BUY, 52.04, 26.02), new LimitParameter(TradeType.SELL, 45.61, 22.81), -1, -1, lotSize, pricePerPoint, scaleProfits, initialBalance)));

        return map;
    }
}
