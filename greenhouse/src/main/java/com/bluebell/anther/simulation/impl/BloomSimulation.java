package com.bluebell.anther.simulation.impl;

import com.bluebell.anther.models.parameter.strategy.impl.BloomStrategyParameters;
import com.bluebell.anther.models.simulation.SimulationResult;
import com.bluebell.anther.models.strategy.StrategyResult;
import com.bluebell.anther.simulation.Simulation;
import com.bluebell.anther.strategies.impl.Bloom;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
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
 * @version 0.1.4
 */
public class BloomSimulation implements Simulation<BloomStrategyParameters> {

    private static final String DESCRIPTION = "%s:%s Candle";

    private final MathService mathService = new MathService();

    private final String symbol;

    private final LocalDate start;

    private final LocalDate end;

    private final MarketPriceTimeInterval timeInterval;

    private final ChronoUnit unit;

    public BloomSimulation(final String symbol, final LocalDate start, final LocalDate end, final MarketPriceTimeInterval timeInterval, final ChronoUnit unit) {
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

        return SimulationResult
                .<BloomStrategyParameters>builder()
                .result(map)
                .build();
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


        return map;
    }
}
