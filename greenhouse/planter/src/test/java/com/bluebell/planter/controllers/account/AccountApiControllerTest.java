package com.bluebell.planter.controllers.account;

import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.enums.account.AccountType;
import com.bluebell.platform.enums.account.Broker;
import com.bluebell.platform.enums.account.Currency;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.radicle.services.account.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.constants.ApiConstants;
import com.bluebell.planter.converters.account.AccountDTOConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link AccountApiController}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class AccountApiControllerTest extends AbstractPlanterTest {

    @MockBean
    private AccountDTOConverter accountDTOConverter;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        /*Mockito.when(this.accountService.createNewAccount(any(), any())).thenReturn(generateTestAccount());*/
        Mockito.when(this.accountDTOConverter.convert(any())).thenReturn(generateTestAccountDTO());
    }


    //  ----------------- getCurrencies -----------------

    @Test
    public void test_getCurrencies_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/account/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is(Currency.values()[0].getIsoCode())));
    }


    //  ----------------- getAccountTypes -----------------

    @Test
    public void test_getAccountTypes_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/account/account-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is(AccountType.values()[0].getCode())));
    }


    //  ----------------- getBrokers -----------------

    @Test
    public void test_getBrokers_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/account/brokers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is(Broker.values()[0].getCode())));
    }


    //  ----------------- getTradePlatforms -----------------

    @Test
    public void test_getTradePlatforms_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/account/trade-platforms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is(TradePlatform.values()[0].getCode())));
    }


    //  ----------------- postCreateNewAccount -----------------

    @Test
    public void test_postCreateNewAccount_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post("/api/v1/account/create-account").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_postCreateNewAccount_success() throws Exception {

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
}
