package com.bluebell.planter.controllers.trade;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.constants.ApiConstants;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.api.dto.trade.CreateUpdateTradeDTO;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.radicle.importing.services.trade.GenericTradeImportService;
import com.bluebell.radicle.services.account.AccountService;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.bluebell.planter.constants.ApiPaths.Trade.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link TradeApiController}
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class TradeApiControllerTest extends AbstractPlanterTest {

    private static final String TEST_ID = "testId1";
    private static final String TRADE_ID = "tradeId";
    private static final String ACCOUNT_NUMBER = "accountNumber";

    private final Account TEST_ACCOUNT = generateTestAccount();

    private final Trade TEST_TRADE_1 = generateTestBuyTrade();
    private final Trade TEST_TRADE_2 = generateTestSellTrade();

    private final MockMultipartFile TEST_FILE = new MockMultipartFile("file", "hello.csv", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
    private final MockMultipartFile TEST_FILE2 = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

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
        Mockito.when(this.tradeService.findTradeByTradeId(TEST_ID, TEST_ACCOUNT)).thenReturn(Optional.of(TEST_TRADE_1));
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.tradeService.findAllTradesWithinTimespan(any(), any(), any(), anyInt(), anyInt(), any())).thenReturn(new PageImpl<>(List.of(generateTestBuyTrade(), generateTestSellTrade()), Pageable.ofSize(10), 10));
        Mockito.when(this.tradeService.findAllTradesForSymbolWithinTimespan(any(), any(), any(), any(), anyInt(), anyInt(), any())).thenReturn(new PageImpl<>(List.of(generateTestBuyTrade(), generateTestSellTrade()), Pageable.ofSize(10), 10));
        Mockito.when(this.tradeService.findAllTradesForTradeTypeWithinTimespan(any(), any(), any(), any(), anyInt(), anyInt(), any())).thenReturn(new PageImpl<>(List.of(generateTestBuyTrade(), generateTestSellTrade()), Pageable.ofSize(10), 10));
        Mockito.when(this.tradeService.findAllTradesForSymbolAndTradeTypeWithinTimespan(any(), any(), any(), any(), any(), anyInt(), anyInt(), any())).thenReturn(new PageImpl<>(List.of(generateTestBuyTrade(), generateTestSellTrade()), Pageable.ofSize(10), 10));
        Mockito.when(this.accountService.findAccountByAccountNumber(1234)).thenReturn(Optional.of(TEST_ACCOUNT));
        Mockito.when(this.accountService.findAccountByAccountNumber(5678)).thenReturn(Optional.empty());
        Mockito.when(this.tradeService.createNewTrade(any(), any())).thenReturn(generateTestSellTrade());
        Mockito.when(this.tradeService.updateTrade(any(), any(), any())).thenReturn(generateTestSellTrade());
    }


    //  ----------------- getTradesForTradeType -----------------

    @Test
    void test_getTradesForTradeType_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("tradeType", List.of("BAD"));

        this.mockMvc.perform(get(getApiPath(BASE, GET_FOR_TYPE)).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("BAD is not a valid trade type")));
    }

    @Test
    void test_getTradesForTradeType_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("tradeType", List.of("BUY"));

        this.mockMvc.perform(get(getApiPath(BASE, GET_FOR_TYPE)).with(testUserContext()).params(map))
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

        this.mockMvc.perform(get(getApiPath(BASE, GET_FOR_INTERVAL)).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getTradesWithinInterval_missingParamEnd() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("start", List.of("2022-08-25T00:00:00"));
        map.put("end", List.of("asdadasdasd"));

        this.mockMvc.perform(get(getApiPath(BASE, GET_FOR_INTERVAL)).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getTradesWithinInterval_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("start", List.of("2022-08-24T00:00:00"));
        map.put("end", List.of("2022-08-25T00:00:00"));

        this.mockMvc.perform(get(getApiPath(BASE, GET_FOR_INTERVAL)).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].openPrice", is(13083.41)))
                .andExpect(jsonPath("$.data[0].closePrice", is(13098.67)))
                .andExpect(jsonPath("$.data[0].netProfit", is(14.85)));
    }


    //  ----------------- getTradesWithinIntervalPaged -----------------

    @Test
    void test_getTradesWithinIntervalPaged_symbol_tradeType_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24T00:00:00"));
        map.put("end", List.of("2022-08-25T00:00:00"));
        map.put("accountNumber", List.of("1234"));
        map.put("page", List.of("0"));
        map.put("symbol", List.of("test_symbol"));
        map.put("tradeType", List.of("BUY"));
        map.put("pageSize", List.of("10"));

        this.mockMvc.perform(get(getApiPath(BASE, GET_FOR_INTERVAL_PAGED)).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.trades[0].openPrice", is(13083.41)))
                .andExpect(jsonPath("$.data.trades[0].closePrice", is(13098.67)))
                .andExpect(jsonPath("$.data.trades[0].netProfit", is(14.85)));
    }

    @Test
    void test_getTradesWithinIntervalPaged__symbol_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24T00:00:00"));
        map.put("end", List.of("2022-08-25T00:00:00"));
        map.put("accountNumber", List.of("1234"));
        map.put("symbol", List.of("test_symbol"));
        map.put("page", List.of("0"));
        map.put("pageSize", List.of("10"));

        this.mockMvc.perform(get(getApiPath(BASE, GET_FOR_INTERVAL_PAGED)).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.trades[0].openPrice", is(13083.41)))
                .andExpect(jsonPath("$.data.trades[0].closePrice", is(13098.67)))
                .andExpect(jsonPath("$.data.trades[0].netProfit", is(14.85)));
    }

    @Test
    void test_getTradesWithinIntervalPaged_tradeType_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24T00:00:00"));
        map.put("end", List.of("2022-08-25T00:00:00"));
        map.put("accountNumber", List.of("1234"));
        map.put("tradeType", List.of("BUY"));
        map.put("page", List.of("0"));
        map.put("pageSize", List.of("10"));

        this.mockMvc.perform(get(getApiPath(BASE, GET_FOR_INTERVAL_PAGED)).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.trades[0].openPrice", is(13083.41)))
                .andExpect(jsonPath("$.data.trades[0].closePrice", is(13098.67)))
                .andExpect(jsonPath("$.data.trades[0].netProfit", is(14.85)));
    }

    @Test
    void test_getTradesWithinIntervalPaged_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24T00:00:00"));
        map.put("end", List.of("2022-08-25T00:00:00"));
        map.put("accountNumber", List.of("1234"));
        map.put("page", List.of("0"));
        map.put("pageSize", List.of("10"));

        this.mockMvc.perform(get(getApiPath(BASE, GET_FOR_INTERVAL_PAGED)).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.trades[0].openPrice", is(13083.41)))
                .andExpect(jsonPath("$.data.trades[0].closePrice", is(13098.67)))
                .andExpect(jsonPath("$.data.trades[0].netProfit", is(14.85)));
    }


    //  ----------------- getTradeForTradeId -----------------

    @Test
    void test_getTradeForTradeId_missingParamTradeId() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put(TRADE_ID, List.of("asdasdad"));
        map.put(ACCOUNT_NUMBER, List.of("1234"));

        this.mockMvc.perform(get(getApiPath(BASE, GET_FOR_TRADE_ID)).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getTradeForTradeId_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put(TRADE_ID, List.of(TEST_ID));
        map.put(ACCOUNT_NUMBER, List.of("1234"));

        this.mockMvc.perform(get(getApiPath(BASE, GET_FOR_TRADE_ID)).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tradeId", is(TEST_ID)));
    }


    //  ----------------- postImportTrades -----------------

    @Test
    void test_postImportTrades_success() throws Exception {
        MockMvc mockMvc1 = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put(ACCOUNT_NUMBER, List.of("1234"));
        map.put("isStrategy", List.of("false"));

        mockMvc1.perform(MockMvcRequestBuilders.multipart(getApiPath(BASE, IMPORT_TRADES)).file(TEST_FILE).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(true)));
    }


    //  ----------------- postCreateNewTrade -----------------

    @Test
    void test_postCreateNewTrade_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post(getApiPath(BASE, CREATE_TRADE))
                        .queryParam(ACCOUNT_NUMBER, "1234")
                        .with(testUserContext())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(Map.of("hello", "world")))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_postCreateNewTrade_missingAccount() throws Exception {

        final CreateUpdateTradeDTO data = CreateUpdateTradeDTO
                .builder()
                .tradeId("123LOL")
                .tradePlatform(TradePlatform.BLUEBELL.getCode())
                .product("Test Equity")
                .tradeType(TradeType.BUY.getCode())
                .closePrice(125.36)
                .tradeCloseTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .tradeOpenTime(LocalDateTime.now().minusMinutes(5).format(DateTimeFormatter.ISO_DATE_TIME))
                .lotSize(0.25)
                .netProfit(89.63)
                .openPrice(102.74)
                .stopLoss(89.55)
                .takeProfit(125.36)
                .build();

        this.mockMvc.perform(post(getApiPath(BASE, CREATE_TRADE))
                        .queryParam(ACCOUNT_NUMBER, "5678")
                        .with(testUserContext())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_postCreateNewTrade_success() throws Exception {

        final CreateUpdateTradeDTO data = CreateUpdateTradeDTO
                .builder()
                .tradeId("123LOL")
                .tradePlatform(TradePlatform.BLUEBELL.getCode())
                .product("Test Equity")
                .tradeType(TradeType.BUY.getCode())
                .closePrice(125.36)
                .tradeCloseTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .tradeOpenTime(LocalDateTime.now().minusMinutes(5).format(DateTimeFormatter.ISO_DATE_TIME))
                .lotSize(0.25)
                .netProfit(89.63)
                .openPrice(102.74)
                .stopLoss(89.55)
                .takeProfit(125.36)
                .build();

        this.mockMvc.perform(post(getApiPath(BASE, CREATE_TRADE))
                        .with(testUserContext())
                        .queryParam(ACCOUNT_NUMBER, "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.netProfit", is(-4.50)));
    }


    //  ----------------- putUpdateTrade -----------------

    @Test
    void test_putUpdateTrade_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(put(getApiPath(BASE, UPDATE_TRADE))
                        .queryParam(ACCOUNT_NUMBER, "1234")
                        .queryParam(TRADE_ID, TEST_ID)
                        .with(testUserContext())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(Map.of("hello", "world")))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_putUpdateTrade_missingAccount() throws Exception {

        final CreateUpdateTradeDTO data = CreateUpdateTradeDTO
                .builder()
                .tradeId("123LOL")
                .tradePlatform(TradePlatform.BLUEBELL.getCode())
                .product("Test Equity")
                .tradeType(TradeType.BUY.getCode())
                .closePrice(125.36)
                .tradeCloseTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .tradeOpenTime(LocalDateTime.now().minusMinutes(5).format(DateTimeFormatter.ISO_DATE_TIME))
                .lotSize(0.25)
                .netProfit(89.63)
                .openPrice(102.74)
                .stopLoss(89.55)
                .takeProfit(125.36)
                .build();

        this.mockMvc.perform(put(getApiPath(BASE, UPDATE_TRADE))
                        .queryParam(ACCOUNT_NUMBER, "5678")
                        .queryParam(TRADE_ID, TEST_ID)
                        .with(testUserContext())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_putUpdateTrade_missingTrade() throws Exception {

        final CreateUpdateTradeDTO data = CreateUpdateTradeDTO
                .builder()
                .tradeId("123LOL")
                .tradePlatform(TradePlatform.BLUEBELL.getCode())
                .product("Test Equity")
                .tradeType(TradeType.BUY.getCode())
                .closePrice(125.36)
                .tradeCloseTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .tradeOpenTime(LocalDateTime.now().minusMinutes(5).format(DateTimeFormatter.ISO_DATE_TIME))
                .lotSize(0.25)
                .netProfit(89.63)
                .openPrice(102.74)
                .stopLoss(89.55)
                .takeProfit(125.36)
                .build();

        this.mockMvc.perform(put(getApiPath(BASE, UPDATE_TRADE))
                        .queryParam(ACCOUNT_NUMBER, "1234")
                        .queryParam(TRADE_ID, "angunfuu")
                        .with(testUserContext())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("No trade was found with trade id: ")));
    }

    @Test
    void test_putUpdateTrade_success() throws Exception {

        final CreateUpdateTradeDTO data = CreateUpdateTradeDTO
                .builder()
                .tradeId("123LOL")
                .tradePlatform(TradePlatform.BLUEBELL.getCode())
                .product("Test Equity")
                .tradeType(TradeType.BUY.getCode())
                .closePrice(125.36)
                .tradeCloseTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .tradeOpenTime(LocalDateTime.now().minusMinutes(5).format(DateTimeFormatter.ISO_DATE_TIME))
                .lotSize(0.25)
                .netProfit(89.63)
                .openPrice(102.74)
                .stopLoss(89.55)
                .takeProfit(125.36)
                .build();

        this.mockMvc.perform(put(getApiPath(BASE, UPDATE_TRADE))
                        .with(testUserContext())
                        .queryParam(ACCOUNT_NUMBER, "1234")
                        .queryParam(TRADE_ID, TEST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.netProfit", is(-4.50)));
    }
}
