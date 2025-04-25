package com.bluebell.planter.controllers.chart;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.constants.ApiConstants;
import com.bluebell.platform.models.core.nonentities.apexcharts.ApexChartCandleStick;
import com.bluebell.radicle.services.chart.ChartService;
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
 * @version 0.1.7
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class ChartApiControllerTest extends AbstractPlanterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChartService<ApexChartCandleStick> chartService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.chartService.getChartData(any(), any(), any(), any(), any())).thenReturn(List.of(ApexChartCandleStick.builder().x(123L).y(new double[]{1.0, 2.0, 3.0}).build()));
    }


    //  ----------------- getApexChartData -----------------

    @Test
    void test_getApexChartData_missingParamStart() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("dasdfasdfaf"));
        map.put("end", List.of("2022-08-25"));
        map.put("interval", List.of("FIVE_MINUTE"));
        map.put("symbol", List.of("NDX"));
        map.put("dataSource", List.of("METATRADER4"));

        this.mockMvc.perform(get("/api/v1/chart/apex-data").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getApexChartData_missingParamEnd() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-25"));
        map.put("end", List.of("asdadasdasd"));
        map.put("interval", List.of("FIVE_MINUTE"));
        map.put("symbol", List.of("NDX"));
        map.put("dataSource", List.of("METATRADER4"));

        this.mockMvc.perform(get("/api/v1/chart/apex-data").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getApexChartData_badSymbol() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24"));
        map.put("end", List.of("2022-08-25"));
        map.put("interval", List.of("FIVE_MINUTE"));
        map.put("symbol", List.of("NDX%&^$$%^#"));
        map.put("dataSource", List.of("METATRADER4"));

        this.mockMvc.perform(get("/api/v1/chart/apex-data").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Invalid symbol: NDX%&^$$%^#")));
    }

    @Test
    void test_getApexChartData_badDataSource() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24"));
        map.put("end", List.of("2022-08-25"));
        map.put("interval", List.of("FIVE_MINUTE"));
        map.put("symbol", List.of("NDX"));
        map.put("dataSource", List.of("BAD"));

        this.mockMvc.perform(get("/api/v1/chart/apex-data").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("BAD is not a valid data source")));
    }

    @Test
    void test_getApexChartData_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24"));
        map.put("end", List.of("2022-08-25"));
        map.put("interval", List.of("FIVE_MINUTE"));
        map.put("symbol", List.of("NDX"));
        map.put("dataSource", List.of("METATRADER4"));

        this.mockMvc.perform(get("/api/v1/chart/apex-data").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].x", is(123)));
    }
}
