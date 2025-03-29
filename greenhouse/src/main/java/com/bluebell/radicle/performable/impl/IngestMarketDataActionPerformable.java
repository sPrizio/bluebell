package com.bluebell.radicle.performable.impl;

import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.models.core.nonentities.action.ActionData;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.parsers.MarketPriceParser;
import com.bluebell.radicle.parsers.impl.FirstRateDataParser;
import com.bluebell.radicle.parsers.impl.MetaTrader4DataParser;
import com.bluebell.radicle.parsers.impl.TradingViewDataParser;
import com.bluebell.radicle.performable.ActionPerformable;
import com.bluebell.radicle.services.market.MarketPriceService;
import jakarta.annotation.Resource;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implementation of {@link ActionPerformable} that ingests market data files
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@Slf4j
@Component("ingestMarketDataActionPerformable")
public class IngestMarketDataActionPerformable implements ActionPerformable {

    @Setter
    private DataSource dataSource = null;

    @Setter
    private String symbol;

    @Value("${bluebell.ingress.root}")
    private String dataRoot;

    @Resource(name = "marketPriceService")
    private MarketPriceService marketPriceService;


    //  METHODS

    @Override
    public ActionData perform() {

        if (this.dataSource == null) {
            return ActionData
                    .builder()
                    .success(false)
                    .logs("No DataSource specified for this action!")
                    .build();
        }

        if (StringUtils.isEmpty(this.symbol)) {
            return ActionData
                    .builder()
                    .success(false)
                    .logs("No symbol specified for this action!")
                    .build();
        }

        try {
            final Set<MarketPrice> marketPrices = new TreeSet<>();
            final MarketPriceParser parser = getMarketPriceParser();
            final File directory = new File(this.dataRoot + File.separator + this.symbol);

            if (!directory.exists() || !directory.isDirectory()) {
                return ActionData
                        .builder()
                        .success(false)
                        .logs(String.format("No data for symbol %s", this.symbol))
                        .build();
            }

            final File[] intervals = directory.listFiles();
            if (isEmptyArray(intervals)) {
                return ActionData
                        .builder()
                        .success(false)
                        .logs(String.format("Symbol %s does not have any time intervals", this.symbol))
                        .build();
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
                    marketPrices.addAll(this.marketPriceService.saveAll(aggregatedMarketPrices));
                }
            }

            return ActionData
                    .builder()
                    .success(true)
                    .data(marketPrices)
                    .logs("MarketPrices fetched successfully.")
                    .build();
        } catch (Exception e) {
            return ActionData
                    .builder()
                    .success(false)
                    .logs(getStackTraceAsString(e))
                    .build();
        }
    }


    //  HELPERS

    /**
     * Returns the {@link MarketPriceParser} based on the given info
     *
     * @return {@link MarketPriceParser}
     */
    private MarketPriceParser getMarketPriceParser() {
        final MarketPriceParser parser;
        switch (this.dataSource) {
            case METATRADER4 -> parser = new MetaTrader4DataParser(false, this.symbol, this.dataRoot);
            case TRADING_VIEW -> parser = new TradingViewDataParser(false, this.symbol, this.dataRoot);
            case FIRST_RATE_DATA -> parser = new FirstRateDataParser(false, this.symbol, this.dataRoot);
            default -> throw new UnsupportedOperationException("Unsupported DataSource: " + this.dataSource);
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
