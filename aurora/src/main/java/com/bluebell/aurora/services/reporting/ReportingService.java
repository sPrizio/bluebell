package com.bluebell.aurora.services.reporting;

import com.bluebell.aurora.models.strategy.StrategyResult;

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
public class ReportingService {

    public void generateReportForStrategyResults(final LocalDate start, final LocalDate end, final ChronoUnit unit, final List<StrategyResult> strategyResults) {

        final StringBuilder stringBuilder = new StringBuilder();
        try {
            final File directory = new File(getDataRoot() + "report.txt");
            if (directory.createNewFile()) {
                FileOutputStream outputStream = new FileOutputStream(directory, false);
                strategyResults.forEach(strategyResult -> stringBuilder.append(getDisplay(strategyResult)));
                outputStream.write(stringBuilder.toString().getBytes());
            }
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
