package com.bluebell.radicle.services.market;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.parsers.impl.FirstRateDataParser;
import com.bluebell.radicle.repositories.market.MarketPriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link MarketPriceService}
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class MarketPriceServiceTest extends AbstractGenericTest {

    private final FirstRateDataParser firstRateDataParser = new FirstRateDataParser(true, "NDX", "/test-data");

    @Autowired
    private MarketPriceRepository marketPriceRepository;

    @Autowired
    private MarketPriceService marketPriceService;

    @BeforeEach
    void setUp() {
        this.marketPriceRepository.deleteAll();
    }


    //  ----------------- saveAll -----------------

    @Test
    void test_saveAll_badData() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.marketPriceService.saveAll(null))
                .withMessageContaining(CorePlatformConstants.Validation.MarketPrice.AGGREGATED_PRICES_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.marketPriceService.saveAll(AggregatedMarketPrices.builder().build()))
                .withMessageContaining(CorePlatformConstants.Validation.MarketPrice.DATA_SOURCE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.marketPriceService.saveAll(AggregatedMarketPrices.builder().dataSource(DataSource.FIRST_RATE_DATA).build()))
                .withMessageContaining(CorePlatformConstants.Validation.MarketPrice.MARKET_PRICE_TIME_INTERVAL_CANNOT_BE_NULL);
    }

    @Test
    void test_saveAll_success() {
        AggregatedMarketPrices prices1 = AggregatedMarketPrices
                .builder()
                .dataSource(DataSource.FIRST_RATE_DATA)
                .interval(MarketPriceTimeInterval.THIRTY_MINUTE)
                .build();

        assertThat(this.marketPriceService.saveAll(prices1)).isEqualTo(-1);

        AggregatedMarketPrices prices2 = AggregatedMarketPrices
                .builder()
                .marketPrices(this.firstRateDataParser.parseMarketPrices("NDX_5min_sample.csv", MarketPriceTimeInterval.FIVE_MINUTE).marketPrices())
                .dataSource(DataSource.FIRST_RATE_DATA)
                .interval(MarketPriceTimeInterval.FIVE_MINUTE)
                .build();

        int count = this.marketPriceService.saveAll(prices2);
        assertThat(count).isEqualTo(790);

        AggregatedMarketPrices prices3 = AggregatedMarketPrices
                .builder()
                .marketPrices(this.firstRateDataParser.parseMarketPrices("NDX_5min_sample.csv", MarketPriceTimeInterval.FIVE_MINUTE).marketPrices())
                .dataSource(DataSource.FIRST_RATE_DATA)
                .interval(MarketPriceTimeInterval.FIVE_MINUTE)
                .build();

        prices3.marketPrices().iterator().next().setClose(9636.36);
        count = this.marketPriceService.saveAll(prices3);
        assertThat(count).isBetween(1, 790);
    }


    //  ----------------- saveAllSet -----------------

    @Test
    void test_saveAllSet_success() {
        AggregatedMarketPrices prices1 = AggregatedMarketPrices
                .builder()
                .dataSource(DataSource.FIRST_RATE_DATA)
                .interval(MarketPriceTimeInterval.THIRTY_MINUTE)
                .build();

        assertThat(this.marketPriceService.saveAllSet(prices1.marketPrices())).isEqualTo(-1);

        AggregatedMarketPrices prices2 = AggregatedMarketPrices
                .builder()
                .marketPrices(this.firstRateDataParser.parseMarketPrices("NDX_5min_sample.csv", MarketPriceTimeInterval.FIVE_MINUTE).marketPrices())
                .dataSource(DataSource.FIRST_RATE_DATA)
                .interval(MarketPriceTimeInterval.FIVE_MINUTE)
                .build();

        int count = this.marketPriceService.saveAllSet(prices2.marketPrices());
        assertThat(count).isEqualTo(790);

        AggregatedMarketPrices prices3 = AggregatedMarketPrices
                .builder()
                .marketPrices(this.firstRateDataParser.parseMarketPrices("NDX_5min_sample.csv", MarketPriceTimeInterval.FIVE_MINUTE).marketPrices())
                .dataSource(DataSource.FIRST_RATE_DATA)
                .interval(MarketPriceTimeInterval.FIVE_MINUTE)
                .build();

        prices3.marketPrices().iterator().next().setClose(9636.36);
        count = this.marketPriceService.saveAllSet(prices3.marketPrices());
        assertThat(count).isBetween(1, 790);
    }

}
