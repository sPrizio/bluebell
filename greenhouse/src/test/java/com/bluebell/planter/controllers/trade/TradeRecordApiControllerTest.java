package com.bluebell.planter.controllers.trade;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecordReport;
import com.bluebell.radicle.services.trade.TradeRecordService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Testing class for {@link TradeRecordApiController}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class TradeRecordApiControllerTest extends AbstractPlanterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeRecordService tradeRecordService;

    @Before
    public void setUp() {
        Mockito.when(this.tradeRecordService.getTradeRecords(any(), any(), any(), any(), anyInt())).thenReturn(new TradeRecordReport(List.of(generateTradeRecord()), null));
    }


    //  ----------------- getTradeRecordsWithinInterval -----------------

    /*@Test
    public void test_getTradeRecordsWithinInterval_badRequest() throws Exception {

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
    public void test_getTradeRecordsWithinInterval_badRequest_enum() throws Exception {

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
    public void test_getTradeRecordsWithinInterval_success() throws Exception {

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
    }*/
}
