package com.bluebell.radicle.parsers.impl;

import com.bluebell.radicle.enums.RadicleTimeInterval;
import com.bluebell.radicle.exceptions.parser.TradingViewDataParsingException;
import com.bluebell.radicle.models.AggregatedMarketPrices;
import com.bluebell.radicle.models.MarketPrice;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link TradingViewDataParser}
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
class TradingViewDataParserTest {

    private final TradingViewDataParser tradingViewDataParser = new TradingViewDataParser(true, "US100");


    //  ----------------- parseMarketPrices -----------------

    @Test
    void test_parseMarketPrices_badPath() {
        assertThat(this.tradingViewDataParser.parseMarketPrices(StringUtils.EMPTY, RadicleTimeInterval.THIRTY_MINUTE).marketPrices())
                .isEmpty();
    }

    @Test
    void test_parseMarketPrices_failed_badFile() {
        assertThatExceptionOfType(TradingViewDataParsingException.class)
                .isThrownBy(() -> this.tradingViewDataParser.parseMarketPrices("CFI_US100-30_8a062_with_errors.csv", RadicleTimeInterval.THIRTY_MINUTE))
                .withMessageContaining("An error occurred while parsing the file. Error:");
    }

    @Test
    void test_parseMarketPrices_success_30min() {
        assertThat(this.tradingViewDataParser.parseMarketPrices("CFI_US100-30_8a062.csv", RadicleTimeInterval.THIRTY_MINUTE).marketPrices())
                .isNotEmpty()
                .element(2)
                .extracting("close")
                .isEqualTo(19611.08);
    }


    //  ----------------- parseMarketPricesByDate -----------------

    @Test
    void test_parseMarketPricesByDate_failed_emptyDirectory() {
        assertThatExceptionOfType(TradingViewDataParsingException.class)
                .isThrownBy(() -> this.tradingViewDataParser.parseMarketPricesByDate(RadicleTimeInterval.FIVE_MINUTE))
                .withMessageContaining("Directory FIVE_MINUTE was empty!");
    }

    @Test
    void test_parseMarketPricesByDate_success_30min() {

        final AggregatedMarketPrices prices =
                this.tradingViewDataParser.parseMarketPricesByDate(RadicleTimeInterval.THIRTY_MINUTE).get(LocalDate.of(2024, 7, 22));

        final MarketPrice marketPrice = prices.marketPrices().first();
        assertThat(marketPrice)
                .extracting("close")
                .isEqualTo(19598.83);
    }

    @Test
    void test_parseMarketPricesByDate_success_1year() {


        assertThatExceptionOfType(TradingViewDataParsingException.class)
                .isThrownBy(() -> this.tradingViewDataParser.parseMarketPricesByDate(RadicleTimeInterval.ONE_YEAR))
                .withMessageContaining("An error occurred while retrieving the data root. Likely no data exists for the requested interval of time.");
    }
}
