package com.bluebell.anther.models.parameter.strategy.impl;

import com.bluebell.anther.models.parameter.strategy.StrategyParameters;
import com.bluebell.anther.strategies.impl.Bloom;
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

    private boolean breakEvenStop;

    private double absoluteProfitTarget;

    public BloomStrategyParameters(final double variance, final boolean normalize, final boolean breakEvenStop, final double absoluteProfitTarget, final BasicStrategyParameters basicStrategyParameters) {
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

        this.variance = variance;
        this.normalize = normalize;
        this.breakEvenStop = breakEvenStop;
        this.absoluteProfitTarget = absoluteProfitTarget;
    }
}
