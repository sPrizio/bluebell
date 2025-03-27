package com.bluebell.radicle.parsers.impl;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.exceptions.parsing.MetaTrader4ParsingException;
import com.bluebell.radicle.parsers.MarketPriceParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

/**
 * Parses data from MT4 data files
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
@Slf4j
public class MetaTrader4DataParser extends AbstractDataParser implements MarketPriceParser {

    private final boolean isTest;
    private final String symbol;


    public MetaTrader4DataParser() {
        this.isTest = false;
        this.symbol = StringUtils.EMPTY;
    }

    public MetaTrader4DataParser(final boolean isTest, final String symbol) {
        this.isTest = isTest;
        this.symbol = symbol;
    }


    //  METHODS

    @Override
    public AggregatedMarketPrices parseMarketPrices(final String file, final MarketPriceTimeInterval interval) {

        final String sampleFile = getDataRoot(interval) + "/" + file;
        if (!validateFile(sampleFile)) {
            LOGGER.error("File {} was not found!\n", file);
            return AggregatedMarketPrices.builder().marketPrices(new TreeSet<>()).interval(interval).dataSource(DataSource.METATRADER4).build();
        }

        final TreeSet<MarketPrice> marketPrices = new TreeSet<>();
        try (LineIterator lineIterator = FileUtils.lineIterator(new File(sampleFile))) {
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                final String[] lineComponents = line.split(",");
                marketPrices.add(
                        MarketPrice
                                .builder()
                                .date(LocalDateTime.parse(lineComponents[0] + " " + lineComponents[1], DateTimeFormatter.ofPattern(CorePlatformConstants.MT4_DATE_SHORT_TIME_FORMAT)))
                                .interval(interval)
                                .open(parseDoubleFromString(lineComponents[2]))
                                .high(parseDoubleFromString(lineComponents[3]))
                                .low(parseDoubleFromString(lineComponents[4]))
                                .close(parseDoubleFromString(lineComponents[5]))
                                .volume((long) parseDoubleFromString(lineComponents[6]))
                                .build()
                );
            }
        } catch (Exception e) {
            throw new MetaTrader4ParsingException(String.format("An error occurred while parsing the file. Error: %s", e.getMessage()), e);
        }

        return AggregatedMarketPrices.builder().marketPrices(marketPrices).interval(interval).dataSource(DataSource.METATRADER4).build();
    }

    @Override
    public Map<LocalDate, AggregatedMarketPrices> parseMarketPricesByDate(final MarketPriceTimeInterval interval) {

        final File directory = new File(getDataRoot(interval));
        final File[] files = directory.listFiles();

        if (files == null || files.length == 0) {
            throw new MetaTrader4ParsingException(String.format("Directory %s was empty!", directory.getName()));
        }

        final Map<LocalDate, AggregatedMarketPrices> masterCollection = new HashMap<>();
        for (final File file : files) {
            final AggregatedMarketPrices marketPrices;
            try {
                switch (interval) {
                    case ONE_MINUTE -> marketPrices = parseMarketPrices(file.getName(), MarketPriceTimeInterval.ONE_MINUTE);
                    case FIVE_MINUTE -> marketPrices = parseMarketPrices(file.getName(), MarketPriceTimeInterval.FIVE_MINUTE);
                    case FIFTEEN_MINUTE -> marketPrices = parseMarketPrices(file.getName(), MarketPriceTimeInterval.FIFTEEN_MINUTE);
                    case THIRTY_MINUTE -> marketPrices = parseMarketPrices(file.getName(), MarketPriceTimeInterval.THIRTY_MINUTE);
                    case ONE_HOUR -> marketPrices = parseMarketPrices(file.getName(), MarketPriceTimeInterval.ONE_HOUR);
                    case FOUR_HOUR -> marketPrices = parseMarketPrices(file.getName(), MarketPriceTimeInterval.FOUR_HOUR);
                    case ONE_DAY -> marketPrices = parseMarketPrices(file.getName(), MarketPriceTimeInterval.ONE_DAY);
                    case ONE_WEEK -> marketPrices = parseMarketPrices(file.getName(), MarketPriceTimeInterval.ONE_WEEK);
                    case ONE_MONTH -> marketPrices = parseMarketPrices(file.getName(), MarketPriceTimeInterval.ONE_MONTH);
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
            final String root = Objects.requireNonNull(getClass().getClassLoader().getResource(String.format("mt4/%s/%s", this.symbol, interval.toString()))).getFile();
            if (this.isTest && !root.contains("test-classes")) {
                return root.replace("classes", "test-classes");
            }

            return root;
        } catch (Exception e) {
            throw new MetaTrader4ParsingException(String.format("An error occurred while retrieving the data root. Likely no data exists for the requested interval of time. Error: %s", e.getMessage()), e);
        }
    }
}
