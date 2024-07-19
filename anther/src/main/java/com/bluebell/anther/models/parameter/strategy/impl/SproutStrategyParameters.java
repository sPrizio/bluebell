package com.bluebell.anther.models.parameter.strategy.impl;

import com.bluebell.anther.models.parameter.strategy.StrategyParameters;
import com.bluebell.anther.strategies.impl.Sprout;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * {@link Sprout}-specific strategy parameters
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
@NoArgsConstructor
public class SproutStrategyParameters extends BasicStrategyParameters implements StrategyParameters {

    private double profitMultiplier;

    public SproutStrategyParameters(final double profitMultiplier, final BasicStrategyParameters basicStrategyParameters) {
        super(
                basicStrategyParameters.getDescription(),
                basicStrategyParameters.getBuyLimit(),
                basicStrategyParameters.getSellLimit(),
                basicStrategyParameters.getStartHour(),
                basicStrategyParameters.getStartMinute(),
                basicStrategyParameters.getLotSize(),
                basicStrategyParameters.getPricePerPoint(),
                basicStrategyParameters.isScaleProfits(),
                basicStrategyParameters.getInitialBalance()
        );

        this.profitMultiplier = profitMultiplier;
    }
}
