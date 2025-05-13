package com.bluebell.planter.controllers.symbol;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.radicle.services.account.AccountService;
import com.bluebell.radicle.services.symbol.SymbolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static com.bluebell.planter.constants.ApiPaths.Symbol.BASE;
import static com.bluebell.planter.constants.ApiPaths.Symbol.GET_TRADED_SYMBOLS;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link SymbolApiController}
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class SymbolApiControllerTest extends AbstractPlanterTest {

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private SymbolService symbolService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Mockito.when(this.accountService.findAccountByAccountNumber(anyLong())).thenReturn(Optional.of(generateTestAccount()));
        Mockito.when(this.symbolService.getTradedSymbolsForAccount(any())).thenReturn(new TreeSet<>(Set.of("Test 1", "Test 2", "Test 3")));
    }


    //  ----------------- getTradedSymbols -----------------

    @Test
    void test_getTradedSymbols_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, GET_TRADED_SYMBOLS)).queryParam("accountNumber", "1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data[0]", is("Test 1")))
                .andExpect(jsonPath("$.data[1]", is("Test 2")))
                .andExpect(jsonPath("$.data[2]", is("Test 3")));
    }
}
