package com.bluebell.anther.services.reporting.impl;

import com.bluebell.anther.models.parameter.strategy.impl.SproutStrategyParameters;
import com.bluebell.anther.models.simulation.SimulationResult;
import com.bluebell.anther.models.strategy.CumulativeStrategyReportEntry;
import com.bluebell.anther.models.strategy.StrategyResult;
import com.bluebell.anther.models.trade.Trade;
import com.bluebell.anther.strategies.impl.Sprout;
import com.bluebell.radicle.services.MathService;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Extends the {@link StrategyReportingService} specific for {@link Sprout}
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public class SproutReportingService extends StrategyReportingService<Sprout, SproutStrategyParameters> {

    private final MathService mathService = new MathService();

    public SproutReportingService() {
        super(Sprout.class);
    }


    //  METHODS

    /**
     * Generates a list of files representing the cumulative performance of strategy results
     *
     * @param simulationResult {@link Map} of {@link List} of {@link StrategyResult}
     */
    public void generateCumulativeReport(final SimulationResult<SproutStrategyParameters> simulationResult) {

        final Map<String, List<Trade>> map = new HashMap<>();
        simulationResult.result().values().stream().flatMap(List::stream).forEach(sr -> {
            final String key = generateKey(sr);
            List<Trade> trades = new ArrayList<>();
            if (map.containsKey(key)) {
                trades = map.get(key);
            }

            trades.addAll(sr.getTrades());
            map.put(key, trades.stream().sorted(Comparator.comparing(Trade::getTradeOpenTime).thenComparing(Trade::getTradeCloseTime)).toList());
        });

        final StringBuilder stringBuilder = new StringBuilder();
        map.forEach((key, value) -> {
            final Pair<Double, Double> deconstructedKey = deconstructKey(key);
            final File tempFile = new File(getContentRoot("cumulative") + String.format("cumulative-%.2f-%.2f.txt", deconstructedKey.getValue0(), deconstructedKey.getValue1()));
            try (FileOutputStream os = new FileOutputStream(tempFile)) {
                stringBuilder
                        .append("Cumulative Trace for Profit Multiplier: ").append(deconstructedKey.getValue0()).append(" and Allowable Risk: ").append(deconstructedKey.getValue1()).append(" points")
                        .append("\n")
                        .append("\n");

                final double pricePerPoint = simulationResult.result().entrySet().iterator().next().getValue().getFirst().getStrategyParameters().getPricePerPoint();
                final List<CumulativeStrategyReportEntry> entries = getCumulativeReportEntries(value, pricePerPoint);
                entries.forEach(entry -> stringBuilder
                        .append(formatNumber(entry.points()))
                        .append("\t$").append(formatNumber(entry.netProfit()))
                        .append("\t").append(formatNumber(entry.trades()))
                        .append("\t").append(formatDateTime(entry.opened()))
                        .append("\t").append(formatDateTime(entry.closed()))
                        .append("\t\t").append(formatNumber(entry.pointsForTrade()))
                        .append("\t$").append(formatNumber(entry.profitForTrade()))
                        .append("\n")
                );

                os.write(stringBuilder.toString().getBytes());
                stringBuilder.setLength(0);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
    }


    //  HELPERS

    /**
     * Generates a key based on the start hour, minute and variance of the strategy result's parameters
     *
     * @param strategyResult {@link StrategyResult}
     * @return key string
     */
    private String generateKey(final StrategyResult<SproutStrategyParameters> strategyResult) {
        return String.format("pm:%.2f|ar:%.2f", strategyResult.getStrategyParameters().getProfitMultiplier(), strategyResult.getStrategyParameters().getAllowableRisk());
    }

    /**
     * Deconstructs the generated keys into its constituents
     *
     * @param key key string
     * @return {@link Triplet}
     */
    private Pair<Double, Double> deconstructKey(final String key) {
        final String[] split = key.split("\\|");
        return Pair.with(Double.parseDouble(split[0].replace("pm:", StringUtils.EMPTY)), Double.parseDouble(split[1].replace("ar:", StringUtils.EMPTY)));
    }

    /**
     * Computes a bunny trail per trade
     *
     * @return {@link List} of {@link CumulativeStrategyReportEntry}
     */
    private List<CumulativeStrategyReportEntry> getCumulativeReportEntries(final List<Trade> trades, final double pricePerPoint) {

        final List<CumulativeStrategyReportEntry> entries = new ArrayList<>();

        int cumTrades = 0;
        double cumPoints = 0.0;
        double cumProfit = 0.0;

        for (final Trade trade : trades) {
            cumTrades += 1;
            cumPoints = this.mathService.add(cumPoints, trade.getPoints());
            cumProfit = this.mathService.add(cumProfit, trade.calculateProfit(pricePerPoint));

            entries.add(new CumulativeStrategyReportEntry(cumPoints, cumProfit, cumTrades, trade.getTradeOpenTime(), trade.getTradeCloseTime(), trade.getPoints(), trade.calculateProfit(pricePerPoint)));
        }

        return entries;
    }

    /**
     * Formats a double
     *
     * @param number double
     * @return properly formatted double
     */
    private String formatNumber(final double number) {
        return StringUtils.rightPad(String.format("%.2f", number), 10, " ");
    }

    /**
     * Formats an integer
     *
     * @param number integer
     * @return properly formatted integer
     */
    private String formatNumber(final int number) {
        return StringUtils.rightPad(StringUtils.leftPad(String.valueOf(number), 2, "0"), 7, " ");
    }

    /**
     * Formats a {@link LocalDateTime}
     *
     * @param dateTime {@link LocalDateTime}
     * @return formatted string
     */
    private String formatDateTime(final LocalDateTime dateTime) {
        return StringUtils.rightPad(dateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy 'at' HH:mm:ss")).trim(), 10, " ");
    }
}
