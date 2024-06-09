package com.bluebell.aurora.models.parameter.strategy.impl;

import com.bluebell.aurora.models.parameter.strategy.StrategyParameters;
import com.bluebell.aurora.strategies.impl.Bloom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * {@link Bloom}-specific strategy parameters
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
@NoArgsConstructor
public class BloomStrategyParameters extends BasicStrategyParameters implements StrategyParameters {

    private double variance;

    private boolean normalize;

    private double absoluteProfitTarget;

    public BloomStrategyParameters(double variance, boolean normalize, double absoluteProfitTarget, final BasicStrategyParameters basicStrategyParameters) {
        super(
                basicStrategyParameters.getDescription(),
                basicStrategyParameters.getBuyLimit(),
                basicStrategyParameters.getSellLimit(),
                basicStrategyParameters.getStartHour(),
                basicStrategyParameters.getStartMinute(),
                basicStrategyParameters.getLotSize(),
                basicStrategyParameters.getPricePerPoint()
        );

        this.variance = variance;
        this.normalize = normalize;
        this.absoluteProfitTarget = absoluteProfitTarget;
    }
}
