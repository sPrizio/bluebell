package com.bluebell.anther.simulation.impl;

import com.bluebell.anther.models.parameter.LimitParameter;
import com.bluebell.anther.models.parameter.strategy.impl.BasicStrategyParameters;
import com.bluebell.anther.models.parameter.strategy.impl.SproutStrategyParameters;
import com.bluebell.anther.models.simulation.SimulationResult;
import com.bluebell.anther.models.strategy.StrategyResult;
import com.bluebell.anther.simulation.Simulation;
import com.bluebell.anther.strategies.impl.Sprout;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.enums.RadicleTimeInterval;
import com.bluebell.radicle.models.AggregatedMarketPrices;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link Simulation} specific for the {@link Sprout} strategy
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
public class SproutSimulation implements Simulation<SproutStrategyParameters> {

    private static final String DESCRIPTION = "Sprout";

    private final String symbol;

    private final LocalDate start;

    private final LocalDate end;

    private final RadicleTimeInterval timeInterval;

    private final ChronoUnit unit;

    public SproutSimulation(final String symbol, final LocalDate start, final LocalDate end, final RadicleTimeInterval timeInterval, final ChronoUnit unit) {
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

        return new SimulationResult<>(map);
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

        //map.put(LocalDate.of(2013, 1, 1), new SproutStrategyParameters(profitMultiplier, allowableRisk, minimumRisk, minimumReward, new BasicStrategyParameters(DESCRIPTION, new LimitParameter(), new LimitParameter(), startHour, startMinute, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        //map.put(LocalDate.of(2014, 1, 1), new SproutStrategyParameters(profitMultiplier, allowableRisk, minimumRisk, minimumReward, new BasicStrategyParameters(DESCRIPTION, new LimitParameter(), new LimitParameter(), startHour, startMinute, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        //map.put(LocalDate.of(2015, 1, 1), new SproutStrategyParameters(profitMultiplier, allowableRisk, minimumRisk, minimumReward, new BasicStrategyParameters(DESCRIPTION, new LimitParameter(), new LimitParameter(), startHour, startMinute, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        //map.put(LocalDate.of(2016, 1, 1), new SproutStrategyParameters(profitMultiplier, allowableRisk, minimumRisk, minimumReward, new BasicStrategyParameters(DESCRIPTION, new LimitParameter(), new LimitParameter(), startHour, startMinute, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        //map.put(LocalDate.of(2017, 1, 1), new SproutStrategyParameters(profitMultiplier, allowableRisk, minimumRisk, minimumReward, new BasicStrategyParameters(DESCRIPTION, new LimitParameter(), new LimitParameter(), startHour, startMinute, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        //map.put(LocalDate.of(2018, 1, 1), new SproutStrategyParameters(profitMultiplier, allowableRisk, minimumRisk, minimumReward, new BasicStrategyParameters(DESCRIPTION, new LimitParameter(), new LimitParameter(), startHour, startMinute, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        //map.put(LocalDate.of(2019, 1, 1), new SproutStrategyParameters(profitMultiplier, allowableRisk, minimumRisk, minimumReward, new BasicStrategyParameters(DESCRIPTION, new LimitParameter(), new LimitParameter(), startHour, startMinute, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        //map.put(LocalDate.of(2020, 1, 1), new SproutStrategyParameters(profitMultiplier, allowableRisk, minimumRisk, minimumReward, new BasicStrategyParameters(DESCRIPTION, new LimitParameter(), new LimitParameter(), startHour, startMinute, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        //map.put(LocalDate.of(2021, 1, 1), new SproutStrategyParameters(profitMultiplier, allowableRisk, minimumRisk, minimumReward, new BasicStrategyParameters(DESCRIPTION, new LimitParameter(), new LimitParameter(), startHour, startMinute, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        //map.put(LocalDate.of(2022, 1, 1), new SproutStrategyParameters(profitMultiplier, allowableRisk, minimumRisk, minimumReward, new BasicStrategyParameters(DESCRIPTION, new LimitParameter(), new LimitParameter(), startHour, startMinute, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        //map.put(LocalDate.of(2023, 1, 1), new SproutStrategyParameters(profitMultiplier, allowableRisk, minimumRisk, minimumReward, new BasicStrategyParameters(DESCRIPTION, new LimitParameter(), new LimitParameter(), startHour, startMinute, lotSize, pricePerPoint, scaleProfits, initialBalance)));
        map.put(LocalDate.of(2024, 1, 1), new SproutStrategyParameters(profitMultiplier, allowableRisk, allowableReward, minimumRisk, minimumReward, 0.05, new BasicStrategyParameters(DESCRIPTION, new LimitParameter(), new LimitParameter(), lotSize, pricePerPoint, initialBalance)));

        return map;
    }
}
