package com.bluebell.radicle.services.trade;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.system.TradeRecordTimeInterval;
import com.bluebell.radicle.AbstractGenericTest;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link TradeRecordService}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TradeRecordServiceTest extends AbstractGenericTest {

    @Autowired
    private TradeRecordService tradeRecordService;

    @MockBean
    private TradeService tradeService;

    @Before
    public void setUp() {
        Mockito.when(this.tradeService.findAllTradesWithinTimespan(any(), any(), any())).thenReturn(List.of(generateTestBuyTrade(), generateTestSellTrade()));
    }


    //  ----------------- getTradeRecords -----------------

    @Test
    public void test_getTradeRecords_missingParamStartDate() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeRecords(null, LocalDate.MAX, generateTestAccount(), TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessage(CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
    }

    @Test
    public void test_getTradeRecords_missingParamEndDate() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeRecords(LocalDate.MIN, null, generateTestAccount(), TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessage(CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    public void test_getTradeRecords_invalidTimeSpan() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeRecords(LocalDate.MAX, LocalDate.MIN, generateTestAccount(), TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessage(CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    public void test_getTradeRecords_missingParamAccount() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeRecords(LocalDate.MIN, LocalDate.MAX, null, TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    public void test_getTradeRecords_missingParamTimeInterval() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeRecords(LocalDate.MIN, LocalDate.MAX, generateTestAccount(), null, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessage(CorePlatformConstants.Validation.System.TIME_INTERVAL_CANNOT_BE_NULL);
    }

    @Test
    public void test_getTradeRecords_success() {
        assertThat(this.tradeRecordService.getTradeRecords(LocalDate.of(2022, 8, 20), LocalDate.of(2022, 8, 25), generateTestAccount(), TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE).tradeRecords())
                .isNotEmpty()
                .element(0)
                .extracting("lowestPoint", "points", "trades", "profitability")
                .containsExactly(10.35, (15.26 - 3.97), 2, 3.84);
    }
}
