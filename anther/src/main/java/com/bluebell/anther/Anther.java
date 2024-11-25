package com.bluebell.anther;

import com.bluebell.anther.engine.impl.BloomDecisionEngine;
import com.bluebell.anther.models.metadata.MetaData;
import com.bluebell.anther.models.parameter.strategy.impl.BloomStrategyParameters;
import com.bluebell.anther.models.parameter.strategy.impl.SproutStrategyParameters;
import com.bluebell.anther.models.simulation.SimulationResult;
import com.bluebell.anther.services.metadata.MetaDataService;
import com.bluebell.anther.services.reporting.impl.BloomReportingService;
import com.bluebell.anther.services.reporting.impl.MetaDataReportingService;
import com.bluebell.anther.services.reporting.impl.SproutReportingService;
import com.bluebell.anther.simulation.impl.BloomSimulation;
import com.bluebell.anther.simulation.impl.SproutSimulation;
import com.bluebell.radicle.enums.RadicleTimeInterval;
import com.bluebell.radicle.models.AggregatedMarketPrices;
import com.bluebell.radicle.parsers.impl.FirstRateDataParser;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * Executes the anther module. Primarily used for testing strategies and obtaining meta-data
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
public class Anther {

    private static final boolean RUN_BLOOM = false;
    private static final boolean RUN_SPROUT = true;
    private static final boolean RUN_SIMULATION = true;
    private static final boolean GENERATE_REPORTS = true;
    private static final boolean GENERATE_METADATA = false;
    private static final boolean GENERATE_CUMULATIVE_REPORTS = true;
    private static final boolean COMPUTE_DECISIONS = false;

    private static final ChronoUnit UNIT = ChronoUnit.DAYS;
    private static final RadicleTimeInterval TIME_INTERVAL = RadicleTimeInterval.THIRTY_MINUTE;
    private static final LocalDate START = LocalDate.of(2024, 1, 26);
    private static final LocalDate END = LocalDate.of(2024, 2, 1);

    /**
     * Main function
     *
     * @param args program arguments
     */
    public static void main(String... args) {
        if (RUN_BLOOM) {
            runBloom(START, END, TIME_INTERVAL, UNIT);
        }

        if (RUN_SPROUT) {
            runSprout(START, END, TIME_INTERVAL, UNIT);
        }

        if (GENERATE_METADATA) {
            runMetadata(START, END, TIME_INTERVAL, UNIT);
        }
    }


    //  HELPERS

    /**
     * Runs the bloom strategy flow
     *
     * @param start start period
     * @param end end period
     * @param timeInterval {@link RadicleTimeInterval}
     * @param unit {@link ChronoUnit}
     */
    private static void runBloom(final LocalDate start, final LocalDate end, final RadicleTimeInterval timeInterval, final ChronoUnit unit) {

        if (RUN_SIMULATION) {
            final BloomSimulation bloomSimulation = new BloomSimulation("US100", start, end, timeInterval, unit);
            final BloomReportingService strategyReportingService = new BloomReportingService();
            final SimulationResult<BloomStrategyParameters> simulationResult = bloomSimulation.simulate();

            if (GENERATE_REPORTS) {
                strategyReportingService.generateReportForSimulationResult(unit, simulationResult);
            }

            if (GENERATE_CUMULATIVE_REPORTS) {
                strategyReportingService.generateCumulativeReport(simulationResult);
            }

            if (COMPUTE_DECISIONS) {
                final BloomDecisionEngine bloomDecisionEngine = new BloomDecisionEngine();
                bloomDecisionEngine.decide(simulationResult);
            }
        }
    }

    /**
     * Runs the sprout strategy flow
     *
     * @param start start period
     * @param end end period
     * @param timeInterval {@link RadicleTimeInterval}
     * @param unit {@link ChronoUnit}
     */
    private static void runSprout(final LocalDate start, final LocalDate end, final RadicleTimeInterval timeInterval, final ChronoUnit unit) {

        if (RUN_SIMULATION) {
            final SproutSimulation sproutSimulation = new SproutSimulation("US100", start, end, timeInterval, unit);
            final SproutReportingService strategyReportingService = new SproutReportingService();
            final SimulationResult<SproutStrategyParameters> simulationResult = sproutSimulation.simulate();

            if (GENERATE_REPORTS) {
                strategyReportingService.generateReportForSimulationResult(unit, simulationResult);
            }

            if (GENERATE_CUMULATIVE_REPORTS) {
                strategyReportingService.generateCumulativeReport(simulationResult);
            }
        }
    }

    /**
     * Generates metadata
     *
     * @param start start period
     * @param end end period
     * @param timeInterval {@link RadicleTimeInterval}
     * @param unit {@link ChronoUnit}
     */
    private static void runMetadata(final LocalDate start, final LocalDate end, final RadicleTimeInterval timeInterval, final ChronoUnit unit) {
        final MetaDataService metaDataService = new MetaDataService();
        final FirstRateDataParser parser = new FirstRateDataParser();

        final Map<LocalDate, AggregatedMarketPrices> masterCollection = parser.parseMarketPricesByDate(timeInterval);
        final MetaDataReportingService metaDataReportingService = new MetaDataReportingService();
        final List<MetaData> metaData = metaDataService.getMetaData(start, end, unit, timeInterval, masterCollection);

        metaDataReportingService.generateMetadataReport(metaData);
    }
}
