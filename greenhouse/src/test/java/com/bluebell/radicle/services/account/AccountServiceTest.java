package com.bluebell.radicle.services.account;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.account.AccountType;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.api.dto.account.CreateUpdateAccountDTO;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.repositories.account.AccountRepository;
import com.bluebell.radicle.services.security.UserService;
import com.bluebell.radicle.services.trade.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

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
 * @version 0.1.2
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class AccountServiceTest extends AbstractGenericTest {

    @MockitoBean
    private AccountRepository accountRepository;

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
}
