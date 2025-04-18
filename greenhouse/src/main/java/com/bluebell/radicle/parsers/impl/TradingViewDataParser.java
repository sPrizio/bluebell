package com.bluebell.radicle.parsers.impl;

import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import com.bluebell.platform.util.DirectoryUtil;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.exceptions.parsing.TradingViewDataParsingException;
import com.bluebell.radicle.parsers.MarketPriceParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Parses data from TradingView data files
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Slf4j
public class TradingViewDataParser extends AbstractDataParser implements MarketPriceParser {

    private final boolean isTest;
    private final String symbol;
    private final String dataRoot;

    public TradingViewDataParser(final boolean isTest, final String symbol, final String dataRoot) {
        this.isTest = isTest;
        this.symbol = symbol;
        this.dataRoot = dataRoot;
    }


    //  METHODS

    @Override
    public AggregatedMarketPrices parseMarketPrices(final String file, final MarketPriceTimeInterval interval) {

        final String sampleFile = getDataRoot(interval) + "/" + file;
        if (!validateFile(sampleFile)) {
            LOGGER.error("File {} was not found!\n", file);
            return AggregatedMarketPrices.builder().marketPrices(new TreeSet<>()).interval(interval).dataSource(DataSource.TRADING_VIEW).build();
        }

        final TreeSet<MarketPrice> marketPrices = new TreeSet<>();
        try (LineIterator lineIterator = FileUtils.lineIterator(new File(sampleFile))) {
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if (line.startsWith("time")) {
                    line = lineIterator.nextLine();
                }

                final String[] lineComponents = line.split(",");
                marketPrices.add(
                        MarketPrice
                                .builder()
                                .date(LocalDateTime.parse(lineComponents[0], DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                                .interval(interval)
                                .open(parseDoubleFromString(lineComponents[1]))
                                .high(parseDoubleFromString(lineComponents[2]))
                                .low(parseDoubleFromString(lineComponents[3]))
                                .close(parseDoubleFromString(lineComponents[4]))
                                .symbol(this.symbol)
                                .dataSource(DataSource.TRADING_VIEW)
                                .build()
                );
            }
        } catch (Exception e) {
            throw new TradingViewDataParsingException(String.format("An error occurred while parsing the file. Error: %s", e.getMessage()), e);
        }

        return AggregatedMarketPrices.builder().marketPrices(marketPrices).interval(interval).dataSource(DataSource.TRADING_VIEW).build();
    }

    @Override
    public Map<LocalDate, AggregatedMarketPrices> parseMarketPricesByDate(final MarketPriceTimeInterval interval) {

        final File directory = new File(getDataRoot(interval));
        final File[] files = directory.listFiles();

        if (files == null || files.length == 0) {
            throw new TradingViewDataParsingException(String.format("Directory %s is empty or does not exist!", directory.getName()));
        }

        final Map<LocalDate, AggregatedMarketPrices> masterCollection = new HashMap<>();
        for (final File file : files) {
            final AggregatedMarketPrices marketPrices;
            try {
                switch (interval) {
                    case THIRTY_MINUTE -> marketPrices = parseMarketPrices(file.getName(), MarketPriceTimeInterval.THIRTY_MINUTE);
                    default -> marketPrices = AggregatedMarketPrices.builder().marketPrices(new TreeSet<>()).interval(interval).dataSource(DataSource.TRADING_VIEW).build();
                }

                if (CollectionUtils.isNotEmpty(marketPrices.marketPrices())) {
                    masterCollection.putAll(generateMasterCollection(marketPrices, interval, DataSource.TRADING_VIEW));
                }
            } catch (Exception e) {
                LOGGER.error("An error occurred while parsing the file {}. Error: {}", file.getName(), e.getMessage(), e);
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
    private String getDataRoot(final MarketPriceTimeInterval interval) {

        try {
            final String root;
            if (this.isTest) {
                root = DirectoryUtil.getTestingResourcesDirectory() + File.separator + this.dataRoot + File.separator + String.format("%s%s%s%s%s", DataSource.TRADING_VIEW.getDataRoot(), File.separator, this.symbol, File.separator, interval.toString());
            } else {
                root = DirectoryUtil.getBaseProjectDirectory() + File.separator + this.dataRoot + File.separator + String.format("%s%s%s%s%s", DataSource.TRADING_VIEW.getDataRoot(), File.separator, this.symbol, File.separator, interval.toString());
            }

            return root;
        } catch (Exception e) {
            throw new TradingViewDataParsingException(String.format("An error occurred while retrieving the data root. Likely no data exists for the requested interval of time. Error: %s", e.getMessage()), e);
        }
    }
}
