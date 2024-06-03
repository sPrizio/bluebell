package com.bluebell.flowerpot.core.services.account;

import com.bluebell.flowerpot.AbstractGenericTest;
import com.bluebell.flowerpot.core.constants.CoreConstants;
import com.bluebell.flowerpot.core.enums.account.AccountType;
import com.bluebell.flowerpot.core.enums.trade.info.TradeType;
import com.bluebell.flowerpot.core.exceptions.system.EntityCreationException;
import com.bluebell.flowerpot.core.exceptions.validation.IllegalParameterException;
import com.bluebell.flowerpot.core.exceptions.validation.MissingRequiredDataException;
import com.bluebell.flowerpot.core.repositories.account.AccountRepository;
import com.bluebell.flowerpot.core.services.platform.UniqueIdentifierService;
import com.bluebell.flowerpot.core.services.security.UserService;
import com.bluebell.flowerpot.core.services.trade.TradeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Testing class for {@link AccountService}
 *
 * @author Stephen Prizio
 * @version 0.0.3
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountServiceTest extends AbstractGenericTest {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TradeService tradeService;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @MockBean
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.retrieveIdForUid("test")).thenReturn(1L);
        Mockito.when(this.tradeService.findAllByTradeType(TradeType.PROMOTIONAL_PAYMENT, generateTestAccount())).thenReturn(List.of(generateTestBuyTrade()));
        Mockito.when(this.accountRepository.findAccountByAccountNumber(1234L)).thenReturn(generateTestAccount());
        Mockito.when(this.accountRepository.findAccountByAccountNumber(-1L)).thenReturn(null);
        Mockito.when(this.accountRepository.save(any())).thenReturn(generateTestAccount());
        Mockito.when(this.userService.findUserByEmail(anyString())).thenReturn(Optional.of(generateTestUser()));
    }


    //  ----------------- findAccountByAccountNumber -----------------

    @Test
    public void test_findAccountByAccountNumber_success() {
        assertThat(this.accountService.findAccountByAccountNumber(1234L))
                .isNotEmpty();
    }


    //  ----------------- createNewAccount -----------------

    @Test
    public void test_createNewAccount_missingUser() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.createNewAccount(null, null))
                .withMessage(CoreConstants.Validation.Security.User.USER_CANNOT_BE_NULL);
    }

    @Test
    public void test_createNewAccount_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.accountService.createNewAccount(null, generateTestUser()))
                .withMessage("The required data for creating an Account entity was null or empty");
    }

    @Test
    public void test_createNewAccount_erroneousCreation() {
        Map<String, Object> map = Map.of("bad", "input");
        assertThatExceptionOfType(EntityCreationException.class)
                .isThrownBy(() -> this.accountService.createNewAccount(map, generateTestUser()))
                .withMessage("An Account could not be created : Cannot invoke \"java.util.Map.get(Object)\" because \"acc\" is null");
    }

    @Test
    public void test_createNewAccount_success() {

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
                                "tradePlatform", "METATRADER4"
                        )
                );

        assertThat(this.accountService.createNewAccount(data, generateTestUser()))
                .isNotNull()
                .extracting("balance", "accountType", "accountNumber")
                .containsExactly(1000.0, AccountType.CFD, 1234L);
    }
}
