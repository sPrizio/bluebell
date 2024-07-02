package com.bluebell.radicle.parsers.impl;

import com.bluebell.radicle.enums.RadicleTimeInterval;
import com.bluebell.radicle.indicators.impl.ExponentialMovingAverageIndicator;
import com.bluebell.radicle.models.AggregatedMarketPrices;
import com.bluebell.radicle.models.MarketPrice;
import com.bluebell.radicle.parsers.MarketPriceParser;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Parses data from FirstData
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
@NoArgsConstructor
public class FirstRateDataParser implements MarketPriceParser {

    private boolean isTest = false;
    private final ExponentialMovingAverageIndicator ema20 = new ExponentialMovingAverageIndicator(20);
    private final ExponentialMovingAverageIndicator ema50 = new ExponentialMovingAverageIndicator(50);
    private final ExponentialMovingAverageIndicator ema100 = new ExponentialMovingAverageIndicator(100);
    private final ExponentialMovingAverageIndicator ema200 = new ExponentialMovingAverageIndicator(200);


    //  CONSTRUCTORS

    public FirstRateDataParser(final boolean isTest) {
        this.isTest = isTest;
    }


    //  METHODS

    @Override
    public AggregatedMarketPrices parseMarketPrices(final String file, final RadicleTimeInterval interval) {

        final String sampleFile = getDataRoot() + "/" + file;
        if (!validateFile(sampleFile)) {
            System.out.printf("File %s was not found!%n", file);
            return new AggregatedMarketPrices(new TreeSet<>(), interval);
        }

        final DateTimeFormatter dateTimeFormatter;
        if (interval == RadicleTimeInterval.ONE_DAY) {
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        } else {
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        }

        final TreeSet<MarketPrice> marketPrices = new TreeSet<>();
        try (LineIterator lineIterator = FileUtils.lineIterator(new File(sampleFile))) {
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if (line.startsWith("timestamp")) {
                    line = lineIterator.nextLine();
                }

                final String[] lineComponents = line.split(",");
                marketPrices.add(new MarketPrice(
                        interval ==
                                RadicleTimeInterval.ONE_DAY ? LocalDate.parse(lineComponents[0], dateTimeFormatter).atStartOfDay()
                                : LocalDateTime.parse(lineComponents[0], dateTimeFormatter),
                        interval,
                        parseDoubleFromString(lineComponents[1]),
                        parseDoubleFromString(lineComponents[2]),
                        parseDoubleFromString(lineComponents[3]),
                        parseDoubleFromString(lineComponents[4]),
                        new HashMap<>()
                ));
            }
        } catch (Exception e) {
            System.out.printf(e.getMessage(), e);
        }

        handleIndicators(new ArrayList<>(marketPrices));
        return new AggregatedMarketPrices(marketPrices, interval);
    }

    @Override
    public Map<LocalDate, AggregatedMarketPrices> parseMarketPricesByDate(final RadicleTimeInterval interval) {

        final AggregatedMarketPrices marketPrices;
        switch (interval) {
            case ONE_MINUTE -> marketPrices = parseMarketPrices(this.isTest ? "NDX_1min_sample.csv" : "NDX_full_1min.txt", RadicleTimeInterval.ONE_MINUTE);
            case FIVE_MINUTE -> marketPrices = parseMarketPrices(this.isTest ? "NDX_5min_sample.csv" : "NDX_full_5min.txt", RadicleTimeInterval.FIVE_MINUTE);
            case THIRTY_MINUTE -> marketPrices = parseMarketPrices(this.isTest ? "NDX_30min_sample.csv" : "NDX_full_30min.txt", RadicleTimeInterval.THIRTY_MINUTE);
            case ONE_HOUR -> marketPrices = parseMarketPrices(this.isTest ? "NDX_1hour_sample.csv" : "NDX_full_1hour.txt", RadicleTimeInterval.ONE_HOUR);
            case ONE_DAY -> marketPrices = parseMarketPrices(this.isTest ? "" : "NDX_1day_sample.csv", RadicleTimeInterval.ONE_DAY);
            default -> marketPrices = new AggregatedMarketPrices(new TreeSet<>(), interval);
        }

        final Map<LocalDate, AggregatedMarketPrices> masterCollection = new HashMap<>();
        marketPrices.marketPrices().forEach(marketPrice -> {
            final AggregatedMarketPrices mapPrices;
            if (masterCollection.containsKey(marketPrice.date().toLocalDate())) {
                mapPrices = masterCollection.get(marketPrice.date().toLocalDate());
            } else {
                mapPrices = new AggregatedMarketPrices(new TreeSet<>(), interval);
            }

            mapPrices.marketPrices().add(marketPrice);
            masterCollection.put(marketPrice.date().toLocalDate(), mapPrices);
        });

        return masterCollection;
    }


    //  HELPERS

    /**
     * Returns the root folder for sample data
     *
     * @return sample data path
     */
    private String getDataRoot() {

        final String root = Objects.requireNonNull(getClass().getClassLoader().getResource("firstratedata")).getFile();
        if (this.isTest && !root.contains("test-classes")) {
            return root.replace("classes", "test-classes");
        }

        return root;
    }

    /**
     * Handles computing indicator values
     *
     * @param marketPrices {@link List} of {@link MarketPrice}
     */
    private void handleIndicators(final List<MarketPrice> marketPrices) {
        for (int i = 0; i < marketPrices.size(); i++) {
            //  handle indicators
            calculateEma(i, marketPrices, this.ema20);
            calculateEma(i, marketPrices, this.ema50);
            calculateEma(i, marketPrices, this.ema100);
            calculateEma(i, marketPrices, this.ema200);
        }
    }

    /**
     * Computes the ema
     *
     * @param i index
     * @param marketPrices {@link List} of {@link MarketPrice}
     * @param ema {@link ExponentialMovingAverageIndicator} instance
     */
    private void calculateEma(final int i, final List<MarketPrice> marketPrices, final ExponentialMovingAverageIndicator ema) {
        if (i < ema.getPeriod()) {
            ema.computeValue(marketPrices.subList(0, i), marketPrices.get(i));
        } else {
            ema.computeValue(marketPrices.subList(i - (int) ema.getPeriod(), i), marketPrices.get(i));
        }
    }
}
