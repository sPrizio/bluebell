package com.bluebell.radicle.parsers.impl;

import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import com.bluebell.radicle.exceptions.parsing.MetaTrader4ParsingException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link MetaTrader4DataParser}
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
class MetaTrader4DataParserTest {

    private final MetaTrader4DataParser metaTrader4DataParser = new MetaTrader4DataParser(true, "NDAQ100");


    //  ----------------- parseMarketPrices -----------------

    @Test
    void test_parseMarketPrices_badPath() {
        assertThat(this.metaTrader4DataParser.parseMarketPrices(StringUtils.EMPTY, MarketPriceTimeInterval.THIRTY_MINUTE).marketPrices())
                .isEmpty();
    }

    @Test
    void test_parseMarketPrices_failed_badFile() {
        assertThatExceptionOfType(MetaTrader4ParsingException.class)
                .isThrownBy(() -> this.metaTrader4DataParser.parseMarketPrices("NDAQ10030_with_errors.csv", MarketPriceTimeInterval.THIRTY_MINUTE))
                .withMessageContaining("An error occurred while parsing the file. Error:");
    }

    @Test
    void test_parseMarketPrices_success_30min() {
        assertThat(this.metaTrader4DataParser.parseMarketPrices("NDAQ10030.csv", MarketPriceTimeInterval.THIRTY_MINUTE).marketPrices())
                .isNotEmpty()
                .element(25)
                .extracting("close")
                .isEqualTo(17489.88);
    }


    //  ----------------- parseMarketPricesByDate -----------------

    @Test
    void test_parseMarketPricesByDate_failed_emptyDirectory() {
        assertThatExceptionOfType(MetaTrader4ParsingException.class)
                .isThrownBy(() -> this.metaTrader4DataParser.parseMarketPricesByDate(MarketPriceTimeInterval.FIVE_MINUTE))
                .withMessageContaining("An error occurred while retrieving the data root. Likely no data exists for the requested interval of time.");
    }

    @Test
    void test_parseMarketPricesByDate_success_30min() {

        final AggregatedMarketPrices prices =
                this.metaTrader4DataParser.parseMarketPricesByDate(MarketPriceTimeInterval.THIRTY_MINUTE).get(LocalDate.of(2024, 10, 7));

        final MarketPrice marketPrice = prices.marketPrices().first();
        assertThat(marketPrice)
                .extracting("close")
                .isEqualTo(20057.75);
    }

    @Test
    void test_parseMarketPricesByDate_success_1year() {


        assertThatExceptionOfType(MetaTrader4ParsingException.class)
                .isThrownBy(() -> this.metaTrader4DataParser.parseMarketPricesByDate(MarketPriceTimeInterval.ONE_YEAR))
                .withMessageContaining("An error occurred while retrieving the data root. Likely no data exists for the requested interval of time.");
    }
}
