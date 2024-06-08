package com.bluebell.aurora;

import com.bluebell.aurora.enums.TradeType;
import com.bluebell.aurora.models.metadata.MetaData;
import com.bluebell.aurora.models.parameter.LimitParameter;
import com.bluebell.aurora.models.strategy.StrategyResult;
import com.bluebell.aurora.models.parameter.strategy.impl.BasicStrategyParameters;
import com.bluebell.aurora.models.parameter.strategy.impl.BloomStrategyParameters;
import com.bluebell.aurora.services.MetaDataService;
import com.bluebell.aurora.strategies.impl.Bloom;
import com.bluebell.core.services.MathService;
import com.bluebell.radicle.enums.RadicleTimeInterval;
import com.bluebell.radicle.models.MarketPrice;
import com.bluebell.radicle.parsers.impl.FirstRateDataParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Executes the aurora module. Primarily used for testing strategies and obtaining meta-data
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public class Aurora {

    public static void main(String... args) {

        final MathService mathService = new MathService();

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

        //TODO: generate reports into txt files for larger reporting

        //TODO: text file to contain metadata that is used for buy & sell profits

        final FirstRateDataParser parser = new FirstRateDataParser();
        final Map<LocalDate, TreeSet<MarketPrice>> masterCollection = parser.parseMarketPricesByDate("NDX_full_5min.txt", RadicleTimeInterval.FIVE_MINUTE);

        final Bloom bloom1 = new Bloom(new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(new LimitParameter(TradeType.BUY, buyProfit, buyStop), new LimitParameter(TradeType.SELL, sellProfit, sellStop), 9, 30, lotSize, pricePerPoint)));
        final Bloom bloom2 = new Bloom(new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(new LimitParameter(TradeType.BUY, buyProfit, buyStop), new LimitParameter(TradeType.SELL, sellProfit, sellStop), 9, 35, lotSize, pricePerPoint)));
        final Bloom bloom3 = new Bloom(new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(new LimitParameter(TradeType.BUY, buyProfit, buyStop), new LimitParameter(TradeType.SELL, sellProfit, sellStop), 9, 40, lotSize, pricePerPoint)));
        final Bloom bloom4 = new Bloom(new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(new LimitParameter(TradeType.BUY, buyProfit, buyStop), new LimitParameter(TradeType.SELL, sellProfit, sellStop), 9, 45, lotSize, pricePerPoint)));
        final Bloom bloom5 = new Bloom(new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(new LimitParameter(TradeType.BUY, buyProfit, buyStop), new LimitParameter(TradeType.SELL, sellProfit, sellStop), 9, 50, lotSize, pricePerPoint)));
        final Bloom bloom6 = new Bloom(new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(new LimitParameter(TradeType.BUY, buyProfit, buyStop), new LimitParameter(TradeType.SELL, sellProfit, sellStop), 9, 55, lotSize, pricePerPoint)));
        final Bloom bloom7 = new Bloom(new BloomStrategyParameters(variance, normalize, absoluteTarget, new BasicStrategyParameters(new LimitParameter(TradeType.BUY, buyProfit, buyStop), new LimitParameter(TradeType.SELL, sellProfit, sellStop), 10, 0, lotSize, pricePerPoint)));


        final LocalDate start = LocalDate.of(2023, 1, 1);
        final LocalDate end = LocalDate.of(2024, 1, 1);

        LocalDate compare = start;
        final ChronoUnit unit = ChronoUnit.YEARS;

        while (compare.isBefore(end)) {
            System.out.print(getDisplay(bloom1.executeStrategy(compare, compare.plus(1, unit), masterCollection)));
            System.out.print(getDisplay(bloom2.executeStrategy(compare, compare.plus(1, unit), masterCollection)));
            System.out.print(getDisplay(bloom3.executeStrategy(compare, compare.plus(1, unit), masterCollection)));
            System.out.print(getDisplay(bloom4.executeStrategy(compare, compare.plus(1, unit), masterCollection)));
            System.out.print(getDisplay(bloom5.executeStrategy(compare, compare.plus(1, unit), masterCollection)));
            System.out.print(getDisplay(bloom6.executeStrategy(compare, compare.plus(1, unit), masterCollection)));
            System.out.print(getDisplay(bloom7.executeStrategy(compare, compare.plus(1, unit), masterCollection)));
            compare = compare.plus(1, unit);
        }

        // TODO: flag to make take profits as static or dynamic
        // TODO: possibly look into nextjs app on this project?

        //TODO: test making the tp and sl values to be based off of the x number of previous days. Compare with actual metadata to look for most accuracy


       /* final MetaDataService metaDataService = new MetaDataService();
        final List<MetaData> metaData = metaDataService.getMetaData(start, end, unit, masterCollection);
        metaData.forEach(System.out::println);*/
    }


    private static String getDisplay(final StrategyResult result) {
        return """
                From %s to %s: \t %s points for $%s with trading success rate of %s and daily hit rate of %s with a max drawdown of %s points
                """.formatted(result.getStart().format(DateTimeFormatter.ofPattern("yyyy")), result.getEnd().format(DateTimeFormatter.ofPattern("yyyy")), result.getPoints(), result.getNetProfit(), result.getWinPercentage() + "%", result.getDailyWinPercentage() + "%", result.getMaxDrawdown());
    }
}
