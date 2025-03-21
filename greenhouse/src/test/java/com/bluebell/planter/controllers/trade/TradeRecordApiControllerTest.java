package com.bluebell.planter.controllers.trade;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.constants.ApiConstants;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecordReport;
import com.bluebell.platform.models.core.nonentities.records.traderecord.controls.TradeRecordControls;
import com.bluebell.platform.models.core.nonentities.records.traderecord.controls.TradeRecordControlsYearEntry;
import com.bluebell.radicle.services.trade.TradeRecordService;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Testing class for {@link TradeRecordApiController}
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class TradeRecordApiControllerTest extends AbstractPlanterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TradeRecordService tradeRecordService;

    @BeforeEach
    public void setUp() {
        Mockito.when(this.tradeRecordService.getTradeRecords(any(), any(), any(), any(), anyInt())).thenReturn(TradeRecordReport.builder().tradeRecords(List.of(generateTradeRecord())).tradeRecordTotals(null).build());
        Mockito.when(this.tradeRecordService.getRecentTradeRecords(any(), any(), anyInt())).thenReturn(TradeRecordReport.builder().tradeRecords(List.of(generateTradeRecord())).tradeRecordTotals(null).build());
        Mockito.when(this.tradeRecordService.getTradeRecordControls(any(), any())).thenReturn(TradeRecordControls.builder().yearEntries(List.of(TradeRecordControlsYearEntry.builder().year("2025").monthEntries(Collections.emptyList()).build())).build());
        Mockito.when(this.tradeRecordService.getTradeLog(any(), any(), any(), any(), anyInt())).thenReturn(generateTradeLog());
    }


    //  ----------------- getTradeRecordsWithinInterval -----------------

    @Test
    void test_getTradeRecordsWithinInterval_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("start", List.of("sadadsada"));
        map.put("end", List.of("2022-08-25"));
        map.put("interval", List.of("DAILY"));

        this.mockMvc.perform(get("/api/v1/trade-record/for-interval").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getTradeRecordsWithinInterval_badRequest_enum() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("start", List.of("2022-08-25"));
        map.put("end", List.of("2022-08-05"));
        map.put("interval", List.of("BAD"));

        this.mockMvc.perform(get("/api/v1/trade-record/for-interval").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("BAD was not a valid interval")));
    }

    @Test
    void test_getTradeRecordsWithinInterval_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("start", List.of("2022-08-25"));
        map.put("end", List.of("2022-08-05"));
        map.put("interval", List.of("DAILY"));

        this.mockMvc.perform(get("/api/v1/trade-record/for-interval").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tradeRecords[0].points", is(47.36)))
                .andExpect(jsonPath("$.data.tradeRecords[0].wins", is(9)))
                .andExpect(jsonPath("$.data.tradeRecords[0].lossAverage", is(-74.32)));
    }


    //  ----------------- getRecentTradeRecords -----------------

    @Test
    void test_getRecentTradeRecords_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("interval", List.of("asdada"));

        this.mockMvc.perform(get("/api/v1/trade-record/recent").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(String.format(CorePlatformConstants.Validation.DataIntegrity.INVALID_INTERVAL, "asdada"))));
    }

    @Test
    void test_getRecentTradeRecords_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("interval", List.of("DAILY"));

        this.mockMvc.perform(get("/api/v1/trade-record/recent").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tradeRecords[0].points", is(47.36)))
                .andExpect(jsonPath("$.data.tradeRecords[0].wins", is(9)))
                .andExpect(jsonPath("$.data.tradeRecords[0].lossAverage", is(-74.32)));
    }


    //  ----------------- getTradeRecordControls -----------------

    @Test
    void test_getTradeRecordControls_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("interval", List.of("asdada"));

        this.mockMvc.perform(get("/api/v1/trade-record/trade-record-controls").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(String.format(CorePlatformConstants.Validation.DataIntegrity.INVALID_INTERVAL, "asdada"))));
    }

    @Test
    void test_getTradeRecordControls_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("interval", List.of("DAILY"));

        this.mockMvc.perform(get("/api/v1/trade-record/trade-record-controls").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.yearEntries[0].year", is("2025")));
    }


    //  ----------------- getTradeLog -----------------

    @Test
    void test_getTradeLog_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("start", List.of("sadadsada"));
        map.put("end", List.of("2022-08-25"));
        map.put("interval", List.of("DAILY"));

        this.mockMvc.perform(get("/api/v1/trade-record/trade-log").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getTradeLog_badRequest_enum() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("start", List.of("2022-08-25"));
        map.put("end", List.of("2022-08-05"));
        map.put("interval", List.of("BAD"));

        this.mockMvc.perform(get("/api/v1/trade-record/trade-log").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("BAD was not a valid interval")));
    }

    @Test
    void test_getTradeLog_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));
        map.put("start", List.of("2022-08-25"));
        map.put("end", List.of("2022-08-05"));
        map.put("interval", List.of("DAILY"));

        this.mockMvc.perform(get("/api/v1/trade-record/trade-log").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.entries[0].totals.netPoints", is(89.63)));
    }
}
