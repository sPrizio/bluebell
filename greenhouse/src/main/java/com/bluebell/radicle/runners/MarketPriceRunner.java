package com.bluebell.radicle.runners;

import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.util.DirectoryUtil;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.parsers.impl.FirstRateDataParser;
import com.bluebell.radicle.parsers.impl.MetaTrader4DataParser;
import com.bluebell.radicle.parsers.impl.TradingViewDataParser;
import com.bluebell.radicle.services.data.MarketDataIngestionService;
import com.bluebell.radicle.services.market.MarketPriceService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

/**
 * Generates testing {@link MarketPrice}s
 *
 * @author Stephen Prizio
 * @version 0.1.7
 */
@Component
@Order(8)
@Profile("dev")
public class MarketPriceRunner extends AbstractRunner implements CommandLineRunner {

    @Value("${bluebell.data.root}")
    private String dataRoot;

    @Value("${bluebell.init.market.data}")
    private String init;

    @Resource(name = "marketDataIngestionService")
    private MarketDataIngestionService marketDataIngestionService;

    @Resource(name = "marketPriceService")
    private MarketPriceService marketPriceService;


    //  METHODS

    @Override
    @Transactional
    public void run(final String... args) {

        logStart();

        if (Boolean.parseBoolean(this.init)) {
            this.marketPriceService.saveAllSet(this.marketDataIngestionService.ingest(DataSource.FIRST_RATE_DATA, "NDX", getArchive()).getValue2());
            this.marketPriceService.saveAllSet(this.marketDataIngestionService.ingest(DataSource.METATRADER4, "NDAQ100", getArchive()).getValue2());
            this.marketPriceService.saveAllSet(this.marketDataIngestionService.ingest(DataSource.TRADING_VIEW, "US100", getArchive()).getValue2());
        } else {
            final FirstRateDataParser firstRateDataParser = new FirstRateDataParser(false, "NDX", this.dataRoot);
            final MetaTrader4DataParser metaTrader4DataParser = new MetaTrader4DataParser(false, "NDAQ100", this.dataRoot);
            final TradingViewDataParser tradingViewDataParser = new TradingViewDataParser(false, "US100", this.dataRoot);

            this.marketPriceService.saveAll(firstRateDataParser.parseMarketPrices("NDX_5min_sample.csv", MarketPriceTimeInterval.FIVE_MINUTE));
            this.marketPriceService.saveAll(metaTrader4DataParser.parseMarketPrices("NDAQ10030.csv", MarketPriceTimeInterval.THIRTY_MINUTE));
            this.marketPriceService.saveAll(tradingViewDataParser.parseMarketPrices("CFI_US100-30_8a062.csv", MarketPriceTimeInterval.THIRTY_MINUTE));
        }

        logEnd();
    }


    //  HELPERS

    /**
     * Gets the latest archive
     *
     * @return archive path
     */
    private String getArchive() {

        final File file = new File(String.format("%s%s%s", DirectoryUtil.getBaseProjectDirectory(), File.separator, this.dataRoot));
        if (!file.exists() || !file.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory: " + file.getAbsolutePath());
        }

        final String archive =
                Arrays.stream(Objects.requireNonNull(file.listFiles()))
                        .filter(File::isDirectory)
                        .map(File::getName)
                        .filter(name -> name.startsWith("archive_"))
                        .min(String::compareTo)
                        .orElse(null);


        return String.format("%s%s%s", this.dataRoot, File.separator, archive);
    }
}
