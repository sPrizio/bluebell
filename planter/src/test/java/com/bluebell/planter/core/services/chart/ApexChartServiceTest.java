package com.bluebell.planter.core.services.chart;

import com.bluebell.planter.AbstractGenericTest;
import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.chart.IntradayInterval;
import com.bluebell.planter.core.exceptions.validation.IllegalParameterException;
import com.bluebell.planter.core.services.chart.impl.ApexChartService;
import org.junit.Before;
import org.junit.Test;
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
 * @version 0.0.7
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApexChartServiceTest extends AbstractGenericTest {

    @Autowired
    private ApexChartService apexChartService;

    @Before
    public void setUp() throws Exception {
    }


    //  ----------------- getChartData -----------------

    @Test
    public void test_getChartData_missingParamStart() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.apexChartService.getChartData(null, LocalDate.MAX, IntradayInterval.FIVE_MINUTES))
                .withMessage(CoreConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
    }

    @Test
    public void test_getChartData_missingParamEnd() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.apexChartService.getChartData(LocalDate.MIN, null, IntradayInterval.FIVE_MINUTES))
                .withMessage(CoreConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    public void test_getChartData_badDates() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.apexChartService.getChartData(LocalDate.MAX, LocalDate.MIN, IntradayInterval.FIVE_MINUTES))
                .withMessage(CoreConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    public void test_getChartData_missingParamInterval() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.apexChartService.getChartData(LocalDate.MIN, LocalDate.MIN, null))
                .withMessage("timeInterval cannot be null");
    }

    @Test
    public void test_getChartData_success() {
        this.apexChartService.setTest(true);
        assertThat(this.apexChartService.getChartData(LocalDate.MIN, LocalDate.MAX, IntradayInterval.FIVE_MINUTES))
                .hasSize(790)
                .first()
                .extracting("x", "y")
                .contains(1715607000000L, new double[]{18228.4, 18233.5, 18204.06, 18204.16});
        this.apexChartService.setTest(false);
    }
}
