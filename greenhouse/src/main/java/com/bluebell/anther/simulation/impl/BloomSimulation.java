package com.bluebell.anther.simulation.impl;

import com.bluebell.anther.models.parameter.LimitParameter;
import com.bluebell.anther.models.parameter.strategy.impl.BasicStrategyParameters;
import com.bluebell.anther.models.parameter.strategy.impl.BloomStrategyParameters;
import com.bluebell.anther.models.simulation.SimulationResult;
import com.bluebell.anther.models.strategy.StrategyResult;
import com.bluebell.anther.simulation.Simulation;
import com.bluebell.anther.strategies.impl.Bloom;
import com.bluebell.platform.enums.time.PlatformTimeInterval;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import com.bluebell.platform.services.MathService;
import com.bluebell.radicle.enums.DataSource;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link Simulation} specific for the {@link Bloom} strategy
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
public class BloomSimulation implements Simulation<BloomStrategyParameters> {

    private static final String DESCRIPTION = "%s:%s Candle";

    private final MathService mathService = new MathService();

    private final String symbol;

    private final LocalDate start;

    private final LocalDate end;

    private final PlatformTimeInterval timeInterval;

    private final ChronoUnit unit;

    public BloomSimulation(final String symbol, final LocalDate start, final LocalDate end, final PlatformTimeInterval timeInterval, final ChronoUnit unit) {
        this.symbol = symbol;
        this.start = start;
        this.end = end;
        this.timeInterval = timeInterval;
        this.unit = unit;
    }


    //  METHODS

    @Override
    public SimulationResult<BloomStrategyParameters> simulate() {

        LocalDate compare = this.start;
        int startingHour = 9;
        int startingMinute = 30;

        final Map<LocalDate, AggregatedMarketPrices> marketData = getMarketData(this.timeInterval, DataSource.FIRST_RATE_DATA, this.symbol);
        final Map<LocalDate, List<StrategyResult<BloomStrategyParameters>>> map = new HashMap<>();
        final List<StrategyResult<BloomStrategyParameters>> entries = new ArrayList<>();

        double variance = 1.0;
        while (compare.isBefore(this.end)) {
            while (variance <= 1.25) {
                while (startingMinute != 5) {
                    Bloom bloom = new Bloom(resolveStrategyParameters(getParameters(startingHour, startingMinute, variance), compare));
                    entries.add(bloom.executeStrategy(compare, compare.plus(1, this.unit), marketData));

                    startingMinute += 5;
                    if (startingMinute == 60) {
                        startingMinute = 0;
                        startingHour = 10;
                    }
                }

                startingHour = 9;
                startingMinute = 30;
                variance = this.mathService.add(variance, 0.05);
            }

            map.put(compare, new ArrayList<>(entries));
            compare = compare.plus(1, unit);
            entries.clear();
            variance = 1.0;
        }

        return new SimulationResult<>(map);
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

        // normalization before 2020 with a value > 15 does not make sense. Keep this in mind
        final boolean normalize = true;
        final double absoluteTarget = 30.0;
        final double lotSize = 0.28;
        final double pricePerPoint = 5.6;
        final boolean breakEvenStop = false;
        final double initialBalance = 30000.0;

        final Map<LocalDate, BloomStrategyParameters> map = new HashMap<>();

        map.put(LocalDate.of(2013, 1, 1), new BloomStrategyParameters(variance, normalize, breakEvenStop, absoluteTarget, startHour, startMinute, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 6.1, 3.1), new LimitParameter(TradeType.SELL, 6.83, 3.42), lotSize, pricePerPoint, initialBalance)));
        map.put(LocalDate.of(2014, 1, 1), new BloomStrategyParameters(variance, normalize, breakEvenStop, absoluteTarget, startHour, startMinute, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 8.0, 4.0), new LimitParameter(TradeType.SELL, 9.11, 4.56), lotSize, pricePerPoint, initialBalance)));
        map.put(LocalDate.of(2015, 1, 1), new BloomStrategyParameters(variance, normalize, breakEvenStop, absoluteTarget, startHour, startMinute, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 14.89, 7.45), new LimitParameter(TradeType.SELL, 12.88, 6.44), lotSize, pricePerPoint, initialBalance)));
        map.put(LocalDate.of(2016, 1, 1), new BloomStrategyParameters(variance, normalize, breakEvenStop, absoluteTarget, startHour, startMinute, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 12.29, 6.15), new LimitParameter(TradeType.SELL, 9.79, 4.9), lotSize, pricePerPoint, initialBalance)));
        map.put(LocalDate.of(2017, 1, 1), new BloomStrategyParameters(variance, normalize, breakEvenStop, absoluteTarget, startHour, startMinute, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 9.88, 4.94), new LimitParameter(TradeType.SELL, 8.2, 4.1), lotSize, pricePerPoint, initialBalance)));
        map.put(LocalDate.of(2018, 1, 1), new BloomStrategyParameters(variance, normalize, breakEvenStop, absoluteTarget, startHour, startMinute, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 20.81, 10.41), new LimitParameter(TradeType.SELL, 25.47, 12.74), lotSize, pricePerPoint, initialBalance)));
        map.put(LocalDate.of(2019, 1, 1), new BloomStrategyParameters(variance, normalize, breakEvenStop, absoluteTarget, startHour, startMinute, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 18.37, 9.19), new LimitParameter(TradeType.SELL, 17.12, 8.56), lotSize, pricePerPoint, initialBalance)));
        map.put(LocalDate.of(2020, 1, 1), new BloomStrategyParameters(variance, normalize, breakEvenStop, absoluteTarget, startHour, startMinute, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 49.53, 24.77), new LimitParameter(TradeType.SELL, 55.98, 28.0), lotSize, pricePerPoint, initialBalance)));
        map.put(LocalDate.of(2021, 1, 1), new BloomStrategyParameters(variance, normalize, breakEvenStop, absoluteTarget, startHour, startMinute, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 48.22, 24.11), new LimitParameter(TradeType.SELL, 39.03, 19.52), lotSize, pricePerPoint, initialBalance)));
        map.put(LocalDate.of(2022, 1, 1), new BloomStrategyParameters(variance, normalize, breakEvenStop, absoluteTarget, startHour, startMinute, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 70.35, 35.18), new LimitParameter(TradeType.SELL, 64.13, 32.07), lotSize, pricePerPoint, initialBalance)));
        map.put(LocalDate.of(2023, 1, 1), new BloomStrategyParameters(variance, normalize, breakEvenStop, absoluteTarget, startHour, startMinute, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 41.84, 20.92), new LimitParameter(TradeType.SELL, 48.93, 24.47), lotSize, pricePerPoint, initialBalance)));
        map.put(LocalDate.of(2024, 1, 1), new BloomStrategyParameters(variance, normalize, breakEvenStop, absoluteTarget, startHour, startMinute, new BasicStrategyParameters(String.format(DESCRIPTION, startHour, startMinute), new LimitParameter(TradeType.BUY, 52.04, 26.02), new LimitParameter(TradeType.SELL, 45.61, 22.81), lotSize, pricePerPoint, initialBalance)));

        return map;
    }
}
