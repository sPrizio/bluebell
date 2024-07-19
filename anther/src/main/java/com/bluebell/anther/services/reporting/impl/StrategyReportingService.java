package com.bluebell.anther.services.reporting.impl;

import com.bluebell.anther.models.parameter.strategy.impl.BasicStrategyParameters;
import com.bluebell.anther.models.parameter.strategy.impl.BloomStrategyParameters;
import com.bluebell.anther.models.parameter.strategy.impl.SproutStrategyParameters;
import com.bluebell.anther.models.simulation.SimulationResult;
import com.bluebell.anther.models.strategy.StrategyResult;
import com.bluebell.anther.services.reporting.ReportingService;
import com.bluebell.anther.strategies.Strategy;
import com.bluebell.anther.strategies.impl.Bloom;
import com.bluebell.anther.strategies.impl.Sprout;
import com.bluebell.anther.util.DirectoryUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * Service that allows reports to be generated for strategy results
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public class StrategyReportingService<S extends Strategy<P>, P extends BasicStrategyParameters> implements ReportingService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d yyyy");

    private final Class<S> strategy;


    //  CONSTRUCTORS

    public StrategyReportingService(final Class<S> clazz) {
        this.strategy = clazz;
    }


    //  METHODS

    /**
     * Generates a report to a text file of a list of strategy results
     *
     * @param unit            {@link ChronoUnit}
     * @param simulationResult {@link SimulationResult}
     */
    public void generateReportForSimulationResult(final ChronoUnit unit, final SimulationResult<P> simulationResult) {

        final StringBuilder stringBuilder = new StringBuilder();
        for (final Map.Entry<LocalDate, List<StrategyResult<P>>> entry : simulationResult.result().entrySet().stream().sorted(Map.Entry.comparingByKey()).toList()) {
            final File tempFile = new File(getContentRoot("reports") + String.format("report-%s-%s.txt", unit.toString().toLowerCase(), entry.getKey().format(DateTimeFormatter.ISO_DATE)));
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
                    } else if (pStrategyResult.getStrategyParameters() instanceof SproutStrategyParameters sp) {
                        stringBuilder.append("Profit Multiplier: ").append(sp.getProfitMultiplier()).append("\n");
                        stringBuilder.append("Allowable Risk: ").append(sp.getAllowableRisk()).append(" points").append("\n");
                    }

                    stringBuilder
                            .append(pStrategyResult.getStrategyParameters().getDescription()).append("\n")
                            .append(pStrategyResult.getWins().size()).append(" wins / ").append(pStrategyResult.getLosses().size()).append(" losses (").append(pStrategyResult.getWinPercentage()).append("%)").append("\n")
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
     * @param root reports root
     * @return root folder
     */
    protected String getContentRoot(final String root) {

        String result = DirectoryUtil.getDirectory(root);
        if (this.strategy.isAssignableFrom(Bloom.class)) {
            result += "bloom/";
        } else if (this.strategy.isAssignableFrom(Sprout.class)) {
            result += "sprout/";
        } else {
            result += "general/";
        }

        final File directory = new File(result);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return result;
    }
}
