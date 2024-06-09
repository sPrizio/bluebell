package com.bluebell.aurora.services.reporting;

import com.bluebell.aurora.models.parameter.strategy.StrategyParameters;
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
import java.util.Objects;

/**
 * Service that allows reports to be generated for strategy results
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public class ReportingService<S extends Strategy<P>, P extends BasicStrategyParameters> {

    public void generateReportForStrategyResults(final LocalDate start, final LocalDate end, final ChronoUnit unit, final List<List<StrategyResult<P>>> strategyResults) {

        final StringBuilder stringBuilder = new StringBuilder();
        final File directory = new File(getDataRoot() + "report.txt");

        try (FileOutputStream outputStream = new FileOutputStream(directory, false)) {
            for (final List<StrategyResult<P>> strategyResultList : strategyResults) {
                for (final StrategyResult<P> strategyResult : strategyResultList) {
                    if (strategyResult.getStrategyParameters() instanceof BloomStrategyParameters bs) {
                        stringBuilder.append("Variance: ").append(bs.getVariance()).append("%");
                        if (bs.isNormalize()) {
                            stringBuilder.append(" with normalization").append("\n");
                        } else {
                            stringBuilder.append("\n");
                        }
                    }

                    stringBuilder
                            .append(strategyResult.getStrategyParameters().getDescription()).append("\n")
                            .append(strategyResult.getWins().size()).append(" wins ").append(strategyResult.getLosses().size()).append(" losses").append("\n")
                            .append(strategyResult.getPoints()).append(" points").append("\n")
                            .append("$").append(strategyResult.getNetProfit()).append("\n")
                            .append(strategyResult.getDailyWinPercentage()).append("%").append("\n")
                            .append("\n");
                }
            }

            outputStream.write(stringBuilder.toString().getBytes());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    //  HELPERS

    /**
     * Returns the root folder for reports
     *
     * @return sample data path
     */
    private String getDataRoot() {
        return Objects.requireNonNull(getClass().getClassLoader().getResource("reports")).getFile();
    }

    private String getDisplay(final StrategyResult result) {
        return """
                From %s to %s: \t %s points for $%s with trading success rate of %s and daily hit rate of %s with a max drawdown of %s points
                """.formatted(result.getStart().format(DateTimeFormatter.ofPattern("yyyy")), result.getEnd().format(DateTimeFormatter.ofPattern("yyyy")), result.getPoints(), result.getNetProfit(), result.getWinPercentage() + "%", result.getDailyWinPercentage() + "%", result.getMaxDrawdown());
    }
}
