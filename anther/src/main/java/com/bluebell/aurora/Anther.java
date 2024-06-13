package com.bluebell.aurora;

import com.bluebell.aurora.models.parameter.strategy.impl.BloomStrategyParameters;
import com.bluebell.aurora.services.reporting.ReportingService;
import com.bluebell.aurora.simulation.impl.BloomSimulation;
import com.bluebell.aurora.strategies.impl.Bloom;
import com.bluebell.radicle.enums.RadicleTimeInterval;
import com.bluebell.radicle.models.AggregatedMarketPrices;
import com.bluebell.radicle.parsers.impl.FirstRateDataParser;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * Executes the aurora module. Primarily used for testing strategies and obtaining meta-data
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

        final FirstRateDataParser parser = new FirstRateDataParser();
        final Map<LocalDate, AggregatedMarketPrices> masterCollection = parser.parseMarketPricesByDate(RadicleTimeInterval.FIVE_MINUTE);

        final ChronoUnit unit = ChronoUnit.YEARS;
        final BloomSimulation bloomSimulation = new BloomSimulation();
        final ReportingService<Bloom, BloomStrategyParameters> reportingService = new ReportingService<>(Bloom.class);
        reportingService.generateReportForStrategyResults(unit, bloomSimulation.simulate(masterCollection, unit, LocalDate.of(2021, 1, 1), LocalDate.of(2025, 1, 1)));

       /* final MetaDataService metaDataService = new MetaDataService();
        final List<MetaData> metaData = metaDataService.getMetaData(start, end, unit, masterCollection);
        metaData.forEach(System.out::println);*/
    }
}
