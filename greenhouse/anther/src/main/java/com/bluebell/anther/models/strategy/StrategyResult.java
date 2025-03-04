package com.bluebell.anther.models.strategy;

import com.bluebell.anther.models.parameter.LimitParameter;
import com.bluebell.anther.models.parameter.strategy.impl.BasicStrategyParameters;
import com.bluebell.anther.models.trade.AntherTrade;
import com.bluebell.anther.strategies.Strategy;
import com.bluebell.platform.services.MathService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Representation of the result of executing a {@link Strategy}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
public class StrategyResult<P extends BasicStrategyParameters> {

    private final MathService mathService = new MathService();

    private final P strategyParameters;

    private final LocalDate start;

    private final LocalDate end;

    private final List<AntherTrade> trades;

    private final int hits;

    private final int misses;

    private final int winPercentage;

    private final double netProfit;

    private final double pointsGained;

    private final double pointsLost;

    private final double points;

    private final double profitability;

    private final int retention;

    private final LimitParameter buyLimit;

    private final LimitParameter sellLimit;

    private final double pricePerPoint;

    private final long averageTradeDuration;

    private final Double maxDrawdown;

    private final Double relativeDrawdown;

    private final double initialBalance;

    private final List<CumulativeStrategyReportEntry> cumulativeReportEntries;


    //  CONSTRUCTORS

    public StrategyResult(final P strategyParameters, final LocalDate start, final LocalDate end, final Collection<AntherTrade> trades, final LimitParameter buyLimit, final LimitParameter sellLimit, final double pricePerPoint, final double initialBalance) {

        this.strategyParameters = strategyParameters;
        this.start = start;
        this.end = end;
        this.trades = trades.stream().sorted(Comparator.comparing(AntherTrade::getTradeCloseTime)).toList();

        final List<AntherTrade> wins = getWins();
        final List<AntherTrade> losses = getLosses();

        this.hits = wins.size();
        this.misses = losses.size();
        this.winPercentage = this.mathService.wholePercentage(this.hits, this.mathService.add(this.hits, this.misses));
        this.pointsGained = this.mathService.getDouble(wins.stream().mapToDouble(AntherTrade::getPoints).sum());
        this.pointsLost = Math.abs(this.mathService.getDouble(losses.stream().mapToDouble(AntherTrade::getPoints).sum()));
        this.points = this.mathService.getDouble(this.mathService.subtract(this.pointsGained, this.pointsLost));
        this.profitability = this.mathService.divide(this.pointsGained, this.pointsLost);
        this.retention = this.mathService.wholePercentage(this.pointsGained, this.mathService.add(this.pointsGained, this.pointsLost));
        this.buyLimit = buyLimit;
        this.sellLimit = sellLimit;
        this.pricePerPoint = pricePerPoint;
        this.averageTradeDuration = calculateAverageTradeDuration(trades);
        this.maxDrawdown = null;
        this.relativeDrawdown = null;
        this.initialBalance = initialBalance;
        this.netProfit = calculateNetProfit();
        this.cumulativeReportEntries = new ArrayList<>();
    }


    //  METHODS

    /**
     * Returns a list of trades that were winners
     *
     * @return {@link List} of {@link AntherTrade}
     */
    public List<AntherTrade> getWins() {
        return this.trades.stream().filter(tr -> tr.getPoints() > 0.0).toList();
    }

    /**
     * Returns a list of trades that were losers
     *
     * @return {@link List} of {@link AntherTrade}
     */
    public List<AntherTrade> getLosses() {
        return this.trades.stream().filter(tr -> tr.getPoints() < 0.0).toList();
    }

    /**
     * Returns the number of days when more points were gained than lost of the total trading days
     *
     * @return whole percentage
     */
    public int getDailyWinPercentage() {

        final Map<LocalDate, List<AntherTrade>> map = new HashMap<>();
        for (AntherTrade tr : this.trades) {
            final List<AntherTrade> found;
            if (map.containsKey(tr.getTradeOpenTime().toLocalDate())) {
                found = new ArrayList<>(map.get(tr.getTradeOpenTime().toLocalDate()));
            } else {
                found = new ArrayList<>();
            }

            found.add(tr);
            map.put(tr.getTradeOpenTime().toLocalDate(), found);
        }

        return this.mathService.wholePercentage(map.entrySet().stream().filter(entry -> isPositive(entry.getValue())).count(), map.size());
    }

    /**
     * Returns the lowest point of this strategy
     *
     * @return min value
     */
    public Double getMaxDrawdown() {
        return this.mathService.getDouble(Objects.requireNonNullElseGet(this.maxDrawdown, () -> this.calculateDrawdown().stream().mapToDouble(DrawdownEntry::getCumulativePoints).min().orElse(0.0)));
    }

