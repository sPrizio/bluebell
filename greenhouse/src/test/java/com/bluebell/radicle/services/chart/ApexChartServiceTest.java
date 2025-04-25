package com.bluebell.radicle.services.chart;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.services.chart.impl.ApexChartService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link ApexChartService}
 *
 * @author Stephen Prizio
 * @version 0.1.7
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class ApexChartServiceTest extends AbstractGenericTest {

    @Autowired
    private ApexChartService apexChartService;


    //  ----------------- getChartData -----------------

    @Test
    void test_getChartData_missingParamStart() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.apexChartService.getChartData(null, LocalDate.MAX, MarketPriceTimeInterval.FIVE_MINUTE, "Test", DataSource.FIRST_RATE_DATA))
                .withMessage(CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_getChartData_missingParamEnd() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.apexChartService.getChartData(LocalDate.MIN, null, MarketPriceTimeInterval.FIVE_MINUTE, "Test", DataSource.FIRST_RATE_DATA))
                .withMessage(CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_getChartData_badDates() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.apexChartService.getChartData(LocalDate.MAX, LocalDate.MIN, MarketPriceTimeInterval.FIVE_MINUTE, "Test", DataSource.FIRST_RATE_DATA))
                .withMessage(CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    void test_getChartData_missingParamInterval() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.apexChartService.getChartData(LocalDate.MIN, LocalDate.MIN, null, "Test", DataSource.FIRST_RATE_DATA))
                .withMessage("timeInterval cannot be null");
    }

    @Test
    void test_getChartData_missingParamSymbol() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.apexChartService.getChartData(LocalDate.MIN, LocalDate.MIN, MarketPriceTimeInterval.FIVE_MINUTE, null, DataSource.FIRST_RATE_DATA))
                .withMessage("symbol cannot be null");
    }

    @Test
    void test_getChartData_missingParamDataSource() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.apexChartService.getChartData(LocalDate.MIN, LocalDate.MIN, MarketPriceTimeInterval.FIVE_MINUTE, "Test", null))
                .withMessage(CorePlatformConstants.Validation.MarketPrice.DATA_SOURCE_CANNOT_BE_NULL);
    }

    @Test
    void test_getChartData_firstRateData_success() {
        this.apexChartService.setTest(true);
        assertThat(this.apexChartService.getChartData(LocalDate.MIN, LocalDate.MAX, MarketPriceTimeInterval.FIVE_MINUTE, "NDX", DataSource.FIRST_RATE_DATA))
                .hasSize(790)
                .first()
                .extracting("x", "y")
                .contains(1715607000000L, new double[]{18228.4, 18233.5, 18204.06, 18204.16});
        this.apexChartService.setTest(false);
    }

    @Test
    void test_getChartData_tradingView_success() {
        this.apexChartService.setTest(true);
        assertThat(this.apexChartService.getChartData(LocalDate.MIN, LocalDate.MAX, MarketPriceTimeInterval.THIRTY_MINUTE, "US100", DataSource.TRADING_VIEW))
                .isNotEmpty();
        this.apexChartService.setTest(false);
    }

    @Test
    void test_getChartData_mt4_success() {
        this.apexChartService.setTest(true);
        assertThat(this.apexChartService.getChartData(LocalDate.MIN, LocalDate.MAX, MarketPriceTimeInterval.THIRTY_MINUTE, "NDAQ100", DataSource.METATRADER4))
                .isNotEmpty();
        this.apexChartService.setTest(false);
    }
}
