package com.bluebell.planter.controllers.account;

import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.constants.ApiConstants;
import com.bluebell.planter.converters.account.AccountDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.enums.account.AccountType;
import com.bluebell.platform.enums.account.Broker;
import com.bluebell.platform.enums.account.Currency;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.radicle.services.account.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Testing class for {@link AccountApiController}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class AccountApiControllerTest extends AbstractPlanterTest {

    @MockBean
    private AccountDTOConverter accountDTOConverter;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.accountService.createNewAccount(any(), any())).thenReturn(new Account());
        Mockito.when(this.accountService.updateAccount(any(), any(), any())).thenReturn(new Account());
        Mockito.when(this.accountDTOConverter.convert(any())).thenReturn(generateTestAccountDTO());
        Mockito.when(this.accountService.findAccountByAccountNumber(1234)).thenReturn(Optional.of(new Account()));
        Mockito.when(this.accountService.findAccountByAccountNumber(5678)).thenReturn(Optional.empty());
        Mockito.when(this.accountService.deleteAccount(any())).thenReturn(true);
    }


    //  ----------------- getCurrencies -----------------

    @Test
    void test_getCurrencies_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/account/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is(Currency.values()[0].getIsoCode())));
    }


    //  ----------------- getAccountTypes -----------------

    @Test
    void test_getAccountTypes_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/account/account-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is(AccountType.values()[0].getCode())));
    }


    //  ----------------- getBrokers -----------------

    @Test
    void test_getBrokers_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/account/brokers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is(Broker.values()[0].getCode())));
    }


    //  ----------------- getTradePlatforms -----------------

    @Test
    void test_getTradePlatforms_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/account/trade-platforms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is(TradePlatform.values()[0].getCode())));
    }


    //  ----------------- postCreateNewAccount -----------------

    @Test
    void test_postCreateNewAccount_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post("/api/v1/account/create-account").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_postCreateNewAccount_success() throws Exception {

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

        this.mockMvc.perform(post("/api/v1/account/create-account").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.balance", is(1000.0)));
    }


    //  ----------------- putUpdateAccount -----------------

    @Test
    void test_putUpdateAccount_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(put("/api/v1/account/update-account").queryParam("accountNumber", "5678").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_putUpdateAccount_noAccountMatch() throws Exception {

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

        this.mockMvc.perform(put("/api/v1/account/update-account")
                        .queryParam("accountNumber", "5678")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format("No account was found for account number %d", 5678))));
    }

    @Test
    void test_putUpdateAccount_success() throws Exception {

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

        this.mockMvc.perform(put("/api/v1/account/update-account")
                        .queryParam("accountNumber", "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.balance", is(1000.0)));
    }


    //  ----------------- deleteAccount -----------------

    @Test
    void test_deleteAccount_missingAccount() throws Exception {
        this.mockMvc.perform(delete("/api/v1/account/delete-account")
                        .queryParam("accountNumber", "5678")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(String.format("No account was found for account number %d", 5678))));
    }

    @Test
    void test_deleteAccount_success() throws Exception {
        this.mockMvc.perform(delete("/api/v1/account/delete-account")
                        .queryParam("accountNumber", "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }
}
