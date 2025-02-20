package com.bluebell.anther.models.engine;

import com.bluebell.anther.engine.DecisionEngine;
import com.bluebell.anther.models.parameter.strategy.impl.BasicStrategyParameters;

/**
 * Class representation of a decision from the {@link DecisionEngine}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public record Decision<P extends BasicStrategyParameters> (P strategyParameters, int index) {}
