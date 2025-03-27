package com.bluebell.anther.simulation.impl;

import com.bluebell.anther.models.parameter.LimitParameter;
import com.bluebell.anther.models.parameter.strategy.impl.SproutStrategyParameters;
import com.bluebell.anther.models.simulation.SimulationResult;
import com.bluebell.anther.models.strategy.StrategyResult;
import com.bluebell.anther.simulation.Simulation;
import com.bluebell.anther.strategies.impl.Sprout;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import com.bluebell.radicle.enums.DataSource;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link Simulation} specific for the {@link Sprout} strategy
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
public class SproutSimulation implements Simulation<SproutStrategyParameters> {

    private static final String DESCRIPTION = "Sprout";

    private final String symbol;

    private final LocalDate start;

    private final LocalDate end;

    private final MarketPriceTimeInterval timeInterval;

    private final ChronoUnit unit;

    public SproutSimulation(final String symbol, final LocalDate start, final LocalDate end, final MarketPriceTimeInterval timeInterval, final ChronoUnit unit) {
        this.symbol = symbol;
        this.start = start;
        this.end = end;
        this.timeInterval = timeInterval;
        this.unit = unit;
    }


    //  METHODS

    @Override
    public SimulationResult<SproutStrategyParameters> simulate() {

        LocalDate compare = this.start;

        final Map<LocalDate, AggregatedMarketPrices> marketData = getMarketData(this.timeInterval, DataSource.TRADING_VIEW, this.symbol);
        final Map<LocalDate, List<StrategyResult<SproutStrategyParameters>>> map = new HashMap<>();

        while (compare.isBefore(this.end)) {
            Sprout sprout = new Sprout(resolveStrategyParameters(getParameters(), compare));
            map.put(compare, List.of(sprout.executeStrategy(compare, compare.plus(1, this.unit), marketData)));
            compare = compare.plus(1, unit);
        }

        return SimulationResult
                .<SproutStrategyParameters>builder()
                .result(map)
                .build();
    }


    //  HELPERS

    /**
     * Dynamically obtains the parameters for simulating a strategy
     *
     * @return Map of parameters
     */
    private Map<LocalDate, SproutStrategyParameters> getParameters() {

        final double lotSize = 0.25;
        final double pricePerPoint = 5.6;
        final double initialBalance = 30000.0;
        final double profitMultiplier = 2.0;
        final double allowableRisk = 60.0;
        final double allowableReward = 85.0;
        final double minimumRisk = 25.0;
        final double minimumReward = 40.0;

        final Map<LocalDate, SproutStrategyParameters> map = new HashMap<>();

        map.put(
                LocalDate.of(2024, 1, 1),
                SproutStrategyParameters
                        .builder()
                        .profitMultiplier(profitMultiplier)
                        .allowableRisk(allowableRisk)
                        .allowableReward(allowableReward)
                        .minimumRisk(minimumRisk)
                        .minimumReward(minimumReward)
                        .variance(0.05)
                        .description(DESCRIPTION)
                        .buyLimit(LimitParameter.builder().build())
                        .sellLimit(LimitParameter.builder().build())
                        .lotSize(lotSize)
                        .pricePerPoint(pricePerPoint)
                        .initialBalance(initialBalance)
                        .build()
        );

        return map;
    }
}
