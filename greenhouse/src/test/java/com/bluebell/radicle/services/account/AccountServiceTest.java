package com.bluebell.radicle.services.account;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.account.AccountType;
import com.bluebell.platform.enums.system.TradeRecordTimeInterval;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.api.dto.account.CreateUpdateAccountDTO;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.models.core.nonentities.account.AccountBalanceHistory;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecordReport;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecordTotals;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.repositories.account.AccountRepository;
import com.bluebell.radicle.services.security.UserService;
import com.bluebell.radicle.services.trade.TradeRecordService;
import com.bluebell.radicle.services.trade.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Testing class for {@link AccountService}
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class AccountServiceTest extends AbstractGenericTest {

    @MockitoBean
    private AccountRepository accountRepository;

    @MockitoBean
    private TradeRecordService tradeRecordService;

    @MockitoBean
    private TradeService tradeService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.tradeService.findAllByTradeType(TradeType.PROMOTIONAL_PAYMENT, generateTestAccount())).thenReturn(List.of(generateTestBuyTrade()));
        Mockito.when(this.accountRepository.findAccountByAccountNumber(1234L)).thenReturn(generateTestAccount());
        Mockito.when(this.accountRepository.findAccountByAccountNumber(-1L)).thenReturn(null);
        Mockito.when(this.accountRepository.save(any())).thenReturn(generateTestAccount());
        Mockito.when(this.userService.findUserByEmail(anyString())).thenReturn(Optional.of(generateTestUser()));
    }


    //  ----------------- getAccountDetails -----------------

    @Test
    void test_getAccountDetails_success() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.getAccountDetails(null))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        assertThat(this.accountService.getAccountDetails(generateTestAccount())).isNotNull();
    }


    //  ----------------- findAccountByAccountNumber -----------------

    @Test
    void test_findAccountByAccountNumber_success() {
        assertThat(this.accountService.findAccountByAccountNumber(1234L))
                .isNotEmpty();
    }


    //  ----------------- createNewAccount -----------------

    @Test
    void test_createNewAccount_missingPortfolio() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.createNewAccount(null, null))
                .withMessage(CorePlatformConstants.Validation.Portfolio.PORTFOLIO_CANNOT_BE_NULL);
    }

    @Test
    void test_createNewAccount_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.accountService.createNewAccount(null, generateTestPortfolio()))
                .withMessage("The required data for creating an Account entity was null or empty");
    }

    @Test
    void test_createNewAccount_success() {

        final CreateUpdateAccountDTO data = CreateUpdateAccountDTO
                .builder()
                .name("Test")
                .active(false)
                .balance(150)
                .number(1234L)
                .currency("CAD")
                .type("CFD")
                .broker("CMC_MARKETS")
                .tradePlatform("METATRADER4")
                .build();

        assertThat(this.accountService.createNewAccount(data, generateTestPortfolio()))
                .isNotNull()
                .extracting("balance", "accountType", "accountNumber")
                .containsExactly(1000.0, AccountType.CFD, 1234L);
    }

    @Test
    void test_createNewLegacyAccount_success() {

        final CreateUpdateAccountDTO data = CreateUpdateAccountDTO
                .builder()
                .name("Test")
                .active(false)
                .isLegacy(true)
                .accountOpenTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .accountCloseTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .balance(150)
                .number(1234L)
                .currency("CAD")
                .type("CFD")
                .broker("CMC_MARKETS")
                .tradePlatform("METATRADER4")
                .build();

        assertThat(this.accountService.createNewAccount(data, generateTestPortfolio()))
                .isNotNull()
                .extracting("balance", "accountType", "accountNumber")
                .containsExactly(1000.0, AccountType.CFD, 1234L);
    }


    //  ----------------- updateAccount -----------------

    @Test
    void test_updateAccount_missingUser() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.updateAccount(null, null, null))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    void test_updateAccount_missingPortfolio() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.updateAccount(generateTestAccount(), CreateUpdateAccountDTO.builder().build(), null))
                .withMessage(CorePlatformConstants.Validation.Portfolio.PORTFOLIO_CANNOT_BE_NULL);
    }

    @Test
    void test_updateAccount_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.accountService.updateAccount(generateTestAccount(), null, generateTestPortfolio()))
                .withMessage("The required data for updating an Account was null or empty");
    }

    @Test
    void test_updateAccount_success() {

        final CreateUpdateAccountDTO data = CreateUpdateAccountDTO
                .builder()
                .name("Test")
                .active(false)
                .balance(150)
                .number(1234L)
                .currency("CAD")
                .type("CFD")
                .broker("CMC_MARKETS")
                .tradePlatform("METATRADER4")
                .build();

        assertThat(this.accountService.updateAccount(generateTestAccount(), data, generateTestPortfolio()))
                .isNotNull()
                .extracting("balance", "accountType", "accountNumber")
                .containsExactly(1000.0, AccountType.CFD, 1234L);
    }


    //  ----------------- deleteAccount -----------------

    @Test
    void test_deleteAccount_success() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.deleteAccount(null))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        assertThat(this.accountService.deleteAccount(generateTestAccount())).isTrue();
    }


    //  ----------------- generateAccountBalanceHistory -----------------

    @Test
    void test_generateAccountBalanceHistory_missingData() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.generateAccountBalanceHistory(null, null))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.generateAccountBalanceHistory(generateTestAccount(), null))
                .withMessage(CorePlatformConstants.Validation.System.TIME_INTERVAL_CANNOT_BE_NULL);

        final Account account = generateTestAccount();
        account.setActive(false);
        assertThat(this.accountService.generateAccountBalanceHistory(account, TradeRecordTimeInterval.MONTHLY)).isEmpty();

        account.setAccountCloseTime(LocalDateTime.MAX);
        assertThat(this.accountService.generateAccountBalanceHistory(account, TradeRecordTimeInterval.MONTHLY)).isEmpty();
    }

    @Test
    void test_generateAccountBalanceHistory_success() {
        final Account account = generateTestAccount();
        account.setActive(true);
        account.setAccountCloseTime(null);

        account.setTrades(List.of(generateTestBuyTrade(), generateTestSellTrade()));

        final Trade trade = account.getTrades().get(1);
        account.setAccountOpenTime(LocalDate.of(2022, 8, 24).atStartOfDay());
        account.getTrades().get(1).setTradeOpenTime(trade.getTradeOpenTime().plusDays(2));
        account.getTrades().get(1).setTradeCloseTime(trade.getTradeCloseTime().plusDays(2));

        Mockito.when(this.tradeRecordService.getTradeRecords(
                Mockito.any(LocalDate.class),
                Mockito.any(LocalDate.class),
                Mockito.eq(account),
                Mockito.eq(TradeRecordTimeInterval.DAILY),
                Mockito.eq(-1)
        )).thenReturn(TradeRecordReport.builder()
                .tradeRecordTotals(TradeRecordTotals.builder().netProfit(0.0).build())
                .build());
        Mockito.when(this.tradeRecordService.getTradeRecords(LocalDate.of(2022, 8, 24), LocalDate.of(2022, 8, 25), account, TradeRecordTimeInterval.DAILY, -1)).thenReturn(TradeRecordReport.builder().tradeRecordTotals(TradeRecordTotals.builder().netProfit(14.85).build()).build());
        Mockito.when(this.tradeRecordService.getTradeRecords(LocalDate.of(2022, 8, 26), LocalDate.of(2022, 8, 27), account, TradeRecordTimeInterval.DAILY, -1)).thenReturn(TradeRecordReport.builder().tradeRecordTotals(TradeRecordTotals.builder().netProfit(-4.5).build()).build());

        final List<AccountBalanceHistory> daily = this.accountService.generateAccountBalanceHistory(account, TradeRecordTimeInterval.DAILY);
        assertThat(daily).isNotEmpty();
        assertThat(daily.get(0).delta()).isEqualTo(14.85);
        assertThat(daily.get(1).delta()).isEqualTo(0.0);
        assertThat(daily.get(2).delta()).isEqualTo(-4.5);

        Mockito.when(this.tradeRecordService.getTradeRecords(
                Mockito.any(LocalDate.class),
                Mockito.any(LocalDate.class),
                Mockito.eq(account),
                Mockito.eq(TradeRecordTimeInterval.MONTHLY),
                Mockito.eq(-1)
        )).thenReturn(TradeRecordReport.builder()
                .tradeRecordTotals(TradeRecordTotals.builder().netProfit(0.0).build())
                .build());
        Mockito.when(this.tradeRecordService.getTradeRecords(LocalDate.of(2022, 8, 1), LocalDate.of(2022, 9, 1), account, TradeRecordTimeInterval.MONTHLY, -1)).thenReturn(TradeRecordReport.builder().tradeRecordTotals(TradeRecordTotals.builder().netProfit(10.35).build()).build());


        final List<AccountBalanceHistory> monthly = this.accountService.generateAccountBalanceHistory(account, TradeRecordTimeInterval.MONTHLY);
        assertThat(monthly).isNotEmpty();
        assertThat(monthly.get(0).delta()).isEqualTo(10.35);
        assertThat(monthly.get(1).delta()).isEqualTo(0.0);
    }
}
