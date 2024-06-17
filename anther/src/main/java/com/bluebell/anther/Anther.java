package com.bluebell.anther;

import com.bluebell.anther.models.metadata.MetaData;
import com.bluebell.anther.models.parameter.strategy.impl.BloomStrategyParameters;
import com.bluebell.anther.services.metadata.MetaDataService;
import com.bluebell.anther.services.reporting.impl.MetaDataReportingService;
import com.bluebell.anther.services.reporting.impl.StrategyReportingService;
import com.bluebell.anther.simulation.impl.BloomSimulation;
import com.bluebell.anther.strategies.impl.Bloom;
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
 *
 * TODO
 *  * possibly look into nextjs app on this project?
 *  * dynamically calculate previous X days average price movements to set the next day's tp and sl. This will need to be tested against raw data to evaluate viability
 */
public class Anther {

    public static void main(String... args) {

        final ChronoUnit unit = ChronoUnit.MONTHS;
        final RadicleTimeInterval timeInterval = RadicleTimeInterval.FIVE_MINUTE;
        final LocalDate start = LocalDate.of(2020, 1, 1);
        final LocalDate end = LocalDate.of(2021, 1, 1);

        final FirstRateDataParser parser = new FirstRateDataParser();
        final Map<LocalDate, AggregatedMarketPrices> masterCollection = parser.parseMarketPricesByDate(timeInterval);

        final BloomSimulation bloomSimulation = new BloomSimulation();
        final StrategyReportingService<Bloom, BloomStrategyParameters> strategyReportingService = new StrategyReportingService<>(Bloom.class);
        strategyReportingService.generateReportForStrategyResults(unit, bloomSimulation.simulate(masterCollection, unit, start, end));

        final MetaDataService metaDataService = new MetaDataService();
        final MetaDataReportingService metaDataReportingService = new MetaDataReportingService();
        final List<MetaData> metaData = metaDataService.getMetaData(start, end, unit, timeInterval, masterCollection);
        metaDataReportingService.generateMetadataReport(metaData);
    }
}
