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
@AllArgsConstructor
public class BloomStrategyParameters implements StrategyParameters {

    private double variance;

    private boolean normalize;

    private double absoluteProfitTarget;

    private BasicStrategyParameters basicStrategyParameters;
}
