package com.bluebell.anther;

import com.bluebell.anther.engine.impl.BloomDecisionEngine;
import com.bluebell.anther.models.metadata.MetaData;
import com.bluebell.anther.models.parameter.strategy.impl.BloomStrategyParameters;
import com.bluebell.anther.models.simulation.SimulationResult;
import com.bluebell.anther.services.metadata.MetaDataService;
import com.bluebell.anther.services.reporting.impl.BloomReportingService;
import com.bluebell.anther.services.reporting.impl.MetaDataReportingService;
import com.bluebell.anther.simulation.impl.BloomSimulation;
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
 * @version 0.0.1
 */
public class Anther {

    private static final boolean RUN_SIMULATION = true;
    private static final boolean GENERATE_REPORTS = true;
    private static final boolean GENERATE_METADATA = false;
    private static final boolean GENERATE_CUMULATIVE_REPORTS = false;
    private static final boolean COMPUTE_DECISIONS = false;


    public static void main(String... args) {

        final ChronoUnit unit = ChronoUnit.YEARS;
        final RadicleTimeInterval timeInterval = RadicleTimeInterval.FIVE_MINUTE;
        final LocalDate start = LocalDate.of(2020, 1, 1);
        final LocalDate end = LocalDate.of(2025, 1, 1);

        final FirstRateDataParser parser = new FirstRateDataParser();
        final Map<LocalDate, AggregatedMarketPrices> masterCollection = parser.parseMarketPricesByDate(timeInterval);

        if (RUN_SIMULATION) {
            final BloomSimulation bloomSimulation = new BloomSimulation();
            final BloomReportingService strategyReportingService = new BloomReportingService();
            final SimulationResult<BloomStrategyParameters> simulationResult = bloomSimulation.simulate(masterCollection, unit, start, end);

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

        if (GENERATE_METADATA) {
            final MetaDataService metaDataService = new MetaDataService();
            final MetaDataReportingService metaDataReportingService = new MetaDataReportingService();
            final List<MetaData> metaData = metaDataService.getMetaData(start, end, unit, timeInterval, masterCollection);
            metaDataReportingService.generateMetadataReport(metaData);
        }
    }
}
