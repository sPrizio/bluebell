package com.bluebell.radicle.services.trade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.system.TradeRecordTimeInterval;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.models.core.nonentities.records.traderecord.controls.TradeRecordControls;
import com.bluebell.AbstractGenericTest;
import com.bluebell.radicle.exceptions.trade.TradeRecordComputationException;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Testing class for {@link TradeRecordService}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class TradeRecordServiceTest extends AbstractGenericTest {

    @Autowired
    private TradeRecordService tradeRecordService;

    @MockitoBean
    private TradeService tradeService;

    @BeforeEach
    public void setUp() {
        Mockito.when(this.tradeService.findAllTradesWithinTimespan(any(), any(), any())).thenReturn(List.of(generateTestBuyTrade(), generateTestSellTrade()));
    }


    //  ----------------- getTradeRecords -----------------

    @Test
    void test_getTradeRecords_missingParamStartDate() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeRecords(null, LocalDate.MAX, generateTestAccount(), TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessage(CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_getTradeRecords_missingParamEndDate() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeRecords(LocalDate.MIN, null, generateTestAccount(), TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessage(CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_getTradeRecords_invalidTimeSpan() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeRecords(LocalDate.MAX, LocalDate.MIN, generateTestAccount(), TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessage(CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    void test_getTradeRecords_missingParamAccount() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeRecords(LocalDate.MIN, LocalDate.MAX, null, TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    void test_getTradeRecords_missingParamTimeInterval() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeRecords(LocalDate.MIN, LocalDate.MAX, generateTestAccount(), null, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessage(CorePlatformConstants.Validation.System.TIME_INTERVAL_CANNOT_BE_NULL);
    }

    @Test
    void test_getTradeRecords_success() {
        assertThat(this.tradeRecordService.getTradeRecords(LocalDate.of(2022, 8, 20), LocalDate.of(2022, 8, 25), generateTestAccount(), TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE).tradeRecords())
                .isNotEmpty()
                .element(0)
                .extracting("lowestPoint", "points", "trades", "profitability")
                .containsExactly(10.35, (15.26 - 3.97), 2, 3.84);
    }


    //  ----------------- getRecentTradeRecords -----------------

    @Test
    void test_getRecentTradeRecords_missingParamTimeInterval() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getRecentTradeRecords(generateTestAccount(), null, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessage(CorePlatformConstants.Validation.System.TIME_INTERVAL_CANNOT_BE_NULL);
    }

    @Test
    void test_getRecentTradeRecords_success() {

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getRecentTradeRecords(null, TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessageContaining(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        final Account empty = generateTestAccount();
        empty.setLastTraded(null);
        assertThat(this.tradeRecordService.getRecentTradeRecords(empty, TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .isNotNull()
                .extracting("tradeRecords")
                .asList()
                .isEmpty();

        empty.setLastTraded(LocalDateTime.now());
        assertThatExceptionOfType(TradeRecordComputationException.class)
                .isThrownBy(() -> this.tradeRecordService.getRecentTradeRecords(empty, TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessageContaining("No trades found for account");

        final Trade test = generateTestBuyTrade();
        test.setTradeCloseTime(null);
        empty.setTrades(new ArrayList<>(List.of(test)));

        assertThatExceptionOfType(TradeRecordComputationException.class)
                .isThrownBy(() -> this.tradeRecordService.getRecentTradeRecords(empty, TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessageContaining("doesn't have any closed trades");

        final Account testAccount = generateTestAccount();
        testAccount.setTrades(new ArrayList<>(List.of(generateTestBuyTrade(), generateTestSellTrade())));

        assertThat(this.tradeRecordService.getRecentTradeRecords(testAccount, TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .extracting("tradeRecords")
                .asList()
                .isNotEmpty();

        assertThat(this.tradeRecordService.getRecentTradeRecords(testAccount, TradeRecordTimeInterval.DAILY, 2))
                .extracting("tradeRecords")
                .asList()
                .isNotEmpty()
                .hasSize(2);
    }


    //  ----------------- getTradeLog -----------------

    @Test
    void test_getTradeRecords_missingParamUser() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeLog(null, LocalDate.MIN, LocalDate.MAX, TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessage(CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);
    }

    @Test
    void test_getTradeLog_missingParamStartDate() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeLog(generateTestUser(), null, LocalDate.MAX, TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessage(CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_getTradeLog_missingParamEndDate() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeLog(generateTestUser(), LocalDate.MIN, null, TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessage(CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_getTradeLog_invalidTimeSpan() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeLog(generateTestUser(), LocalDate.MAX, LocalDate.MIN, TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessage(CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    void test_getTradeLog_missingParamTimeInterval() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeLog(generateTestUser(), LocalDate.MIN, LocalDate.MAX, null, CorePlatformConstants.MAX_RESULT_SIZE))
                .withMessage(CorePlatformConstants.Validation.System.TIME_INTERVAL_CANNOT_BE_NULL);
    }

    @Test
    void test_getTradeLog_success() {

        final User user = generateTestUser();
        final Account testAccount = generateTestAccount();
        testAccount.setTrades(new ArrayList<>(List.of(generateTestBuyTrade(), generateTestSellTrade())));
        user.setAccounts(new ArrayList<>(List.of(testAccount)));

        assertThat(this.tradeRecordService.getTradeLog(user, LocalDate.of(2022, 7, 1), LocalDate.of(2022, 10, 1), TradeRecordTimeInterval.DAILY, CorePlatformConstants.MAX_RESULT_SIZE))
                .extracting("entries")
                .asList()
                .isNotEmpty();
    }


    //  ----------------- getTradeRecordControls -----------------

    @Test
    void test_getTradeRecordControls_success() {

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeRecordControls(null, TradeRecordTimeInterval.DAILY))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.getTradeRecordControls(generateTestAccount(), null))
                .withMessage(CorePlatformConstants.Validation.DataIntegrity.INTERVAL_CANNOT_BE_NULL);

        final Account testAccount = generateTestAccount();
        testAccount.setTrades(new ArrayList<>(List.of(generateTestBuyTrade(), generateTestSellTrade())));

        final TradeRecordControls controls = this.tradeRecordService.getTradeRecordControls(testAccount, TradeRecordTimeInterval.DAILY);
        assertThat(controls)
                .isNotNull()
                .extracting("yearEntries")
                .asList()
                .isNotEmpty()
                .element(0)
                .extracting("monthEntries")
                .asList()
                .isNotEmpty()
                .element(7)
                .extracting("monthNumber", "value")
                .containsExactly(8, 2);
    }
}
