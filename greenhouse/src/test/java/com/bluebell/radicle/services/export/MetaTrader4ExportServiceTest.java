package com.bluebell.radicle.services.export;

import com.bluebell.AbstractGenericTest;
import com.bluebell.configuration.BluebellTestConfiguration;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import com.bluebell.platform.util.DirectoryUtil;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.parsers.impl.FirstRateDataParser;
import com.bluebell.radicle.repositories.market.MarketPriceRepository;
import com.bluebell.radicle.services.market.MarketPriceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link MetaTrader4ExportService}
 *
 * @author Stephen Prizio
 * @version 1.0.0
 */
@Import(BluebellTestConfiguration.class)
@SpringBootTest
@RunWith(SpringRunner.class)
class MetaTrader4ExportServiceTest extends AbstractGenericTest {

    private final FirstRateDataParser firstRateDataParser = new FirstRateDataParser(true, "NDX", "/test-data");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MarketPriceRepository marketPriceRepository;

    @Autowired
    private MarketPriceService marketPriceService;

    @Autowired
    private MetaTrader4ExportService metaTrader4ExportService;

    @BeforeEach
    void setUp() {
        this.jdbcTemplate.execute("TRUNCATE TABLE accounts, trades, transactions RESTART IDENTITY CASCADE");
        this.marketPriceRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        final File[] files = new File(DirectoryUtil.getOutputDirectory()).listFiles();
        final List<File> fileList = Arrays.stream(files).filter(f -> f.getName().endsWith(".csv")).toList();
        fileList.forEach(File::delete);
    }


    //  ----------------- exportMarketPrices -----------------

    @Test
    void test_exportMarketPrices_badData() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.metaTrader4ExportService.exportMarketPrices("Test", null, LocalDateTime.MIN, LocalDateTime.MAX, DataSource.FIRST_RATE_DATA))
                .withMessage(CorePlatformConstants.Validation.MarketPrice.MARKET_PRICE_TIME_INTERVAL_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.metaTrader4ExportService.exportMarketPrices(null, MarketPriceTimeInterval.FIVE_MINUTE, LocalDateTime.MIN, LocalDateTime.MAX, DataSource.FIRST_RATE_DATA))
                .withMessage(CorePlatformConstants.Validation.MarketPrice.SYMBOL_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.metaTrader4ExportService.exportMarketPrices("Test", MarketPriceTimeInterval.FIVE_MINUTE, null, LocalDateTime.MAX, DataSource.FIRST_RATE_DATA))
                .withMessage(CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.metaTrader4ExportService.exportMarketPrices("Test", MarketPriceTimeInterval.FIVE_MINUTE, LocalDateTime.MIN, null, DataSource.FIRST_RATE_DATA))
                .withMessage(CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.metaTrader4ExportService.exportMarketPrices("Test", MarketPriceTimeInterval.FIVE_MINUTE, LocalDateTime.MAX, LocalDateTime.MIN, DataSource.FIRST_RATE_DATA))
                .withMessage(CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.metaTrader4ExportService.exportMarketPrices("Test", MarketPriceTimeInterval.FIVE_MINUTE, LocalDateTime.MIN, LocalDateTime.MAX, null))
                .withMessage(CorePlatformConstants.Validation.MarketPrice.DATA_SOURCE_CANNOT_BE_NULL);


        this.metaTrader4ExportService.exportMarketPrices("NDX", MarketPriceTimeInterval.FIVE_MINUTE, LocalDateTime.of(2022, 1, 1, 0, 0, 0), LocalDateTime.of(2023, 1, 1, 0, 0, 0), DataSource.FIRST_RATE_DATA);

    }

    @Test
    void test_exportMarketPrices_success() {

        AggregatedMarketPrices prices2 = AggregatedMarketPrices
                .builder()
                .marketPrices(this.firstRateDataParser.parseMarketPrices("NDX_5min_sample.csv", MarketPriceTimeInterval.FIVE_MINUTE).marketPrices())
                .dataSource(DataSource.FIRST_RATE_DATA)
                .interval(MarketPriceTimeInterval.FIVE_MINUTE)
                .build();

        prices2.marketPrices().forEach(price -> price.setDate(price.getDate().minusYears(2)));

        this.marketPriceService.saveAll(prices2);

        this.metaTrader4ExportService.exportMarketPrices("NDX", MarketPriceTimeInterval.FIVE_MINUTE, LocalDateTime.of(2022, 1, 1, 0, 0, 0), LocalDateTime.of(2023, 1, 1, 0, 0, 0), DataSource.FIRST_RATE_DATA);

        final File[] files = new File(DirectoryUtil.getOutputDirectory()).listFiles();
        assertThat(files).isNotNull();

        final List<File> fileList = Arrays.stream(files).filter(f -> f.getName().endsWith(".csv")).toList();
        assertThat(fileList).hasSize(1);
    }
}
