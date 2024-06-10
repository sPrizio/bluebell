package com.bluebell.aurora.services.reporting;

import com.bluebell.aurora.models.parameter.strategy.impl.BasicStrategyParameters;
import com.bluebell.aurora.models.parameter.strategy.impl.BloomStrategyParameters;
import com.bluebell.aurora.models.strategy.StrategyResult;
import com.bluebell.aurora.strategies.Strategy;
import com.bluebell.aurora.strategies.impl.Bloom;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Service that allows reports to be generated for strategy results
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public class ReportingService<S extends Strategy<P>, P extends BasicStrategyParameters> {

    private final Class<S> strategy;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d yyyy");


    //  CONSTRUCTORS

    public ReportingService(final Class<S> clazz) {
        this.strategy = clazz;
    }


    //  METHODS

    /**
     * Generates a report to a text file of a list of strategy results
     *
     * @param unit {@link ChronoUnit}
     * @param strategyResults list of list of strategy results (allows for multiple configurations)
     */
    public void generateReportForStrategyResults(final ChronoUnit unit, final Map<LocalDate, List<StrategyResult<P>>> strategyResults) {

        final StringBuilder stringBuilder = new StringBuilder();
        for (final Map.Entry<LocalDate, List<StrategyResult<P>>> entry : strategyResults.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList()) {
            final File tempFile = new File(getDataRoot() + String.format("report-%s.txt", entry.getKey().format(DateTimeFormatter.ISO_DATE)));
            try (FileOutputStream os = new FileOutputStream(tempFile)) {
                stringBuilder
                        .append("Period: ").append(entry.getKey().format(DATE_FORMATTER)).append(" to ").append(entry.getKey().plus(1, unit).format(DATE_FORMATTER))
                        .append("\n").append("Aggregation Period: ").append(unit)
                        .append("\n")
                        .append("\n");

                for (StrategyResult<P> pStrategyResult : entry.getValue()) {
                    if (pStrategyResult.getStrategyParameters() instanceof BloomStrategyParameters bs) {
                        stringBuilder.append("Variance: ").append(bs.getVariance()).append("%");
                        if (bs.isNormalize()) {
                            stringBuilder.append(" with normalization").append("\n");
                        } else {
                            stringBuilder.append("\n");
                        }
                    }

                    stringBuilder
                            .append(pStrategyResult.getStrategyParameters().getDescription()).append("\n")
                            .append(pStrategyResult.getWins().size()).append(" wins / ").append(pStrategyResult.getLosses().size()).append(" losses").append("\n")
                            .append("Net Points: ").append(pStrategyResult.getPoints()).append("\n")
                            .append("Net Profit: ").append("$").append(pStrategyResult.getNetProfit()).append("\n")
                            .append("Daily Win Percentage: ").append(pStrategyResult.getDailyWinPercentage()).append("%").append("\n")
                            .append("Drawdown: ").append(pStrategyResult.getMaxDrawdown()).append(" points").append("\n")
                            .append("\n");
                }

                os.write(stringBuilder.toString().getBytes());
                stringBuilder.setLength(0);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }


    //  HELPERS

    /**
     * Returns the root folder for reports
     *
     * @return sample data path
     */
    private String getDataRoot() {

        final String result;
        if (this.strategy.isAssignableFrom(Bloom.class)) {
            result = Objects.requireNonNull(getClass().getClassLoader().getResource("reports")).getFile() + "bloom/";
        } else {
            result = Objects.requireNonNull(getClass().getClassLoader().getResource("reports")).getFile() + "general/";
        }

        final File directory = new File(result);
        if (!directory.exists()){
            directory.mkdirs();
        }

        return result;
    }
}
