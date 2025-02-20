package com.bluebell.planter.api.controllers.chart;

import com.bluebell.planter.AbstractGenericTest;
import com.bluebell.planter.api.constants.ApiConstants;
import com.bluebell.planter.core.services.chart.ChartService;
import com.bluebell.platform.models.core.nonentities.apexcharts.ApexChartCandleStick;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
 * @version 0.0.1
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class ChartApiControllerTest extends AbstractGenericTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChartService<ApexChartCandleStick> chartService;

    @Before
    public void setUp() throws Exception {
        Mockito.when(this.chartService.getChartData(any(), any(), any())).thenReturn(List.of(new ApexChartCandleStick(123L, new double[]{1.0, 2.0, 3.0})));
    }


    //  ----------------- getApexChartData -----------------

    @Test
    public void test_getApexChartData_missingParamStart() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("dasdfasdfaf"));
        map.put("end", List.of("2022-08-25"));
        map.put("interval", List.of("five-minute"));

        this.mockMvc.perform(get("/api/v1/chart/apex-data").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_getApexChartData_missingParamEnd() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-25"));
        map.put("end", List.of("asdadasdasd"));
        map.put("interval", List.of("five-minute"));

        this.mockMvc.perform(get("/api/v1/chart/apex-data").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_getApexChartData_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24"));
        map.put("end", List.of("2022-08-25"));
        map.put("interval", List.of("five-minute"));

        this.mockMvc.perform(get("/api/v1/chart/apex-data").with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].x", is(123)));
    }
}
