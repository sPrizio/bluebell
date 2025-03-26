package com.bluebell.anther.models.metadata;

import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.services.MathService;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.javatuples.Pair;
import org.javatuples.Quintet;
import org.javatuples.Triplet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.SortedSet;

/**
 * Class representation of metadata about trading prices
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
@Getter
public class MetaData {

    private final MathService mathService = new MathService();

    private final LocalDate start;

    private final LocalDate end;

    private final ChronoUnit unit;

    private final MarketPriceTimeInterval timeInterval;

    private final int count;

    private final int bullDays;

    private final double averageBullGain;

    private final int bearDays;

    private final double averageBearGain;

    private final int dojiDays;

    private final double absoluteHigh;

    private final double absoluteLow;

    private final double averageChange;

    private final double averageBullWickSize;

    private final double averageBearWickSize;

    private final int bullProbability;

    private final int bearProbability;

    private final int dojiProbability;


    //  CONSTRUCTORS

    public MetaData(final LocalDate start, final LocalDate end, final ChronoUnit unit, final MarketPriceTimeInterval timeInterval, final SortedSet<MarketPrice> prices) {

        this.start = start;
        this.end = end;
        this.count = prices.size();
        this.unit = unit;
        this.timeInterval = timeInterval;

        final Triplet<Double, Double, Double> values = calculate(prices);
        this.absoluteHigh = values.getValue0();
        this.absoluteLow = values.getValue1();
        this.averageChange = values.getValue2();

        final Pair<Double, Double> wicks = calculateWicks(prices);
        this.averageBullWickSize = wicks.getValue0();
        this.averageBearWickSize = wicks.getValue1();

        final Quintet<Integer, Double, Integer, Double, Integer> directions = calculateDirectionalInformation(prices);
        this.bullDays = directions.getValue0();
        this.averageBullGain = directions.getValue1();
        this.bearDays = directions.getValue2();
        this.averageBearGain = directions.getValue3();
        this.dojiDays = directions.getValue4();

        this.bullProbability = this.mathService.wholePercentage(this.bullDays, prices.size());
        this.bearProbability = this.mathService.wholePercentage(this.bearDays, prices.size());
        this.dojiProbability = this.mathService.wholePercentage(this.dojiDays, prices.size());
    }


    //  METHODS

    /**
     * Calculates the highs and lows and average changes
     *
     * @param prices market prices
     * @return {@link Triplet}
     */
    public Triplet<Double, Double, Double> calculate(final SortedSet<MarketPrice> prices) {

        if (CollectionUtils.isEmpty(prices)) {
            return Triplet.with(0.0, 0.0, 0.0);
        }

        double absoluteTop = 0.0;
        double absoluteBottom = Integer.MAX_VALUE;
        final double average = this.mathService.getDouble(prices.stream().mapToDouble(pr -> Math.abs(this.mathService.subtract(pr.getHigh(), pr.getLow()))).average().orElse(0.0));

        for (MarketPrice price : prices) {
            if (price.getHigh() > absoluteTop) {
                absoluteTop = price.getHigh();
            }

            if (price.getLow() < absoluteBottom) {
                absoluteBottom = price.getLow();
            }
        }

        return Triplet.with(absoluteTop, prices.isEmpty() ? 0.0 : absoluteBottom, average);
    }

    /**
     * Calculates the average bull and bear wick sizes (distance of the high/low from the open
     *
     * @param prices market prices
     * @return {@link Pair}
     */
    public Pair<Double, Double> calculateWicks(final SortedSet<MarketPrice> prices) {

        if (CollectionUtils.isEmpty(prices)) {
            return Pair.with(0.0, 0.0);
        }

        final List<MarketPrice> bullPrices = prices.stream().filter(MarketPrice::isBullish).filter(MarketPrice::hasFullBody).toList();
        final List<MarketPrice> bearPrices = prices.stream().filter(MarketPrice::isBearish).filter(MarketPrice::hasFullBody).toList();

        final double bullWick = bullPrices.isEmpty() ? 0.0 : this.mathService.getDouble(bullPrices.stream().mapToDouble(pr -> this.mathService.subtract(pr.getOpen(), pr.getLow())).average().orElse(0.0));
        final double bearWick = bearPrices.isEmpty() ? 0.0 : this.mathService.getDouble(bearPrices.stream().mapToDouble(pr -> this.mathService.subtract(pr.getHigh(), pr.getOpen())).average().orElse(0.0));

        return Pair.with(bullWick, bearWick);
    }

    /**
     * Calculates directional information related to the bars
     *
     * @param prices market prices
     * @return {@link Quintet}
     */
    public Quintet<Integer, Double, Integer, Double, Integer> calculateDirectionalInformation(final SortedSet<MarketPrice> prices) {

        if (CollectionUtils.isEmpty(prices)) {
            return Quintet.with(0, 0.0, 0, 0.0, 0);
        }

        final List<MarketPrice> bullPrices = prices.stream().filter(MarketPrice::isBullish).filter(MarketPrice::hasFullBody).toList();
        final List<MarketPrice> bearPrices = prices.stream().filter(MarketPrice::isBearish).filter(MarketPrice::hasFullBody).toList();
        final List<MarketPrice> dojiPrices = prices.stream().filter(MarketPrice::isDoji).toList();

        final double bullGain = bullPrices.isEmpty() ? 0.0 : this.mathService.getDouble(bullPrices.stream().mapToDouble(pr -> this.mathService.subtract(pr.getClose(), pr.getOpen())).average().orElse(0.0));
        final double bearGain = bearPrices.isEmpty() ? 0.0 : this.mathService.getDouble(bearPrices.stream().mapToDouble(pr -> this.mathService.subtract(pr.getOpen(), pr.getClose())).average().orElse(0.0));

        return Quintet.with(bullPrices.size(), bullGain, bearPrices.size(), bearGain, dojiPrices.size());
    }


    @Override
    public String toString() {
        return """
                MetaData {
                \tstart = %s
                \tend = %s
                \tcount = %s
                \tmarketPriceInterval = %s
                \taggregatedInterval = %s
                \tabsoluteHigh = %s
                \tabsoluteLow = %s
                \tAbsolute Change = %s
                \taverageChangePerBar = %s
                \taverageBullWickSize = %s
                \taverageBearWickSize = %s
                \tbullishDays = %s
                \taverageBullishGain = %s
                \tbearishDays = %s
                \taverageBearishGain = %s
                \tindecisiveDays = %s
                \tbullProbability = %s
                \tbearProbability = %s
                \tdojiProbability = %s
                }'
                """
                .formatted(
                        this.start.format(DateTimeFormatter.ISO_DATE),
                        this.end.format(DateTimeFormatter.ISO_DATE),
                        this.count,
                        this.timeInterval,
                        this.unit,
                        this.absoluteHigh,
                        this.absoluteLow,
                        this.mathService.subtract(this.absoluteHigh, this.absoluteLow),
                        this.averageChange,
                        this.averageBullWickSize,
                        this.averageBearWickSize,
                        this.bullDays,
                        this.averageBullGain,
                        this.bearDays,
                        this.averageBearGain,
                        this.dojiDays,
                        this.bullProbability + "%",
                        this.bearProbability + "%",
                        this.dojiProbability + "%"
                );
    }
}
