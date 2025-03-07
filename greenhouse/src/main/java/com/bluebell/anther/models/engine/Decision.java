package com.bluebell.anther.models.engine;

import com.bluebell.anther.engine.DecisionEngine;
import com.bluebell.anther.models.parameter.strategy.impl.BasicStrategyParameters;
import lombok.Builder;
import lombok.Getter;

/**
 * Class representation of a decision from the {@link DecisionEngine}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Builder
public record Decision<P extends BasicStrategyParameters>(
        @Getter P strategyParameters,
        @Getter int index
) {}
