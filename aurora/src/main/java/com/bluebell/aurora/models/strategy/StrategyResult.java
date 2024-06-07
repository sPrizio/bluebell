package com.bluebell.aurora.models.strategy;

import com.bluebell.aurora.models.parameter.LimitParameter;
import com.bluebell.aurora.models.trade.Trade;
import com.bluebell.aurora.strategies.Strategy;
import com.bluebell.core.services.MathService;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Representation of the result of executing a {@link Strategy}
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
public class StrategyResult {

    private final MathService mathService = new MathService();

    private final LocalDate start;

    private final LocalDate end;

    private final List<Trade> trades;

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


    //  CONSTRUCTORS

    public StrategyResult(final LocalDate start, final LocalDate end, final Collection<Trade> trades, final LimitParameter buyLimit, final LimitParameter sellLimit, final double pricePerPoint) {

        this.start = start;
        this.end = end;
        this.trades = trades.stream().toList();

        final List<Trade> wins = getWins();
        final List<Trade> losses = getLosses();

        this.hits = wins.size();
        this.misses = losses.size();
        this.winPercentage = this.mathService.wholePercentage(this.hits, this.mathService.add(this.hits, this.misses));
        this.pointsGained = this.mathService.getDouble(wins.stream().mapToDouble(Trade::getPoints).sum());
        this.pointsLost = Math.abs(this.mathService.getDouble(losses.stream().mapToDouble(Trade::getPoints).sum()));
        this.points = this.mathService.getDouble(this.mathService.subtract(this.pointsGained, this.pointsLost));
        this.profitability = this.mathService.divide(this.pointsGained, this.pointsLost);
        this.retention = this.mathService.wholePercentage(this.pointsGained, this.mathService.add(this.pointsGained, this.pointsLost));
        this.buyLimit = buyLimit;
        this.sellLimit = sellLimit;
        this.pricePerPoint = pricePerPoint;
        this.netProfit = calculateNetProfit();
        this.averageTradeDuration = calculateAverageTradeDuration(trades);
    }


    //  METHODS

    /**
     * Returns a list of trades that were winners
     *
     * @return {@link List} of {@link Trade}
     */
    public List<Trade> getWins() {
        return this.trades.stream().filter(tr -> tr.getPoints() > 0.0).toList();
    }

    /**
     * Returns a list of trades that were losers
     *
     * @return {@link List} of {@link Trade}
     */
    public List<Trade> getLosses() {
        return this.trades.stream().filter(tr -> tr.getPoints() < 0.0).toList();
    }

    /**
     * Returns the number of days when more points were gained than lost of the total trading days
     *
     * @return whole percentage
     */
    public int getDailyWinPercentage() {

        final Map<LocalDate, List<Trade>> map = new HashMap<>();
        for (Trade tr : this.trades) {
            final List<Trade> found;
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
                        this.pricePerPoint
                );
    }


    //  HELPERS

    /**
     * Calculates the net profit
     *
     * @return net profit
     */
    private double calculateNetProfit() {
        return this.mathService.multiply(this.points, this.pricePerPoint);
    }

    /**
     * Calculates the average {@link Trade} duration in seconds
     *
     * @param trades {@link Collection} of {@link Trade}
     * @return average trade duration
     */
    private long calculateAverageTradeDuration(final Collection<Trade> trades) {
        return (long) trades.stream().mapToLong(Trade::getTradeDuration).filter(val -> val < 390).average().orElse(0.0);
    }

    /**
     * Returns true if the given list of trades have total positive sum of points
     *
     * @param list {@link List} of {@link Trade}
     * @return true if all points are greater than zero
     */
    private boolean isPositive(final List<Trade> list) {
        return CollectionUtils.isNotEmpty(list) && list.stream().mapToDouble(Trade::getPoints).sum() > 0.0;
    }
}
