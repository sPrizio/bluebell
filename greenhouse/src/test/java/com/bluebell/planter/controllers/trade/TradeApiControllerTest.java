package com.bluebell.planter.controllers.trade;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.constants.ApiConstants;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.api.dto.trade.CreateUpdateMultipleTradesDTO;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.radicle.importing.services.trade.GenericTradeImportService;
import com.bluebell.radicle.services.trade.TradeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link TradeApiController}
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class TradeApiControllerTest extends AbstractPlanterTest {

    private final Account TEST_ACCOUNT = generateTestAccount();

    private final Trade TEST_TRADE_1 = generateTestBuyTrade();
    private final Trade TEST_TRADE_2 = generateTestSellTrade();

    private final MockMultipartFile TEST_FILE = new MockMultipartFile("file", "hello.csv", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
    private final MockMultipartFile TEST_FILE2 = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenericTradeImportService genericTradeImportService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private TradeService tradeService;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.genericTradeImportService.importTrades(any(), any())).thenReturn(StringUtils.EMPTY);
        Mockito.when(this.tradeService.findAllByTradeType(TradeType.BUY, TEST_ACCOUNT)).thenReturn(List.of(TEST_TRADE_1));
        Mockito.when(this.tradeService.findAllTradesWithinTimespan(any(), any(), any())).thenReturn(List.of(TEST_TRADE_1, TEST_TRADE_2));
        Mockito.when(this.tradeService.findTradeByTradeId("testId1", TEST_ACCOUNT)).thenReturn(Optional.of(TEST_TRADE_1));
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.tradeService.findAllTradesWithinTimespan(any(), any(), any(), anyInt(), anyInt())).thenReturn(new PageImpl<>(List.of(generateTestBuyTrade(), generateTestSellTrade()), Pageable.ofSize(10), 10));
        Mockito.when(this.tradeService.createTrades(any())).thenReturn(true);
    }


    //  ----------------- getTradesForTradeType -----------------

    @Test
    void test_getTradesForTradeType_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("tradeType", List.of("BAD"));

        this.mockMvc.perform(get("/api/v1/trade/for-type").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("BAD is not a valid trade type")));
    }

    @Test
    void test_getTradesForTradeType_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("tradeType", List.of("BUY"));

        this.mockMvc.perform(get("/api/v1/trade/for-type").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].openPrice", is(13083.41)))
                .andExpect(jsonPath("$.data[0].closePrice", is(13098.67)))
                .andExpect(jsonPath("$.data[0].netProfit", is(14.85)));
    }


    //  ----------------- getTradesWithinInterval -----------------

    @Test
    void test_getTradesWithinInterval_missingParamStart() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("start", List.of("dasdfasdfaf"));
        map.put("end", List.of("2022-08-25T00:00:00"));

        this.mockMvc.perform(get("/api/v1/trade/for-interval").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getTradesWithinInterval_missingParamEnd() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("start", List.of("2022-08-25T00:00:00"));
        map.put("end", List.of("asdadasdasd"));

        this.mockMvc.perform(get("/api/v1/trade/for-interval").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getTradesWithinInterval_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("start", List.of("2022-08-24T00:00:00"));
        map.put("end", List.of("2022-08-25T00:00:00"));

        this.mockMvc.perform(get("/api/v1/trade/for-interval").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].openPrice", is(13083.41)))
                .andExpect(jsonPath("$.data[0].closePrice", is(13098.67)))
                .andExpect(jsonPath("$.data[0].netProfit", is(14.85)));
    }


    //  ----------------- getTradesWithinIntervalPaged -----------------

    @Test
    void test_getTradesWithinIntervalPaged_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24T00:00:00"));
        map.put("end", List.of("2022-08-25T00:00:00"));
        map.put("accountNumber", List.of("1234"));
        map.put("page", List.of("0"));
        map.put("pageSize", List.of("10"));

        this.mockMvc.perform(get("/api/v1/trade/for-interval-paged").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.trades[0].openPrice", is(13083.41)))
                .andExpect(jsonPath("$.data.trades[0].closePrice", is(13098.67)))
                .andExpect(jsonPath("$.data.trades[0].netProfit", is(14.85)));
    }


    //  ----------------- getTradeForTradeId -----------------

    @Test
    void test_getTradeForTradeId_missingParamTradeId() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("tradeId", List.of("asdasdad"));
        map.put("accountNumber", List.of("1234"));

        this.mockMvc.perform(get("/api/v1/trade/for-trade-id").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getTradeForTradeId_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("tradeId", List.of("testId1"));
        map.put("accountNumber", List.of("1234"));

        this.mockMvc.perform(get("/api/v1/trade/for-trade-id").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tradeId", is("testId1")));
    }


    //  ----------------- postImportTrades -----------------

    @Test
    void test_postImportTrades_success() throws Exception {
        MockMvc mockMvc1 = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("isStrategy", List.of("false"));

        mockMvc1.perform(MockMvcRequestBuilders.multipart("/api/v1/trade/import-trades").file(TEST_FILE).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(true)));
    }


    //  ----------------- postCreateTrades -----------------

    @Test
    void test_postCreateTrades_success() throws Exception {

        final CreateUpdateMultipleTradesDTO trades =
                CreateUpdateMultipleTradesDTO
                        .builder()
                        .userIdentifier("Test")
                        .accountNumber(1234L)
                        .trades(List.of(
                                generateTestCreateUpdateTradeDTO(generateTestBuyTrade()),
                                generateTestCreateUpdateTradeDTO(generateTestSellTrade())
                        ))
                        .build();

        this.mockMvc.perform(post("/api/v1/trade/create-trades").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(trades)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(true)));
    }
}
