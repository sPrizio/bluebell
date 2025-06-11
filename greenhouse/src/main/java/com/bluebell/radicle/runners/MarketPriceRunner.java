package com.bluebell.radicle.runners;

import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.services.MathService;
import com.bluebell.platform.util.DirectoryUtil;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.parsers.impl.FirstRateDataParser;
import com.bluebell.radicle.parsers.impl.MetaTrader4DataParser;
import com.bluebell.radicle.parsers.impl.TradingViewDataParser;
import com.bluebell.radicle.repositories.account.AccountRepository;
import com.bluebell.radicle.repositories.market.MarketPriceRepository;
import com.bluebell.radicle.repositories.trade.TradeRepository;
import com.bluebell.radicle.services.data.MarketDataIngestionService;
import com.bluebell.radicle.services.market.MarketPriceService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Generates testing {@link MarketPrice}s
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Component
@Profile("dev")
@ConditionalOnProperty(name = "bluebell.cmdlr.market.data", havingValue = "true", matchIfMissing = true)
public class MarketPriceRunner extends AbstractRunner implements CommandLineRunner, Ordered {

    private static final Random RANDOM = new Random();
    private static final MathService MATH_SERVICE = new MathService();

    @Value("${bluebell.data.root}")
    private String dataRoot;

    @Value("${bluebell.init.market.data}")
    private String init;