    /**
     * Returns the lowest point throughout the strategy
     *
     * @return min value
     */
    public Double getRelativeDrawdown() {

        if (this.relativeDrawdown == null) {
            final List<DrawdownEntry> values = this.calculateDrawdown();
            double sum = 0.0;
            double min = 0.0;

            for (final DrawdownEntry val : values) {
                if (val.getPoints() > 0.0) {
                    sum = 0.0;
                } else {
                    sum += val.getPoints();
                }

                if (sum < min) {
                    min = sum;
                }
            }

            return this.mathService.getDouble(min);
        }

        return this.mathService.getDouble(this.relativeDrawdown);
    }

    /**
     * Computes a bunny trail per trade
     *
     * @return {@link List} of {@link CumulativeStrategyReportEntry}
     */
    public List<CumulativeStrategyReportEntry> getCumulativeReportEntries() {

        final List<CumulativeStrategyReportEntry> entries = new ArrayList<>();

        int cumTrades = 0;
        double cumPoints = 0.0;
        double cumProfit = 0.0;

        for (final AntherTrade trade : this.trades) {
            cumTrades += 1;
            cumPoints = this.mathService.add(cumPoints, trade.getPoints());
            cumProfit = this.mathService.add(cumProfit, trade.calculateProfit(this.pricePerPoint));

            entries.add(new CumulativeStrategyReportEntry(cumPoints, cumProfit, cumTrades, StringUtils.capitalize(trade.getTradeType().toString().toLowerCase()), trade.getTradeOpenTime(), trade.getTradeCloseTime(), trade.getPoints(), trade.calculateProfit(this.pricePerPoint)));
        }

        return entries;
    }

    @Override
    public String toString() {
        return """
                StrategyResult for period %s to %s { +
                \thits = %s
                \tmisses = %s
                \twinPercentage = %s
                \tdailyWinPercentage = %s
                \tnetProfit = %s
                \tpointsGained = %s
                \tpointsLost = %s
                \tpoints = %s
                \tprofitability = %s
                \tretention = %s
                \taverageTradeDuration = %s minutes
                \tbuyLimit = %s
                \tsellLimit = %s
                \tpricePerPoint = %s
                \tmaxDrawdown = %s
                }
                """
                .formatted(
                        this.start.format(DateTimeFormatter.ISO_DATE),
                        this.end.format(DateTimeFormatter.ISO_DATE),
                        this.hits,
                        this.misses,
                        this.winPercentage + "%",
                        this.getDailyWinPercentage() + "%",
                        this.netProfit,
                        this.pointsGained,
                        this.pointsLost,
                        this.points,
                        this.profitability,
                        this.retention + "%",
                        this.averageTradeDuration,
                        this.buyLimit.toString(),
                        this.sellLimit.toString(),
                        this.pricePerPoint,
                        this.maxDrawdown
                );
    }


    //  HELPERS

    /**
     * Calculates the net profit.
     *
     * @return net profit
     */
    private double calculateNetProfit() {
        return this.mathService.multiply(this.points, this.pricePerPoint);
    }

    /**
     * Calculates the average {@link AntherTrade} duration in seconds
     *
     * @param trades {@link Collection} of {@link AntherTrade}
     * @return average trade duration
     */
    private long calculateAverageTradeDuration(final Collection<AntherTrade> trades) {
        return (long) trades.stream().mapToLong(AntherTrade::getTradeDuration).filter(val -> val < 390).average().orElse(0.0);
    }

    /**
     * Returns true if the given list of trades have total positive sum of points
     *
     * @param list {@link List} of {@link AntherTrade}
     * @return true if all points are greater than zero
     */
    private boolean isPositive(final List<AntherTrade> list) {
        return CollectionUtils.isNotEmpty(list) && list.stream().mapToDouble(AntherTrade::getPoints).sum() > 0.0;
    }

    /**
     * Calculates the drawdown history for the list of trades as if they were executed sequentially
     *
     * @return {@link List} if {@link DrawdownEntry}
     */
    private List<DrawdownEntry> calculateDrawdown() {

        final List<DrawdownEntry> entries = new ArrayList<>();

        double sum = 0.0;
        for (final AntherTrade trade : this.trades.stream().sorted(Comparator.comparing(AntherTrade::getTradeOpenTime).thenComparing(AntherTrade::getTradeCloseTime)).toList()) {
            sum = this.mathService.add(trade.getPoints(), sum);
            entries.add(new DrawdownEntry(trade.getPoints(), sum));
        }

        return entries;
    }


    //  INNER CLASSES

    /**
     * Minor inner class that is meant to keep track of each trade's overall performance in an iterative method
     */
    @Getter
    @AllArgsConstructor
    private static class DrawdownEntry {

        private double points;

        private double cumulativePoints;
    }
}
