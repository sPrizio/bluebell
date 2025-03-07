package com.bluebell.anther.models.simulation;

import com.bluebell.anther.models.parameter.strategy.impl.BasicStrategyParameters;
import com.bluebell.anther.models.strategy.StrategyResult;
import com.bluebell.anther.simulation.Simulation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Class representation of the result of running a {@link Simulation}. In this case we return a map indexed by dates where the values
 * can be 1 or more {@link StrategyResult}s.
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Builder
public record SimulationResult<P extends BasicStrategyParameters>(
        @Getter Map<LocalDate, List<StrategyResult<P>>> result
) { }
