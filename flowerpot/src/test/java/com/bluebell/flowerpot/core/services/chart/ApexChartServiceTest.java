package com.bluebell.flowerpot.core.services.chart;

import com.bluebell.flowerpot.AbstractGenericTest;
import com.bluebell.flowerpot.core.constants.CoreConstants;
import com.bluebell.flowerpot.core.enums.chart.IntradayInterval;
import com.bluebell.flowerpot.core.exceptions.validation.IllegalParameterException;
import com.bluebell.flowerpot.core.services.chart.impl.ApexChartService;
import com.bluebell.radicle.models.MarketPrice;
import com.bluebell.radicle.parsers.impl.FirstRateDataParser;
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Testing class for {@link ApexChartService}
 *
 * @author Stephen Prizio
 * @version 0.0.6
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApexChartServiceTest extends AbstractGenericTest {

    private final FirstRateDataParser firstRateDataParser = new FirstRateDataParser();

    @Autowired
    private ApexChartService apexChartService;

    @Before
    public void setUp() throws Exception {

        final TreeSet<MarketPrice> test = new TreeSet<>();
        test.add(new MarketPrice());
        test.add(new MarketPrice());

        Mockito.when(this.firstRateDataParser.parseMarketPricesByDate(anyString(), any())).thenReturn(Map.of(LocalDate.of(2024, 1, 1), test));
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
        assertThatExceptionOfType(IllegalParameterException.class)
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
        assertThat(this.apexChartService.getChartData(LocalDate.MIN, LocalDate.MAX, IntradayInterval.FIVE_MINUTES))
                .hasSize(1)
                .first()
                .extracting("x", "y")
                .contains(Tuple.tuple(13083.41, 13098.67, 14.85), Tuple.tuple(13160.09, 13156.12, -4.50));
    }
}
