package com.bluebell.radicle.parsers.impl;

import com.bluebell.radicle.enums.RadicleTimeInterval;
import com.bluebell.radicle.models.MarketPrice;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link FirstRateDataParser}
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
class FirstRateDataParserTest {

    private final FirstRateDataParser firstRateDataParser = new FirstRateDataParser(true);


    //  ----------------- parseMarketPrices -----------------

    @Test
    void test_parseMarketPrices_badPath() {
        assertThat(this.firstRateDataParser.parseMarketPrices(StringUtils.EMPTY, RadicleTimeInterval.FIVE_MINUTE))
                .isEmpty();
    }

    @Test
    void test_parseMarketPrices_success_5min() {
        assertThat(this.firstRateDataParser.parseMarketPrices("NDX_5min_sample.csv", RadicleTimeInterval.FIVE_MINUTE))
                .isNotEmpty()
                .element(2)
                .extracting("close")
                .isEqualTo(18179.4);
    }

    @Test
    void test_parseMarketPrices_success_1day() {
        assertThat(this.firstRateDataParser.parseMarketPrices("NDX_1day_sample.csv", RadicleTimeInterval.ONE_DAY))
                .isNotEmpty()
                .element(4)
                .extracting("close")
                .isEqualTo(18546.23);
    }


    //  ----------------- parseMarketPricesByDate -----------------

    @Test
    void test_parseMarketPricesByDate_success() {

        final TreeSet<MarketPrice> prices =
                this.firstRateDataParser.parseMarketPricesByDate(RadicleTimeInterval.FIVE_MINUTE).get(LocalDate.of(2024, 5, 14));

        final MarketPrice marketPrice = prices.first();
        assertThat(marketPrice)
                .extracting("close")
                .isEqualTo(18193.15);
    }
}
