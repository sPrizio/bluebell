package com.bluebell.planter.controllers.market;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.constants.ApiConstants;
import com.bluebell.platform.util.DirectoryUtil;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
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

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link MarketPriceApiController}
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class MarketPriceApiControllerTest extends AbstractPlanterTest {

    @Autowired
    private MockMvc mockMvc;

    private static final MockMultipartFile TEST_FILE_BAD_EXTENSION = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
    private static final MockMultipartFile TEST_FILE_BAD_DATA = new MockMultipartFile("file", "hello.csv", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
    private static final MockMultipartFile TEST_FILE_GOOD = new MockMultipartFile("file", "hello.csv", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
    private static final MockMultipartFile TEST_FILE_GOOD_DATA = new MockMultipartFile("file", "hello.csv", MediaType.TEXT_PLAIN_VALUE, "2024.01.24;16:00;17538.13;17568.25;17535.5;17566.38;1297".getBytes());

    @AfterEach
    void tearDown() throws IOException {
        FileUtils.deleteDirectory(new File(String.format("%s%stest-ingress", DirectoryUtil.getTestingResourcesDirectory(), File.separator)));
    }


    //  ----------------- postIngestMarketPriceDataFromMT4 -----------------

    @Test
    void test_postIngestMarketPriceDataFromMT4_badRequest_file() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("symbol", List.of("NDAQ100"));
        map.put("priceInterval", List.of("FIVE_MINUTE"));

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/market-price/ingest").file(TEST_FILE_BAD_EXTENSION).with(testUserContext()).params(map))
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

    @Test
    void test_postIngestMarketPriceDataFromMT4_badFile() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("symbol", List.of("NDAQ100"));
        map.put("priceInterval", List.of("FIVE_MINUTE"));

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/market-price/ingest").file(TEST_FILE_BAD_DATA).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("File did not contain valid mt4 .csv")));
    }

    @Test
    void test_postIngestMarketPriceDataFromMT4_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("symbol", List.of("NDAQ100"));
        map.put("priceInterval", List.of("FIVE_MINUTE"));

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/market-price/ingest").file(TEST_FILE_GOOD_DATA).with(testUserContext()).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("was successfully saved to the ingress")));

        assertThat(new File(String.format("%s%stest-ingress%s%s%s%s%s%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, File.separator, "mt4", File.separator, "NDAQ100", File.separator, "FIVE_MINUTE", File.separator, "hello.csv"))).exists();
    }
}
