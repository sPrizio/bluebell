package com.bluebell.radicle.parsers.impl;

import com.bluebell.radicle.enums.RadicleTimeInterval;
import com.bluebell.radicle.exceptions.parser.FirstRateDataParsingException;
import com.bluebell.radicle.models.AggregatedMarketPrices;
import com.bluebell.radicle.models.MarketPrice;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.*;

/**
 * Testing class for {@link FirstRateDataParser}
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
class FirstRateDataParserTest {

    private final FirstRateDataParser firstRateDataParser = new FirstRateDataParser(true);


    //  ----------------- parseMarketPrices -----------------

    @Test
    void test_parseMarketPrices_badPath() {
        assertThat(this.firstRateDataParser.parseMarketPrices(StringUtils.EMPTY, RadicleTimeInterval.FIVE_MINUTE).marketPrices())
                .isEmpty();
    }

    @Test
    void test_parseMarketPrices_failed_badFile() {
        assertThatExceptionOfType(FirstRateDataParsingException.class)
                .isThrownBy(() -> this.firstRateDataParser.parseMarketPrices("NDX_1day_sample_with_errors.csv", RadicleTimeInterval.ONE_DAY))
                .withMessageContaining("An error occurred while parsing the file. Error:");
    }

    @Test
    void test_parseMarketPrices_success_5min() {
        assertThat(this.firstRateDataParser.parseMarketPrices("NDX_5min_sample.csv", RadicleTimeInterval.FIVE_MINUTE).marketPrices())
                .isNotEmpty()
                .element(2)
                .extracting("close")
                .isEqualTo(18179.4);
    }

    @Test
    void test_parseMarketPrices_success_1day() {
        assertThat(this.firstRateDataParser.parseMarketPrices("NDX_1day_sample.csv", RadicleTimeInterval.ONE_DAY).marketPrices())
                .isNotEmpty()
                .element(4)
                .extracting("close")
                .isEqualTo(18546.23);
    }


    //  ----------------- parseMarketPricesByDate -----------------

    @Test
    void test_parseMarketPricesByDate_success_1min() {

        final AggregatedMarketPrices prices =
                this.firstRateDataParser.parseMarketPricesByDate(RadicleTimeInterval.ONE_MINUTE).get(LocalDate.of(2024, 5, 14));

        final MarketPrice marketPrice = prices.marketPrices().first();
        assertThat(marketPrice)
                .extracting("close")
                .isEqualTo(18192.54);
    }

    @Test
    void test_parseMarketPricesByDate_success_5min() {

        final AggregatedMarketPrices prices =
                this.firstRateDataParser.parseMarketPricesByDate(RadicleTimeInterval.FIVE_MINUTE).get(LocalDate.of(2024, 5, 14));

        final MarketPrice marketPrice = prices.marketPrices().first();
        assertThat(marketPrice)
                .extracting("close")
                .isEqualTo(18193.15);
    }

    @Test
    void test_parseMarketPricesByDate_success_10min() {

        final AggregatedMarketPrices prices =
                this.firstRateDataParser.parseMarketPricesByDate(RadicleTimeInterval.TEN_MINUTE).get(LocalDate.of(2024, 5, 14));

        final MarketPrice marketPrice = prices.marketPrices().first();
        assertThat(marketPrice)
                .extracting("close")
                .isEqualTo(18200.22);
    }

    @Test
    void test_parseMarketPricesByDate_success_15min() {

        final AggregatedMarketPrices prices =
                this.firstRateDataParser.parseMarketPricesByDate(RadicleTimeInterval.FIFTEEN_MINUTE).get(LocalDate.of(2024, 5, 14));

        final MarketPrice marketPrice = prices.marketPrices().first();
        assertThat(marketPrice)
                .extracting("close")
                .isEqualTo(18217.08);
    }

    @Test
    void test_parseMarketPricesByDate_success_30min() {

        final AggregatedMarketPrices prices =
                this.firstRateDataParser.parseMarketPricesByDate(RadicleTimeInterval.THIRTY_MINUTE).get(LocalDate.of(2024, 5, 14));

        final MarketPrice marketPrice = prices.marketPrices().first();
        assertThat(marketPrice)
                .extracting("close")
                .isEqualTo(18193.02);
    }

    @Test
    void test_parseMarketPricesByDate_success_1hour() {

        final AggregatedMarketPrices prices =
                this.firstRateDataParser.parseMarketPricesByDate(RadicleTimeInterval.ONE_HOUR).get(LocalDate.of(2024, 5, 14));

        final MarketPrice marketPrice = prices.marketPrices().first();
        assertThat(marketPrice)
                .extracting("close")
                .isEqualTo(18193.02);
    }

    @Test
    void test_parseMarketPricesByDate_success_1day() {

        final AggregatedMarketPrices prices =
                this.firstRateDataParser.parseMarketPricesByDate(RadicleTimeInterval.ONE_DAY).get(LocalDate.of(2024, 5, 14));

        final MarketPrice marketPrice = prices.marketPrices().first();
        assertThat(marketPrice)
                .extracting("close")
                .isEqualTo(18322.77);
    }

    @Test
    void test_parseMarketPricesByDate_success_1year() {

        final AggregatedMarketPrices prices =
                this.firstRateDataParser.parseMarketPricesByDate(RadicleTimeInterval.ONE_YEAR).getOrDefault(LocalDate.of(2024, 5, 14), new AggregatedMarketPrices(new TreeSet<>(), RadicleTimeInterval.ONE_YEAR));

        assertThat(prices.marketPrices())
                .isEmpty();
    }
}
