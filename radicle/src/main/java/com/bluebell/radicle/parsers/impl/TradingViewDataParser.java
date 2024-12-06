package com.bluebell.radicle.parsers.impl;

import com.bluebell.radicle.enums.RadicleTimeInterval;
import com.bluebell.radicle.exceptions.parser.TradingViewDataParsingException;
import com.bluebell.radicle.models.AggregatedMarketPrices;
import com.bluebell.radicle.models.MarketPrice;
import com.bluebell.radicle.parsers.MarketPriceParser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Parses data from TradingView data files
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
public class TradingViewDataParser extends AbstractDataParser implements MarketPriceParser {

    private final boolean isTest;
    private final String symbol;

    public TradingViewDataParser(final boolean isTest, final String symbol) {
        this.isTest = isTest;
        this.symbol = symbol;
    }


    //  METHODS

    @Override
    public AggregatedMarketPrices parseMarketPrices(final String file, final RadicleTimeInterval interval) {

        final String sampleFile = getDataRoot(interval) + "/" + file;
        if (!validateFile(sampleFile)) {
            System.out.printf("File %s was not found!%n", file);
            return new AggregatedMarketPrices(new TreeSet<>(), interval);
        }

        final TreeSet<MarketPrice> marketPrices = new TreeSet<>();
        try (LineIterator lineIterator = FileUtils.lineIterator(new File(sampleFile))) {
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if (line.startsWith("time")) {
                    line = lineIterator.nextLine();
                }

                final String[] lineComponents = line.split(",");
                marketPrices.add(new MarketPrice(
                        //  example date: 2024-07-19T16:30:00-04:00
                        LocalDateTime.parse(lineComponents[0], DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                        interval,
                        parseDoubleFromString(lineComponents[1]),
                        parseDoubleFromString(lineComponents[2]),
                        parseDoubleFromString(lineComponents[3]),
                        parseDoubleFromString(lineComponents[4])
                ));
            }
        } catch (Exception e) {
            throw new TradingViewDataParsingException(String.format("An error occurred while parsing the file. Error: %s", e.getMessage()), e);
        }

        return new AggregatedMarketPrices(marketPrices, interval);
    }

    @Override
    public Map<LocalDate, AggregatedMarketPrices> parseMarketPricesByDate(final RadicleTimeInterval interval) {

        final File directory = new File(getDataRoot(interval));
        final File[] files = directory.listFiles();

        if (files == null || files.length == 0) {
            throw new TradingViewDataParsingException(String.format("Directory %s was empty!", directory.getName()));
        }

        final Map<LocalDate, AggregatedMarketPrices> masterCollection = new HashMap<>();
        for (final File file : files) {
            final AggregatedMarketPrices marketPrices;
            try {
                switch (interval) {
                    case THIRTY_MINUTE -> marketPrices = parseMarketPrices(file.getName(), RadicleTimeInterval.THIRTY_MINUTE);
                    default -> marketPrices = new AggregatedMarketPrices(new TreeSet<>(), interval);
                }

                if (CollectionUtils.isNotEmpty(marketPrices.marketPrices())) {
                    masterCollection.putAll(generateMasterCollection(marketPrices, interval));
                }
            } catch (Exception e) {
                System.out.printf("An error occurred while parsing the file %s. Error: %s", file.getName(), e.getMessage());
            }
        }

        return masterCollection;
    }


    //  HELPERS

    /**
     * Returns the root folder for sample data
     *
     * @return sample data path
     */
    private String getDataRoot(final RadicleTimeInterval interval) {

        try {
            final String root = Objects.requireNonNull(getClass().getClassLoader().getResource(String.format("tradingview/%s/%s", this.symbol, interval.toString()))).getFile();
            if (this.isTest && !root.contains("test-classes")) {
                return root.replace("classes", "test-classes");
            }

            return root;
        } catch (Exception e) {
            throw new TradingViewDataParsingException(String.format("An error occurred while retrieving the data root. Likely no data exists for the requested interval of time. Error: %s", e.getMessage()), e);
        }
    }
}
