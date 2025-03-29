package com.bluebell.radicle.services.data;

import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import com.bluebell.platform.util.DirectoryUtil;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.parsers.MarketPriceParser;
import com.bluebell.radicle.parsers.impl.FirstRateDataParser;
import com.bluebell.radicle.parsers.impl.MetaTrader4DataParser;
import com.bluebell.radicle.parsers.impl.TradingViewDataParser;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Triplet;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * Service responsible for obtaining market data within the ingress directory
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@Slf4j
@Setter
@Component("marketDataIngestionService")
public class MarketDataIngestionService {

    private boolean isTest = false;

    /**
     * Ingests market data files from the ingress folder
     *
     * @param dataSource {@link DataSource}
     * @param symbol symbol
     * @param dataRoot directory with data
     * @return {@link Triplet} with success flag, message and result set
     */
    public Triplet<Boolean, String, Set<MarketPrice>> ingest(final DataSource dataSource, final String symbol, final String dataRoot) {

        if (dataSource == null) {
            return Triplet.with(false, "No datasource specified!", Collections.emptySet());
        }

        if (StringUtils.isEmpty(symbol)) {
            return Triplet.with(false, "No symbol specified!", Collections.emptySet());
        }

        if (StringUtils.isEmpty(dataRoot)) {
            return Triplet.with(false, "No data root specified!", Collections.emptySet());
        }

        try {
            final Set<MarketPrice> marketPrices = new TreeSet<>();
            final MarketPriceParser parser = getMarketPriceParser(dataSource, symbol, dataRoot);
            final String basePath = this.isTest ? DirectoryUtil.getTestingResourcesDirectory() : DirectoryUtil.getBaseProjectDirectory();
            final File directory = new File(String.format("%s%s%s%s%s%s%s", basePath, File.separator, dataRoot, File.separator, dataSource.getDataRoot(), File.separator, symbol));

            if (!directory.exists() || !directory.isDirectory()) {
                return Triplet.with(false, String.format("Symbol %s does not exist", symbol), Collections.emptySet());
            }

            final File[] intervals = directory.listFiles();
            if (isEmptyArray(intervals)) {
                return Triplet.with(false, String.format("Symbol %s does not have any data", symbol), Collections.emptySet());
            }

            for (final File interval : intervals) {
                final File[] dataFiles = interval.listFiles();
                final MarketPriceTimeInterval marketPriceTimeInterval = GenericEnum.getByCode(MarketPriceTimeInterval.class, interval.getName().toUpperCase());
                if (marketPriceTimeInterval == null || isEmptyArray(dataFiles)) {
                    LOGGER.error("{} was not a valid time interval or no data was available", interval.getName());
                    continue;
                }

                for (final File dataFile : dataFiles) {
                    final AggregatedMarketPrices aggregatedMarketPrices = parser.parseMarketPrices(dataFile.getName(), marketPriceTimeInterval);
                    marketPrices.addAll(aggregatedMarketPrices.marketPrices());
                }
            }

            final File processedDirectory = new File(String.format("%s%s%s%s%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, dataRoot, File.separator, "/processed", File.separator, dataSource.getDataRoot()));
            if (!processedDirectory.exists()) {
                processedDirectory.mkdirs();
            }

            FileUtils.copyDirectory(directory.getParentFile(), processedDirectory);
            FileUtils.deleteDirectory(directory.getParentFile());
            return Triplet.with(true, "MarketPrices fetched successfully", marketPrices);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            return Triplet.with(false, sw.toString(), Collections.emptySet());
        }
    }


    //  HELPERS

    /**
     * Returns the {@link MarketPriceParser} based on the given info
     *
     * @param dataSource {@link DataSource}
     * @param symbol symbol
     * @param dataRoot directory with data
     * @return {@link MarketPriceParser}
     */
    private MarketPriceParser getMarketPriceParser(final DataSource dataSource, final String symbol, final String dataRoot) {
        final MarketPriceParser parser;
        switch (dataSource) {
            case METATRADER4 -> parser = new MetaTrader4DataParser(this.isTest, symbol, dataRoot);
            case TRADING_VIEW -> parser = new TradingViewDataParser(this.isTest, symbol, dataRoot);
            case FIRST_RATE_DATA -> parser = new FirstRateDataParser(this.isTest, symbol, dataRoot);
            default -> throw new UnsupportedOperationException("Unsupported DataSource: " + dataSource);
        }
        return parser;
    }

    /**
     * Checks if the given array is empty
     *
     * @param array array
     * @return true if null or empty
     */
    private boolean isEmptyArray(final Object[] array) {
        return array == null || array.length == 0;
    }
}
