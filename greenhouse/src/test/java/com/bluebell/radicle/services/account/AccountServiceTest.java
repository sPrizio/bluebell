package com.bluebell.radicle.services.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.account.AccountType;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.AbstractGenericTest;
import com.bluebell.radicle.exceptions.system.EntityCreationException;
import com.bluebell.radicle.exceptions.system.EntityModificationException;
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

/**
 * Testing class for {@link AccountService}
 *
 * @author Stephen Prizio
 * @version 0.1.0
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
    public void setUp() {
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
    void test_createNewAccount_missingUser() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.createNewAccount(null, null))
                .withMessage(CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);
    }

    @Test
    void test_createNewAccount_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.accountService.createNewAccount(null, generateTestUser()))
                .withMessage("The required data for creating an Account entity was null or empty");
    }

    @Test
    void test_createNewAccount_erroneousCreation() {
        Map<String, Object> map = Map.of("bad", "input");
        assertThatExceptionOfType(EntityCreationException.class)
                .isThrownBy(() -> this.accountService.createNewAccount(map, generateTestUser()))
                .withMessage("An Account could not be created : Cannot invoke \"java.util.Map.get(Object)\" because \"acc\" is null");
    }

    @Test
    void test_createNewAccount_success() {

        Map<String, Object> data =
                Map.of(
                        "account",
                        Map.of(
                                "name", "Test",
                                "number", "123",
                                "balance", "150",
                                "currency", "CAD",
                                "type", "CFD",
                                "broker", "CMC_MARKETS",
                                "dailyStop", "55",
                                "dailyStopType", "POINTS",
                                "tradePlatform", "METATRADER4",
                                "isDefault", "true"
                        )
                );

        assertThat(this.accountService.createNewAccount(data, generateTestUser()))
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
    void test_updateAccount_missingAccount() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.updateAccount(generateTestAccount(), new HashMap<>(), null))
                .withMessage(CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);
    }

    @Test
    void test_updateAccount_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.accountService.updateAccount(generateTestAccount(), null, generateTestUser()))
                .withMessage("The required data for updating an Account was null or empty");
    }

    @Test
    void test_updateAccount_erroneousCreation() {
        Map<String, Object> map = Map.of("bad", "input");
        assertThatExceptionOfType(EntityModificationException.class)
                .isThrownBy(() -> this.accountService.updateAccount(generateTestAccount(), map, generateTestUser()))
                .withMessage("An error occurred while modifying the Account : Cannot invoke \"java.util.Map.get(Object)\" because \"acc\" is null");
    }

    @Test
    void test_updateAccount_success() {

        final Map<String, Object> dataMap = new HashMap<>();
        final Map<String, Object> data = new HashMap<>();
        dataMap.put("name", "Test");
        dataMap.put("active", "true");
        dataMap.put("number", "1234");
        dataMap.put("balance", "1000.0");
        dataMap.put("currency", "CAD");
        dataMap.put("type", "CFD");
        dataMap.put("broker", "CMC_MARKETS");
        dataMap.put("dailyStop", "55");
        dataMap.put("dailyStopType", "POINTS");
        dataMap.put("tradePlatform", "METATRADER4");
        dataMap.put("isDefault", "true");

        data.put("account", dataMap);

        assertThat(this.accountService.updateAccount(generateTestAccount(), data, generateTestUser()))
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
