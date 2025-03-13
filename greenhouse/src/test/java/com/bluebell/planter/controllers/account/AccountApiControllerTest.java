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
import com.bluebell.platform.models.api.dto.account.CreateUpdateAccountDTO;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Testing class for {@link AccountApiController}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class AccountApiControllerTest extends AbstractPlanterTest {

    @MockitoBean
    private AccountDTOConverter accountDTOConverter;

    @MockitoBean
    private AccountService accountService;

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
        Mockito.when(this.accountService.getAccountDetails(any())).thenReturn(generateAccountDetails());
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


    //  ----------------- getDetails -----------------

    @Test
    void test_getDetails_missingAccount() throws Exception {
        this.mockMvc.perform(get("/api/v1/account/get-details")
                        .queryParam("accountNumber", "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("No account was found for account number")));
    }

    @Test
    void test_getDetails_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/account/get-details")
                        .queryParam("accountNumber", "1234")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.consistency", is(91)));
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
