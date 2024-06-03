package com.bluebell.aurora;

import com.bluebell.aurora.models.metadata.MetaData;
import com.bluebell.aurora.models.strategy.parameter.impl.StaticStrategyParameters;
import com.bluebell.aurora.services.MetaDataService;
import com.bluebell.aurora.strategies.impl.ProjectAuroraStrategy;
import com.bluebell.radicle.enums.TimeInterval;
import com.bluebell.radicle.models.MarketPrice;
import com.bluebell.radicle.parsers.impl.FirstRateDataParser;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Aurora {

    public static void main(String... args) {

        final FirstRateDataParser parser = new FirstRateDataParser();
        final Map<LocalDate, TreeSet<MarketPrice>> masterCollection = parser.parseMarketPricesByDate("NDX_full_5min.txt", TimeInterval.FIVE_MINUTE);

        /*final ProjectAuroraStrategy projectAuroraStrategy = new ProjectAuroraStrategy(60, 30, 9, 50, 0.25, 9.55);*/
        final ProjectAuroraStrategy projectAuroraStrategy = new ProjectAuroraStrategy(new StaticStrategyParameters(10, 5, 9, 50, 0.25, 9.55));

        final LocalDate start = LocalDate.of(2013, 1, 1);
        final LocalDate end = LocalDate.of(2025, 1, 1);

        LocalDate compare = start;
        while (compare.isBefore(end)) {
            System.out.println(projectAuroraStrategy.executeStrategy(compare, compare.plusYears(1), masterCollection));
            compare = compare.plusYears(1);
        }

        // TODO: make the strategy take dynamic data
        // TODO: flag to make take profits as static or dynamic
        // TODO: in order to normalize data the strategy should take in a list of parameters that are tied to the dates?
        // TODO: generate report of losing days and look for patterns
        // TODO: drawdown calculator!

        // TODO: testing aurora
        // TODO: possibly look into nextjs app on this project?


        final MetaDataService metaDataService = new MetaDataService();
        //final List<MetaData> metaData = metaDataService.getMetaData(start, end, ChronoUnit.YEARS, masterCollection);
        //metaData.forEach(System.out::println);
    }
}