    @Value("${bluebell.cmdlr.order.market-price}")
    private int order;

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "marketDataIngestionService")
    private MarketDataIngestionService marketDataIngestionService;

    @Resource(name = "marketPriceRepository")
    private MarketPriceRepository marketPriceRepository;

    @Resource(name = "marketPriceService")
    private MarketPriceService marketPriceService;

    @Resource(name = "tradeRepository")
    private TradeRepository tradeRepository;


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
            final FirstRateDataParser firstRateDataParser = new FirstRateDataParser(false, "NDX", getArchive());
            final MetaTrader4DataParser metaTrader4DataParser = new MetaTrader4DataParser(false, "NDAQ100", getArchive());
            final TradingViewDataParser tradingViewDataParser = new TradingViewDataParser(false, "US100", getArchive());

            this.marketPriceService.saveAll(firstRateDataParser.parseMarketPrices("NDX_5min_sample.csv", MarketPriceTimeInterval.FIVE_MINUTE));
            this.marketPriceService.saveAll(metaTrader4DataParser.parseMarketPrices("NDAQ_30MINUTE.csv", MarketPriceTimeInterval.THIRTY_MINUTE));
            this.marketPriceService.saveAll(tradingViewDataParser.parseMarketPrices("CFI_US100-30_8a062.csv", MarketPriceTimeInterval.THIRTY_MINUTE));

            final List<Trade> trades = this.tradeRepository
                    .findAllByAccount(this.accountRepository.findAccountByAccountNumber(1234L))
                    .stream()
                    .sorted(Comparator.comparing(Trade::getTradeOpenTime).reversed())
                    .toList();

            int count = 0;
            while (count < 5) {
                generateTestChartingData(trades.get(count));
                count = count + 1;
            }
        }

        logEnd();
    }

    @Override
    public int getOrder() {
        return this.order;
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

    /**
     * Generates a list of {@link MarketPrice} for the given {@link Trade}
     *
     * @param trade {@link Trade}
     */
    private void generateTestChartingData(final Trade trade) {
        LocalDateTime start = trade.getTradeOpenTime().withHour(9).withMinute(30).withSecond(0);
        LocalDateTime end = start.withHour(16).withMinute(0);

        generateTestChartingDataForSymbol(start, end, "Nasdaq 100", trade);
        generateTestChartingDataForSymbol(start, end, "S&P 500", trade);
    }

    /**
     * Generates market prices for the given symbol
     *
     * @param start  start interval
     * @param end    end interval
     * @param symbol symbol
     * @param trade  {@link Trade}
     */
    private void generateTestChartingDataForSymbol(final LocalDateTime start, final LocalDateTime end, final String symbol, final Trade trade) {
        generateTestChartingDataForTimeInterval(start, end, MarketPriceTimeInterval.ONE_MINUTE, symbol, trade);
        generateTestChartingDataForTimeInterval(start, end, MarketPriceTimeInterval.FIVE_MINUTE, symbol, trade);
        generateTestChartingDataForTimeInterval(start, end, MarketPriceTimeInterval.TEN_MINUTE, symbol, trade);
        generateTestChartingDataForTimeInterval(start, end, MarketPriceTimeInterval.FIFTEEN_MINUTE, symbol, trade);
        generateTestChartingDataForTimeInterval(start, end, MarketPriceTimeInterval.THIRTY_MINUTE, symbol, trade);
        generateTestChartingDataForTimeInterval(start, end, MarketPriceTimeInterval.ONE_HOUR, symbol, trade);
    }

    /**
     * Generates market prices for the given time interval
     *
     * @param start                   start interval
     * @param end                     end interval
     * @param marketPriceTimeInterval {@link MarketPriceTimeInterval}
     * @param symbol                  symbol
     * @param trade                   {@link Trade}
     */
    private void generateTestChartingDataForTimeInterval(final LocalDateTime start, final LocalDateTime end, final MarketPriceTimeInterval marketPriceTimeInterval, final String symbol, final Trade trade) {
        LocalDateTime compare = start;
        MarketPrice tracker = null;

        while (compare.isBefore(end)) {
            final MarketPrice marketPrice;
            double openPrice = getOpenPrice(trade);
            if (tracker == null) {
                marketPrice = generatePrice(compare, marketPriceTimeInterval, symbol, openPrice);
            } else {
                marketPrice = generatePrice(compare, marketPriceTimeInterval, symbol, tracker.getClose());
            }

            tracker = marketPrice;
            compare = compare.plus(marketPriceTimeInterval.getAmount(), marketPriceTimeInterval.getUnit());
        }
    }

    /**
     * Calculates the open price
     *
     * @param trade {@link Trade}
     * @return open price
     */
    private double getOpenPrice(Trade trade) {
        final double openPrice;
        if (trade.getTradeType() == TradeType.BUY) {
            if (trade.getNetProfit() > 0) {
                openPrice = trade.getOpenPrice() - 100.0;
            } else {
                openPrice = trade.getOpenPrice() + 100.0;
            }
        } else {
            if (trade.getNetProfit() > 0) {
                openPrice = trade.getOpenPrice() + 100.0;
            } else {
                openPrice = trade.getOpenPrice() - 100.0;
            }
        }
        return openPrice;
    }

    /**
     * Generates a {@link MarketPrice}
     *
     * @param compare                 date time
     * @param marketPriceTimeInterval {@link MarketPriceTimeInterval}
     * @param symbol                  symbol
     * @param openPrice               starting price
     * @return {@link MarketPrice}
     */
    private MarketPrice generatePrice(final LocalDateTime compare, final MarketPriceTimeInterval marketPriceTimeInterval, final String symbol, final double openPrice) {

        double high = openPrice + (RANDOM.nextDouble() * 150.0);
        double low = openPrice - (RANDOM.nextDouble() * 150.0);

        double actualHigh = Math.max(high, low);
        double actualLow = Math.min(high, low);

        double close = actualLow + (RANDOM.nextDouble() * (actualHigh - actualLow));

        final MarketPrice marketPrice = MarketPrice
                .builder()
                .date(compare)
                .interval(marketPriceTimeInterval)
                .symbol(symbol)
                .open(MATH_SERVICE.getDouble(openPrice))
                .high(MATH_SERVICE.getDouble(high))
                .low(MATH_SERVICE.getDouble(low))
                .close(MATH_SERVICE.getDouble(close))
                .volume(RANDOM.nextInt(10_000))
                .dataSource(DataSource.METATRADER4)
                .build();

        this.marketPriceRepository.upsertMarketPrice(marketPrice.getDate(), marketPrice.getInterval(), marketPrice.getSymbol(), marketPrice.getOpen(), marketPrice.getHigh(), marketPrice.getLow(), marketPrice.getClose(), marketPrice.getVolume(), marketPrice.getDataSource());
        return marketPrice;
    }
}
