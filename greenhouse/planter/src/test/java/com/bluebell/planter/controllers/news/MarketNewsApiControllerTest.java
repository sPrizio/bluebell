package com.bluebell.planter.controllers.news;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.enums.system.Country;
import com.bluebell.radicle.services.news.MarketNewsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link MarketNewsApiController}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class MarketNewsApiControllerTest extends AbstractPlanterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarketNewsService marketNewsService;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        /*Mockito.when(this.marketNewsService.findNewsWithinInterval(any(), any(), any())).thenReturn(List.of(generateMarketNews()));
        Mockito.when(this.marketNewsService.findMarketNewsForDate(any())).thenReturn(Optional.of(generateMarketNews()));*/
    }


    //  ----------------- getNews -----------------

    @Test
    public void test_getNews_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("date", List.of("BAD"));

        this.mockMvc.perform(get("/api/v1/news/get").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Looks like your request could not be processed. Check your inputs and try again!")));
    }

    @Test
    public void test_getNews_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("date", List.of("2023-01-21"));

        this.mockMvc.perform(get("/api/v1/news/get").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.date", is("2023-01-19")));
    }


    //  ----------------- getNewsForInterval -----------------

    @Test
    public void test_getNewsForInterval_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("BAD"));
        map.put("end", List.of("2023-01-21"));
        map.put("locales", List.of(Country.ALL_COUNTRIES.getIsoCode()));

        this.mockMvc.perform(get("/api/v1/news/get-for-interval").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Looks like your request could not be processed. Check your inputs and try again!")));
    }

    @Test
    public void test_getNewsForInterval_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2023-01-16"));
        map.put("end", List.of("2023-01-21"));
        map.put("locales", List.of(Country.ALL_COUNTRIES.getIsoCode()));

        this.mockMvc.perform(get("/api/v1/news/get-for-interval").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].date", is("2023-01-19")));
    }


    //  ----------------- postFetchNews -----------------

    @Test
    public void test_postFetchNews_failure() throws Exception {
        Mockito.when(this.marketNewsService.fetchMarketNews()).thenReturn(false);
        this.mockMvc.perform(post("/api/v1/news/fetch-news").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    public void test_postFetchNews_success() throws Exception {
        Mockito.when(this.marketNewsService.fetchMarketNews()).thenReturn(true);
        this.mockMvc.perform(post("/api/v1/news/fetch-news").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }
}
