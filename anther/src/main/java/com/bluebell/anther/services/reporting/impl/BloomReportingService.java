package com.bluebell.anther.services.reporting.impl;

import com.bluebell.anther.models.parameter.strategy.impl.BloomStrategyParameters;
import com.bluebell.anther.models.strategy.CumulativeStrategyReportEntry;
import com.bluebell.anther.models.strategy.StrategyResult;
import com.bluebell.anther.models.trade.Trade;
import com.bluebell.anther.strategies.impl.Bloom;
import com.bluebell.radicle.services.MathService;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Triplet;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public class BloomReportingService extends StrategyReportingService<Bloom, BloomStrategyParameters> {

    private final MathService mathService = new MathService();

    public BloomReportingService() {
        super(Bloom.class);
    }


    //  METHODS

    public void generateCumulativeReport(final Map<LocalDate, List<StrategyResult<BloomStrategyParameters>>> strategyResults) {

        final Map<String, List<Trade>> map = new HashMap<>();
        strategyResults.values().stream().flatMap(List::stream).forEach(sr -> {
            final String key = generateKey(sr);
            List<Trade> trades = new ArrayList<>();
            if (map.containsKey(key)) {
                trades = map.get(key);
            }

            trades.addAll(sr.getTrades());
            map.put(key, trades);
        });

        final StringBuilder stringBuilder = new StringBuilder();
        map.forEach((key, value) -> {
            final Triplet<Integer, Integer, Double> deconstructedKey = deconstructKey(key);
            final File tempFile = new File(getContentRoot("cumulative") + String.format("cumulative-%s%s_%.2f.txt", StringUtils.leftPad(deconstructedKey.getValue0().toString(), 2, "0"), StringUtils.leftPad(deconstructedKey.getValue1().toString(), 2, "0"), deconstructedKey.getValue2()));
            try (FileOutputStream os = new FileOutputStream(tempFile)) {
                stringBuilder
                        .append("Cumulative Trace for the ").append(deconstructedKey.getValue0()).append(":").append(deconstructedKey.getValue1()).append(" candle with a variance of ").append(deconstructedKey.getValue2()).append("%")
                        .append("\nNormalization: ").append(strategyResults.entrySet().iterator().next().getValue().getFirst().getStrategyParameters().isNormalize())
                        .append("\n")
                        .append("\n");

                final double pricePerPoint = strategyResults.entrySet().iterator().next().getValue().getFirst().getStrategyParameters().getPricePerPoint();
                final List<CumulativeStrategyReportEntry> entries = getCumulativeReportEntries(value, pricePerPoint);
                entries.forEach(entry -> stringBuilder.append(formatNumber(entry.points())).append("\t$").append(formatNumber(entry.netProfit())).append("\t").append(formatNumber(entry.trades())).append("\n"));

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
    private String generateKey(final StrategyResult<BloomStrategyParameters> strategyResult) {
        return String.format("hour:%d|minute:%d|variance:%.2f", strategyResult.getStrategyParameters().getStartHour(), strategyResult.getStrategyParameters().getStartMinute(), strategyResult.getStrategyParameters().getVariance());
    }

    /**
     * Deconstructs the generated keys into its constituents
     *
     * @param key key string
     * @return {@link Triplet}
     */
    private Triplet<Integer, Integer, Double> deconstructKey(final String key) {
        final String[] split = key.split("\\|");
        return Triplet.with(
                Integer.parseInt(split[0].replace("hour:", StringUtils.EMPTY)),
                Integer.parseInt(split[1].replace("minute:", StringUtils.EMPTY)),
                Double.parseDouble(split[2].replace("variance:", StringUtils.EMPTY))
        );
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

            entries.add(new CumulativeStrategyReportEntry(cumPoints, cumProfit, cumTrades));
        }

        return entries;
    }

    private String formatNumber(final double number) {
        return StringUtils.rightPad(String.format("%.2f", number), 10, " ");
    }

    private String formatNumber(final int number) {
        return StringUtils.rightPad(StringUtils.leftPad(String.valueOf(number), 2, "0"), 7, " ");
    }
}
