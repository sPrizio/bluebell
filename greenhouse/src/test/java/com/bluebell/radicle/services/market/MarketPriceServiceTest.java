package com.bluebell.radicle.services.market;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.parsers.impl.FirstRateDataParser;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

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
    private MarketPriceService marketPriceService;


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

        assertThat(this.marketPriceService.saveAll(prices1))
                .asInstanceOf(InstanceOfAssertFactories.SET)
                .isEmpty();

        AggregatedMarketPrices prices2 = AggregatedMarketPrices
                .builder()
                .marketPrices(this.firstRateDataParser.parseMarketPrices("NDX_5min_sample.csv", MarketPriceTimeInterval.FIVE_MINUTE).marketPrices())
                .dataSource(DataSource.FIRST_RATE_DATA)
                .interval(MarketPriceTimeInterval.FIVE_MINUTE)
                .build();

        final Set<MarketPrice> result = this.marketPriceService.saveAll(prices2);
        assertThat(result)
                .isNotEmpty()
                .element(2)
                .extracting("close")
                .isEqualTo(18179.4);

        assertThat(result)
                .element(2)
                .extracting("id")
                .isNotNull();
    }
}
