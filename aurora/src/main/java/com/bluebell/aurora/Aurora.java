package com.bluebell.aurora;

import com.bluebell.aurora.enums.TradeType;
import com.bluebell.aurora.models.parameter.LimitParameter;
import com.bluebell.aurora.models.strategy.StrategyResult;
import com.bluebell.aurora.models.parameter.strategy.impl.BasicStrategyParameters;
import com.bluebell.aurora.models.parameter.strategy.impl.BloomStrategyParameters;
import com.bluebell.aurora.services.reporting.ReportingService;
import com.bluebell.aurora.strategies.impl.Bloom;
import com.bluebell.core.services.MathService;
import com.bluebell.radicle.enums.RadicleTimeInterval;
import com.bluebell.radicle.models.MarketPrice;
import com.bluebell.radicle.parsers.impl.FirstRateDataParser;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Executes the aurora module. Primarily used for testing strategies and obtaining meta-data
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public class Aurora {

    public static void main(String... args) {

        final MathService mathService = new MathService();
        final ReportingService<Bloom, BloomStrategyParameters> reportingService = new ReportingService<>();

        // config
        final double variance = 1.15;
        final boolean normalize = true;
        final double absoluteTarget = 30.0;
        final double buyProfit = 41.84;
        final double sellProfit = 48.93;
        final double buyStop = mathService.divide(buyProfit, 2.0);
        final double sellStop = mathService.divide(sellProfit, 2.0);
        final double lotSize = 0.25;
        final double pricePerPoint = 9.55;

        //TODO: auto place breakeven stop should be implemented. Definitely a way to minimize risk!

        //TODO: text file to contain metadata that is used for buy & sell profits

        final FirstRateDataParser parser = new FirstRateDataParser();
        final Map<LocalDate, TreeSet<MarketPrice>> masterCollection = parser.parseMarketPricesByDate("NDX_full_5min.txt", RadicleTimeInterval.FIVE_MINUTE);

        final LocalDate start = LocalDate.of(2023, 1, 1);
        final LocalDate end = LocalDate.of(2024, 1, 1);

        LocalDate compare = start;
        final ChronoUnit unit = ChronoUnit.YEARS;
        int startingHour = 9;
        int startingMinute = 30;

        final Map<LocalDate, List<StrategyResult<BloomStrategyParameters>>> map = new HashMap<>();
        final List<StrategyResult<BloomStrategyParameters>> entries = new ArrayList<>();

        while (compare.isBefore(end)) {
            while (startingMinute != 5) {
                Bloom bloom = new Bloom(new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(String.format("%s:%s Candle", startingHour, startingMinute), new LimitParameter(TradeType.BUY, buyProfit, buyStop), new LimitParameter(TradeType.SELL, sellProfit, sellStop), startingHour, startingMinute, lotSize, pricePerPoint)));
                entries.add(bloom.executeStrategy(compare, compare.plus(1, unit), masterCollection));

                startingMinute += 5;
                if (startingMinute == 60) {
                    startingMinute = 0;
                    startingHour = 10;
                }
            }

            map.put(compare, new ArrayList<>(entries));
            compare = compare.plus(1, unit);
            entries.clear();
        }

        reportingService.generateReportForStrategyResults(unit, map);

        // TODO: flag to make take profits as static or dynamic
        // TODO: possibly look into nextjs app on this project?

        //TODO: test making the tp and sl values to be based off of the x number of previous days. Compare with actual metadata to look for most accuracy


       /* final MetaDataService metaDataService = new MetaDataService();
        final List<MetaData> metaData = metaDataService.getMetaData(start, end, unit, masterCollection);
        metaData.forEach(System.out::println);*/
    }
}
