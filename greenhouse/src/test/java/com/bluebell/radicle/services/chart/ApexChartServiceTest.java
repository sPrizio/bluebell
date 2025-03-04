package com.bluebell.radicle.services.chart;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.chart.IntradayInterval;
import com.bluebell.radicle.AbstractGenericTest;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.services.chart.impl.ApexChartService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Testing class for {@link ApexChartService}
 *
 * @author Stephen Prizio
 * @version 0.1.0
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
                .isThrownBy(() -> this.apexChartService.getChartData(null, LocalDate.MAX, IntradayInterval.FIVE_MINUTES))
                .withMessage(CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_getChartData_missingParamEnd() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.apexChartService.getChartData(LocalDate.MIN, null, IntradayInterval.FIVE_MINUTES))
                .withMessage(CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_getChartData_badDates() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.apexChartService.getChartData(LocalDate.MAX, LocalDate.MIN, IntradayInterval.FIVE_MINUTES))
                .withMessage(CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    void test_getChartData_missingParamInterval() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.apexChartService.getChartData(LocalDate.MIN, LocalDate.MIN, null))
                .withMessage("timeInterval cannot be null");
    }

    @Test
    void test_getChartData_success() {
        this.apexChartService.setTest(true);
        assertThat(this.apexChartService.getChartData(LocalDate.MIN, LocalDate.MAX, IntradayInterval.FIVE_MINUTES))
                .hasSize(790)
                .first()
                .extracting("x", "y")
                .contains(1715607000000L, new double[]{18228.4, 18233.5, 18204.06, 18204.16});
        this.apexChartService.setTest(false);
    }
}
