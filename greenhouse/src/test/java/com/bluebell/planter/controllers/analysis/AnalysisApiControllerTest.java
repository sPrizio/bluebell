package com.bluebell.planter.controllers.analysis;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.constants.ApiConstants;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.nonentities.records.analysis.AnalysisResult;
import com.bluebell.radicle.services.account.AccountService;
import com.bluebell.radicle.services.analysis.AnalysisService;
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

import java.util.List;
import java.util.Optional;

import static com.bluebell.planter.constants.ApiPaths.Analysis.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link AnalysisApiController}
 *
 * @author Stephen Prizio
 * @version 0.1.9
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class AnalysisApiControllerTest extends AbstractPlanterTest {

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private AnalysisService analysisService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Mockito.when(this.accountService.findAccountByAccountNumber(1234)).thenReturn(Optional.of(Account.builder().build()));
        Mockito.when(this.accountService.findAccountByAccountNumber(5678)).thenReturn(Optional.empty());
        Mockito.when(this.analysisService.computeTimeBucketAnalysis(any(), any(), any(), anyBoolean())).thenReturn(List.of(AnalysisResult.builder().label("Test").value(91.0).count(1).build()));
        Mockito.when(this.analysisService.computeWeekdayAnalysis(any(), any())).thenReturn(List.of(AnalysisResult.builder().label("Test").value(91.0).count(1).build()));
        Mockito.when(this.analysisService.computeTradeDurationAnalysis(any(), any(), any())).thenReturn(List.of(AnalysisResult.builder().label("Test").value(91.0).count(1).build()));
        Mockito.when(this.analysisService.computeWeekdayTimeBucketAnalysis(any(), any(), any(), any())).thenReturn(List.of(AnalysisResult.builder().label("Test").value(91.0).count(1).build()));
    }


    //  ----------------- getTimeBucketsAnalysis -----------------

    @Test
    void test_getTimeBucketsAnalysis_badFilter() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, TIME_BUCKETS))
                        .queryParam("accountNumber", "5678")
                        .queryParam("filter", "bad_filter")
                        .queryParam("isOpened", "true")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getTimeBucketsAnalysis_missingAccount() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, TIME_BUCKETS))
                        .queryParam("accountNumber", "5678")
                        .queryParam("filter", "PROFIT")
                        .queryParam("isOpened", "true")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(CorePlatformConstants.Validation.Account.ACCOUNT_NOT_FOUND)));
    }

    @Test
    void test_getTimeBucketsAnalysis_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, TIME_BUCKETS))
                        .queryParam("accountNumber", "1234")
                        .queryParam("filter", "PROFIT")
                        .queryParam("isOpened", "true")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].value", is(91.0)));
    }


    //  ----------------- getWeekdaysAnalysis -----------------

    @Test
    void test_getWeekdaysAnalysis_badFilter() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, WEEKDAYS))
                        .queryParam("accountNumber", "5678")
                        .queryParam("filter", "bad_filter")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getWeekdaysAnalysis_missingAccount() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, WEEKDAYS))
                        .queryParam("accountNumber", "5678")
                        .queryParam("filter", "PROFIT")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(CorePlatformConstants.Validation.Account.ACCOUNT_NOT_FOUND)));
    }

    @Test
    void test_getWeekdaysAnalysis_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, WEEKDAYS))
                        .queryParam("accountNumber", "1234")
                        .queryParam("filter", "PROFIT")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].value", is(91.0)));
    }


    //  ----------------- getWeekdayTimeBucketsAnalysis -----------------

    @Test
    void test_getWeekdayTimeBucketsAnalysis_badFilter() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, WEEKDAYS_TIME_BUCKETS))
                        .queryParam("accountNumber", "5678")
                        .queryParam("filter", "bad_filter")
                        .queryParam("weekday", "MONDAY")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getWeekdayTimeBucketsAnalysis_badWeekday() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, WEEKDAYS_TIME_BUCKETS))
                        .queryParam("accountNumber", "5678")
                        .queryParam("filter", "PROFIT")
                        .queryParam("weekday", "bad_weekday")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(String.format(String.format(CorePlatformConstants.Validation.DataIntegrity.INVALID_WEEKDAY, "bad_weekday")))));
    }

    @Test
    void test_getWeekdayTimeBucketsAnalysis_missingAccount() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, WEEKDAYS_TIME_BUCKETS))
                        .queryParam("accountNumber", "5678")
                        .queryParam("filter", "PROFIT")
                        .queryParam("weekday", "MONDAY")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(CorePlatformConstants.Validation.Account.ACCOUNT_NOT_FOUND)));
    }

    @Test
    void test_getWeekdayTimeBucketsAnalysis_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, WEEKDAYS_TIME_BUCKETS))
                        .queryParam("accountNumber", "1234")
                        .queryParam("filter", "PROFIT")
                        .queryParam("weekday", "MONDAY")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].value", is(91.0)));
    }


    //  ----------------- getTradeDurationsAnalysis -----------------

    @Test
    void test_getTradeDurationsAnalysis_badFilter() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, TRADE_DURATIONS))
                        .queryParam("accountNumber", "5678")
                        .queryParam("filter", "bad_filter")
                        .queryParam("tradeDurationFilter", "MONDAY")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getTradeDurationsAnalysis_missingAccount() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, TRADE_DURATIONS))
                        .queryParam("accountNumber", "5678")
                        .queryParam("filter", "PROFIT")
                        .queryParam("tradeDurationFilter", "WINS")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(CorePlatformConstants.Validation.Account.ACCOUNT_NOT_FOUND)));
    }

    @Test
    void test_getTradeDurationsAnalysis_success() throws Exception {
        this.mockMvc.perform(get(getApiPath(BASE, TRADE_DURATIONS))
                        .queryParam("accountNumber", "1234")
                        .queryParam("filter", "PROFIT")
                        .queryParam("tradeDurationFilter", "WINS")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].value", is(91.0)));
    }
}
