package com.bluebell.planter.core.services.trade;

import com.bluebell.planter.AbstractGenericTest;
import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.system.FlowerpotTimeInterval;
import com.bluebell.planter.core.exceptions.validation.IllegalParameterException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link TradeRecordService}
 *
 * @author Stephen Prizio
 * @version 0.0.7
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
                .isThrownBy(() -> this.tradeRecordService.getTradeRecords(null, LocalDate.MAX, generateTestAccount(), FlowerpotTimeInterval.DAILY, CoreConstants.MAX_RESULT_SIZE))
                .withMessage(CoreConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
    }

    @Test
    public void test_getTradeRecords_missingParamEndDate() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeRecords(LocalDate.MIN, null, generateTestAccount(), FlowerpotTimeInterval.DAILY, CoreConstants.MAX_RESULT_SIZE))
                .withMessage(CoreConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    public void test_getTradeRecords_invalidTimeSpan() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeRecords(LocalDate.MAX, LocalDate.MIN, generateTestAccount(), FlowerpotTimeInterval.DAILY, CoreConstants.MAX_RESULT_SIZE))
                .withMessage(CoreConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    public void test_getTradeRecords_missingParamAccount() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeRecords(LocalDate.MIN, LocalDate.MAX, null, FlowerpotTimeInterval.DAILY, CoreConstants.MAX_RESULT_SIZE))
                .withMessage(CoreConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    public void test_getTradeRecords_missingParamTimeInterval() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeRecords(LocalDate.MIN, LocalDate.MAX, generateTestAccount(), null, CoreConstants.MAX_RESULT_SIZE))
                .withMessage(CoreConstants.Validation.System.TIME_INTERVAL_CANNOT_BE_NULL);
    }

    @Test
    public void test_getTradeRecords_success() {
        assertThat(this.tradeRecordService.getTradeRecords(LocalDate.of(2022, 8, 20), LocalDate.of(2022, 8, 25), generateTestAccount(), FlowerpotTimeInterval.DAILY, CoreConstants.MAX_RESULT_SIZE).tradeRecords())
                .isNotEmpty()
                .element(0)
                .extracting("lowestPoint", "points", "trades", "profitability")
                .containsExactly(10.35, (15.26 - 3.97), 2, 3.84);
    }
}
