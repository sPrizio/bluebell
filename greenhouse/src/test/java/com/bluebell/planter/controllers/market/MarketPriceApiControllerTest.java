package com.bluebell.planter.controllers.market;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.constants.ApiConstants;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link MarketPriceApiController}
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class MarketPriceApiControllerTest extends AbstractPlanterTest {

    @Autowired
    private MockMvc mockMvc;

    private final MockMultipartFile TEST_FILE_BAD = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
    private final MockMultipartFile TEST_FILE_GOOD = new MockMultipartFile("file", "hello.csv", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());


    //  ----------------- postIngestMarketPriceDataFromMT4 -----------------

    @Test
    void test_postIngestMarketPriceDataFromMT4_badRequest_file() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("symbol", List.of("NDAQ100"));
        map.put("priceInterval", List.of("FIVE_MINUTE"));

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/market-price/ingest").file(TEST_FILE_BAD).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_postIngestMarketPriceDataFromMT4_badRequest_symbol() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("symbol", List.of("afasf#%^$^$#$%#$%%##%^"));
        map.put("priceInterval", List.of("FIVE_MINUTE"));

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/market-price/ingest").file(TEST_FILE_GOOD).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("afasf#%^$^$#$%#$%%##%^ was not a valid symbol")));

    }

    @Test
    void test_postIngestMarketPriceDataFromMT4_badRequest_interval() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("symbol", List.of("NDAQ100"));
        map.put("priceInterval", List.of("^[a-zA-Z][a-zA-Z0-9.!]*$"));

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/market-price/ingest").file(TEST_FILE_GOOD).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("^[a-zA-Z][a-zA-Z0-9.!]*$ is not a valid time interval")));
    }
}
