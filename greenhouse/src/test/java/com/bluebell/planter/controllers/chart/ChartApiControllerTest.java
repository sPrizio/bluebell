package com.bluebell.planter.controllers.chart;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.nonentities.apexcharts.ApexChartCandleStick;
import com.bluebell.radicle.services.chart.ChartService;
import com.bluebell.radicle.services.trade.TradeService;
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

import java.util.List;
import java.util.Optional;

import static com.bluebell.planter.constants.ApiPaths.Chart.APEX_DATA;
import static com.bluebell.planter.constants.ApiPaths.Chart.BASE;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link ChartApiController}
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class ChartApiControllerTest extends AbstractPlanterTest {

    final Account account = generateTestAccount();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChartService<ApexChartCandleStick> chartService;

    @MockitoBean
    private TradeService tradeService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.chartService.getChartDataForTrade(any(), any())).thenReturn(List.of(ApexChartCandleStick.builder().x(123L).y(new double[]{1.0, 2.0, 3.0}).build()));
        Mockito.when(this.tradeService.findTradeByTradeId("1234", this.account)).thenReturn(Optional.empty());
        Mockito.when(this.tradeService.findTradeByTradeId("5678", this.account)).thenReturn(Optional.of(generateTestBuyTrade()));
    }


    //  ----------------- getApexChartData -----------------

    @Test
    void test_getApexChartData_missingTrade() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("tradeId", List.of("1234"));
        map.put("interval", List.of("FIVE_MINUTE"));
        map.put("accountNumber", List.of("1234"));

        this.mockMvc.perform(get(getApiPath(BASE, APEX_DATA)).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Trade with id 1234 not found")));
    }

    @Test
    void test_getApexChartData_invalidInterval() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("tradeId", List.of("5678"));
        map.put("interval", List.of("asdasdasd"));
        map.put("accountNumber", List.of("1234"));

        this.mockMvc.perform(get(getApiPath(BASE, APEX_DATA)).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("is not a valid time interval")));
    }

    @Test
    void test_getApexChartData_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("tradeId", List.of("5678"));
        map.put("interval", List.of("FIVE_MINUTE"));
        map.put("accountNumber", List.of("1234"));

        this.mockMvc.perform(get(getApiPath(BASE, APEX_DATA)).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].x", is(123)));
    }
}
