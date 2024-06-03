package com.bluebell.aurora.models.strategy;

import com.bluebell.aurora.models.trade.Trade;
import com.bluebell.aurora.strategies.Strategy;
import com.bluebell.core.services.MathService;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

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

    private final int hits;

    private final int misses;

    private final int winPercentage;

    private final double netProfit;

    private final double pointsGained;

    private final double pointsLost;

    private final double points;

    private final double profitability;

    private final int retention;

    private final int profitMultiplier;

    private final int lossMultiplier;

    private final double pricePerPoint;

    private final long averageTradeDuration;


    //  CONSTRUCTORS

    public StrategyResult(final LocalDate start, final LocalDate end, final Collection<Trade> trades, final int profitMultiplier, final int lossMultiplier, final double pricePerPoint) {

        final List<Trade> wins = trades.stream().filter(tr -> tr.getPoints() > 0.0).toList();
        final List<Trade> losses = trades.stream().filter(tr -> tr.getPoints() < 0.0).toList();

        this.start = start;
        this.end = end;
        this.hits = wins.size();
        this.misses = losses.size();
        this.winPercentage = this.mathService.wholePercentage(hits, mathService.add(this.hits, this.misses));
        this.pointsGained = this.mathService.getDouble(wins.stream().mapToDouble(Trade::getPoints).sum());
        this.pointsLost = Math.abs(this.mathService.getDouble(losses.stream().mapToDouble(Trade::getPoints).sum()));
        this.points = this.mathService.getDouble(this.mathService.subtract(this.pointsGained, this.pointsLost));
        this.profitability = this.mathService.divide(this.pointsGained, this.pointsLost);
        this.retention = this.mathService.wholePercentage(this.pointsGained, this.mathService.add(this.pointsGained, this.pointsLost));
        this.profitMultiplier = profitMultiplier;
        this.lossMultiplier = lossMultiplier;
        this.pricePerPoint = pricePerPoint;
        this.netProfit = calculateNetProfit();
        this.averageTradeDuration = calculateAverageTradeDuration(trades);
    }


    //  METHODS

    @Override
    public String toString() {
        return """
                StrategyResult for period %s to %s { +
                \thits = %s
                \tmisses = %s
                \twinPercentage = %s
                \tnetProfit = %s
                \tpointsGained = %s
                \tpointsLost = %s
                \tpoints = %s
                \tprofitability = %s
                \tretention = %s
                \taverageTradeDuration = %s minutes
                \tprofitMultiplier = %s
                \tlossMultiplier = %s
                \tpricePerPoint = %s
                }
                """
                .formatted(
                        this.start.format(DateTimeFormatter.ISO_DATE),
                        this.end.format(DateTimeFormatter.ISO_DATE),
                        this.hits,
                        this.misses,
                        this.winPercentage + "%",
                        this.netProfit,
                        this.pointsGained,
                        this.pointsLost,
                        this.points,
                        this.profitability,
                        this.retention + "%",
                        this.averageTradeDuration,
                        this.profitMultiplier,
                        this.lossMultiplier,
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
}
