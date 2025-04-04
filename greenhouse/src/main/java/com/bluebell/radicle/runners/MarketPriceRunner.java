package com.bluebell.radicle.runners;

import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.radicle.parsers.impl.FirstRateDataParser;
import com.bluebell.radicle.parsers.impl.MetaTrader4DataParser;
import com.bluebell.radicle.parsers.impl.TradingViewDataParser;
import com.bluebell.radicle.services.market.MarketPriceService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generates testing {@link MarketPrice}s
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@Component
@Order(8)
@Profile("dev")
public class MarketPriceRunner extends AbstractRunner implements CommandLineRunner {

    @Value("${bluebell.data.root}")
    private String dataRoot;

    @Resource(name = "marketPriceService")
    private MarketPriceService marketPriceService;


    //  METHODS

    @Override
    @Transactional
    public void run(String... args) {

        logStart();

        final FirstRateDataParser firstRateDataParser = new FirstRateDataParser(false, "NDX", this.dataRoot);
        final MetaTrader4DataParser metaTrader4DataParser = new MetaTrader4DataParser(false, "NDAQ100", this.dataRoot);
        final TradingViewDataParser tradingViewDataParser = new TradingViewDataParser(false, "US100", this.dataRoot);

        this.marketPriceService.saveAll(firstRateDataParser.parseMarketPrices("NDX_5min_sample.csv", MarketPriceTimeInterval.FIVE_MINUTE));
        this.marketPriceService.saveAll(metaTrader4DataParser.parseMarketPrices("NDAQ10030.csv", MarketPriceTimeInterval.THIRTY_MINUTE));
        this.marketPriceService.saveAll(tradingViewDataParser.parseMarketPrices("CFI_US100-30_8a062.csv", MarketPriceTimeInterval.THIRTY_MINUTE));

        logEnd();
    }
}
