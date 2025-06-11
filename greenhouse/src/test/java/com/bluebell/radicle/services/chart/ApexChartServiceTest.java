package com.bluebell.radicle.services.chart;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.services.chart.impl.ApexChartService;
import com.bluebell.radicle.services.market.MarketPriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link ApexChartService}
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class ApexChartServiceTest extends AbstractGenericTest {

    @Autowired
    private ApexChartService apexChartService;

    @MockitoBean
    private MarketPriceService marketPriceService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.marketPriceService.findMarketPricesForTrade(any(), any())).thenReturn(List.of(generateTestMarketPrice()));
    }


    //  ----------------- getChartDataForTrade -----------------

    @Test
    void test_getChartData_missingParamTrade() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.apexChartService.getChartDataForTrade(null, MarketPriceTimeInterval.FIVE_MINUTE))
                .withMessage(CorePlatformConstants.Validation.Trade.TRADE_CANNOT_BE_NULL);
    }

    @Test
    void test_getChartData_missingParamInterval() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.apexChartService.getChartDataForTrade(generateTestBuyTrade(), null))
                .withMessage(CorePlatformConstants.Validation.System.TIME_INTERVAL_CANNOT_BE_NULL);
    }

    @Test
    void test_getChartData_success() {
        assertThat(this.apexChartService.getChartDataForTrade(generateTestBuyTrade(), MarketPriceTimeInterval.FIVE_MINUTE))
                .hasSize(1)
                .first()
                .extracting("y")
                .isEqualTo(new double[]{11234.05, 12365.89, 10258.30, 11856.34});
    }
}
