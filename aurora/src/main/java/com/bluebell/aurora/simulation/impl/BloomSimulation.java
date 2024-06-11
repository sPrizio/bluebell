package com.bluebell.aurora.simulation.impl;

import com.bluebell.aurora.enums.TradeType;
import com.bluebell.aurora.models.parameter.LimitParameter;
import com.bluebell.aurora.models.parameter.strategy.impl.BasicStrategyParameters;
import com.bluebell.aurora.models.parameter.strategy.impl.BloomStrategyParameters;
import com.bluebell.aurora.models.strategy.StrategyResult;
import com.bluebell.aurora.simulation.Simulation;
import com.bluebell.aurora.strategies.impl.Bloom;
import com.bluebell.core.services.MathService;
import com.bluebell.radicle.models.MarketPrice;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Implementation of {@link Simulation} specific for the {@link Bloom} strategy
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public class BloomSimulation implements Simulation<BloomStrategyParameters> {

    private static final String DESCRIPTION = "%s:%s Candle";

    private final MathService mathService = new MathService();


    //  METHODS

    @Override
    public Map<LocalDate, List<StrategyResult<BloomStrategyParameters>>> simulate(final Map<LocalDate, TreeSet<MarketPrice>> marketData, final ChronoUnit unit, final LocalDate startDate, final LocalDate endDate) {

        LocalDate compare = startDate;
        int startingHour = 9;
        int startingMinute = 30;

        final Map<LocalDate, List<StrategyResult<BloomStrategyParameters>>> map = new HashMap<>();
        final List<StrategyResult<BloomStrategyParameters>> entries = new ArrayList<>();

        double variance = 1.0;
        while (compare.isBefore(endDate)) {
            while (variance <= 1.25) {
                while (startingMinute != 5) {
                    Bloom bloom = new Bloom(getParameters(startingHour, startingMinute, variance).get(compare));
                    entries.add(bloom.executeStrategy(compare, compare.plus(1, unit), marketData));

                    startingMinute += 5;
                    if (startingMinute == 60) {
                        startingMinute = 0;
                        startingHour = 10;
                    }
                }

                startingHour = 9;
                startingMinute = 30;
                variance = mathService.add(variance, 0.05);
            }

            map.put(compare, new ArrayList<>(entries));
            compare = compare.plus(1, unit);
            entries.clear();
            variance = 1.0;
        }

        return map;
    }


    //  HELPERS

    /**
     * Dynamically obtains the parameters for simulating a strategy
     *
     * @param startHour start hour
     * @param startMinute start minute
     * @param variance variance
     * @return Map of parameters
     */
    private Map<LocalDate, BloomStrategyParameters> getParameters(final int startHour, final int startMinute, final double variance) {

        final boolean normalize = true;
        final double absoluteTarget = 30.0;
        final double lotSize = 0.25;
        final double pricePerPoint = 9.55;

        final Map<LocalDate, BloomStrategyParameters> map = new HashMap<>();

        map.put(LocalDate.of(2013, 1, 1), new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 6.1, 3.1), new LimitParameter(TradeType.SELL, 6.83, 3.42), startHour, startMinute, lotSize, pricePerPoint)));
        map.put(LocalDate.of(2014, 1, 1), new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 8.0, 4.0), new LimitParameter(TradeType.SELL, 9.11, 4.56), startHour, startMinute, lotSize, pricePerPoint)));
        map.put(LocalDate.of(2015, 1, 1), new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 14.89, 7.45), new LimitParameter(TradeType.SELL, 12.88, 6.44), startHour, startMinute, lotSize, pricePerPoint)));
        map.put(LocalDate.of(2016, 1, 1), new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 12.29, 6.15), new LimitParameter(TradeType.SELL, 9.79, 4.9), startHour, startMinute, lotSize, pricePerPoint)));
        map.put(LocalDate.of(2017, 1, 1), new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 9.88, 4.94), new LimitParameter(TradeType.SELL, 8.2, 4.1), startHour, startMinute, lotSize, pricePerPoint)));
        map.put(LocalDate.of(2018, 1, 1), new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 20.81, 10.41), new LimitParameter(TradeType.SELL, 25.47, 12.74), startHour, startMinute, lotSize, pricePerPoint)));
        map.put(LocalDate.of(2019, 1, 1), new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 18.37, 9.19), new LimitParameter(TradeType.SELL, 17.12, 8.56), startHour, startMinute, lotSize, pricePerPoint)));
        map.put(LocalDate.of(2020, 1, 1), new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 49.53, 24.77), new LimitParameter(TradeType.SELL, 55.98, 28.0), startHour, startMinute, lotSize, pricePerPoint)));
        map.put(LocalDate.of(2021, 1, 1), new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 48.22, 24.11), new LimitParameter(TradeType.SELL, 39.03, 19.52), startHour, startMinute, lotSize, pricePerPoint)));
        map.put(LocalDate.of(2022, 1, 1), new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 70.35, 35.18), new LimitParameter(TradeType.SELL, 64.13, 32.07), startHour, startMinute, lotSize, pricePerPoint)));
        map.put(LocalDate.of(2023, 1, 1), new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 41.84, 20.92), new LimitParameter(TradeType.SELL, 48.93, 24.47), startHour, startMinute, lotSize, pricePerPoint)));
        map.put(LocalDate.of(2024, 1, 1), new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 52.04, 26.02), new LimitParameter(TradeType.SELL, 45.61, 22.81), startHour, startMinute, lotSize, pricePerPoint)));

        return map;
    }
}
