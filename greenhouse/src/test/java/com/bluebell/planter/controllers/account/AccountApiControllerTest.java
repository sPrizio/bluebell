package com.bluebell.planter.controllers.account;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.constants.ApiConstants;
import com.bluebell.planter.converters.account.AccountDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.enums.account.AccountType;
import com.bluebell.platform.enums.account.Broker;
import com.bluebell.platform.enums.account.Currency;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.models.api.dto.account.CreateUpdateAccountDTO;
import com.bluebell.platform.models.api.dto.account.CreateUpdateAccountTradingDataDTO;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.radicle.security.constants.SecurityConstants;
import com.bluebell.radicle.services.account.AccountService;
import com.bluebell.radicle.services.portfolio.PortfolioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.bluebell.planter.constants.ApiPaths.Account.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link AccountApiController}
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class AccountApiControllerTest extends AbstractPlanterTest {

    private static final String PORTFOLIO_UID = "portfolioUid";

    @MockitoBean
    private AccountDTOConverter accountDTOConverter;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private PortfolioService portfolioService;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.accountService.createNewAccount(any(), any())).thenReturn(Account.builder().build());
        Mockito.when(this.accountService.updateAccount(any(), any(), any())).thenReturn(Account.builder().build());
        Mockito.when(this.accountDTOConverter.convert(any())).thenReturn(generateTestAccountDTO());
        Mockito.when(this.accountService.findAccountByAccountNumber(1234)).thenReturn(Optional.of(Account.builder().build()));
        Mockito.when(this.accountService.findAccountByAccountNumber(5678)).thenReturn(Optional.empty());
        Mockito.when(this.accountService.deleteAccount(any())).thenReturn(true);
        Mockito.when(this.portfolioService.findPortfolioByUid("1234")).thenReturn(Optional.of(generateTestPortfolio()));
        Mockito.when(this.portfolioService.findPortfolioByUid("5678")).thenReturn(Optional.empty());
        Mockito.when(this.accountService.getAccountDetails(any())).thenReturn(generateAccountDetails());
        Mockito.when(this.accountService.updateAccountTradingData(any())).thenReturn(true);
    }


    //  ----------------- getCurrencies -----------------

    @Test
    void test_getCurrencies_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, CURRENCIES)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is(Currency.values()[0].getIsoCode())));
    }


    //  ----------------- getAccountTypes -----------------

    @Test
    void test_getAccountTypes_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, ACCOUNT_TYPES)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is(AccountType.values()[0].getCode())));
    }


    //  ----------------- getBrokers -----------------

    @Test
    void test_getBrokers_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, BROKERS)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is(Broker.values()[0].getCode())));
    }


    //  ----------------- getTradePlatforms -----------------

    @Test
    void test_getTradePlatforms_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, TRADE_PLATFORMS)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is(TradePlatform.values()[0].getCode())));
    }


    //  ----------------- getDetails -----------------

    @Test
    void test_getDetails_missingAccount() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_DETAILS))
                        .queryParam("accountNumber", "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("No account was found for account number")));
    }

    @Test
    void test_getDetails_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_DETAILS))
                        .queryParam("accountNumber", "1234")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.consistency", is(91)));
    }


    //  ----------------- postCreateNewAccount -----------------

    @Test
    void test_postCreateNewAccount_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post(getApiPath(BASE, CREATE_ACCOUNT))
                        .queryParam(PORTFOLIO_UID, "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(Map.of("hello", "world")))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_postCreateNewAccount_missingPortfolio() throws Exception {

        final CreateUpdateAccountDTO data = CreateUpdateAccountDTO
                .builder()
                .name("Test")
                .active(false)
                .balance(150)
                .number(123L)
                .currency("CAD")
                .type("CFD")
                .broker("CMC_MARKETS")
                .tradePlatform("METATRADER4")
                .build();

        this.mockMvc.perform(post(getApiPath(BASE, CREATE_ACCOUNT))
                        .queryParam(PORTFOLIO_UID, "5678")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Portfolio not found")));
    }

    @Test
    void test_postCreateNewAccount_success() throws Exception {

        final CreateUpdateAccountDTO data = CreateUpdateAccountDTO
                .builder()
                .name("Test")
                .active(false)
                .balance(150)
                .number(123L)
                .currency("CAD")
                .type("CFD")
                .broker("CMC_MARKETS")
                .tradePlatform("METATRADER4")
                .build();

        this.mockMvc.perform(post(getApiPath(BASE, CREATE_ACCOUNT))
                        .requestAttr(SecurityConstants.USER_REQUEST_KEY, generateTestUser())
                        .queryParam(PORTFOLIO_UID, "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.balance", is(1000.0)));
    }


    //  ----------------- postUpdateAccountTradingData -----------------

    @Test
    void test_postUpdateAccountTradingData_success() throws Exception {

        final CreateUpdateAccountTradingDataDTO trades =
                CreateUpdateAccountTradingDataDTO
                        .builder()
                        .userIdentifier("Test")
                        .accountNumber(1234L)
                        .trades(List.of(
                                generateTestCreateUpdateTradeDTO(generateTestBuyTrade()),
                                generateTestCreateUpdateTradeDTO(generateTestSellTrade())
                        ))
                        .transactions(List.of(
                                generateTestCreateUpdateTransactionDTO(generateTestTransactionDeposit(generateTestAccount())),
                                generateTestCreateUpdateTransactionDTO(generateTestTransactionWithdrawal(generateTestAccount()))
                        ))
                        .build();

        this.mockMvc.perform(post(getApiPath(BASE, UPDATE_TRADE_DATA))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(trades))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(true)));
    }


    //  ----------------- putUpdateAccount -----------------

    @Test
    void test_putUpdateAccount_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(put(getApiPath(BASE, UPDATE_ACCOUNT))
                        .queryParam("accountNumber", "5678")
                        .queryParam(PORTFOLIO_UID, "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(Map.of("hello", "world")))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_putUpdateAccount_missingPortfolio() throws Exception {

        final CreateUpdateAccountDTO data = CreateUpdateAccountDTO
                .builder()
                .name("Test")
                .active(false)
                .balance(150)
                .number(123L)
                .currency("CAD")
                .type("CFD")
                .broker("CMC_MARKETS")
                .tradePlatform("METATRADER4")
                .build();

        this.mockMvc.perform(put(getApiPath(BASE, UPDATE_ACCOUNT))
                        .requestAttr(SecurityConstants.USER_REQUEST_KEY, generateTestUser())
                        .queryParam("accountNumber", "5678")
                        .queryParam(PORTFOLIO_UID, "5678")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Portfolio not found")));
    }

    @Test
    void test_putUpdateAccount_missingAccount() throws Exception {

        final CreateUpdateAccountDTO data = CreateUpdateAccountDTO
                .builder()
                .name("Test")
                .active(false)
                .balance(150)
                .number(123L)
                .currency("CAD")
                .type("CFD")
                .broker("CMC_MARKETS")
                .tradePlatform("METATRADER4")
                .build();

        this.mockMvc.perform(put(getApiPath(BASE, UPDATE_ACCOUNT))
                        .requestAttr(SecurityConstants.USER_REQUEST_KEY, generateTestUser())
                        .queryParam("accountNumber", "5678")
                        .queryParam(PORTFOLIO_UID, "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format("No account was found for account number %d", 5678))));
    }

    @Test
    void test_putUpdateAccount_success() throws Exception {

        final CreateUpdateAccountDTO data = CreateUpdateAccountDTO
                .builder()
                .name("Test")
                .active(false)
                .balance(150)
                .number(123L)
                .currency("CAD")
                .type("CFD")
                .broker("CMC_MARKETS")
                .tradePlatform("METATRADER4")
                .build();

        this.mockMvc.perform(put(getApiPath(BASE, UPDATE_ACCOUNT))
                        .requestAttr(SecurityConstants.USER_REQUEST_KEY, generateTestUser())
                        .queryParam("accountNumber", "1234")
                        .queryParam(PORTFOLIO_UID, "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.balance", is(1000.0)));
    }


    //  ----------------- deleteAccount -----------------

    @Test
    void test_deleteAccount_missingAccount() throws Exception {
        this.mockMvc.perform(delete(getApiPath(BASE, DELETE_ACCOUNT))
                        .queryParam("accountNumber", "5678")
                        .queryParam(PORTFOLIO_UID, "5678")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format("No account was found for account number %d", 5678))));
    }

    @Test
    void test_deleteAccount_success() throws Exception {
        this.mockMvc.perform(delete(getApiPath(BASE, DELETE_ACCOUNT))
                        .queryParam("accountNumber", "1234")
                        .queryParam(PORTFOLIO_UID, "5678")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }
}
