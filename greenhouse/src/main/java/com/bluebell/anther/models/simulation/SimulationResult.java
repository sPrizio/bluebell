package com.bluebell.anther.models.simulation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.bluebell.anther.models.parameter.strategy.impl.BasicStrategyParameters;
import com.bluebell.anther.models.strategy.StrategyResult;
import com.bluebell.anther.simulation.Simulation;

/**
 * Class representation of the result of running a {@link Simulation}. In this case we return a map indexed by dates where the values
 * can be 1 or more {@link StrategyResult}s.
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public record SimulationResult<P extends BasicStrategyParameters>(Map<LocalDate, List<StrategyResult<P>>> result) {
}
