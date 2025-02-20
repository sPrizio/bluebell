package com.bluebell.anther.engine;

import com.bluebell.anther.models.engine.Decision;
import com.bluebell.anther.models.parameter.strategy.impl.BasicStrategyParameters;
import com.bluebell.anther.models.simulation.SimulationResult;
import com.bluebell.anther.strategies.Strategy;

import java.util.List;

/**
 * A decision engine is a process by which a {@link Strategy} is analyzed to determine its optimal parameters
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public interface DecisionEngine<S extends Strategy<P>, P extends BasicStrategyParameters> {

    /**
     * Determines the best decision from the consideration list
     *
     * @param simulationResult {@link SimulationResult}
     * @return {@link Decision}
     */
    Decision<P> decide(final SimulationResult<P> simulationResult);

    /**
     * Generates a list of decisions to be evaluated
     *
     * @param simulationResult {@link SimulationResult}
     * @return {@link List} of {@link Decision}
     */
    List<Decision<P>> consider(final SimulationResult<P> simulationResult);
}
