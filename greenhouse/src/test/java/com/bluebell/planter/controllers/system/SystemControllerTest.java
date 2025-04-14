package com.bluebell.planter.controllers.system;

import com.bluebell.planter.constants.ApiConstants;
import com.bluebell.radicle.services.system.IncomingPingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link SystemController}
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class SystemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${bluebell.base.api.controller.endpoint}")
    private String baseApiDomain;

    @Value("${bluebell.domain}")
    private String domain;

    @Value("${bluebell.version}")
    private String version;

    @Value("${bluebell.api.version}")
    private String apiVersion;

    @MockitoBean
    private IncomingPingService incomingPingService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.incomingPingService.acknowledgeIncomingPing("good")).thenReturn(true);
        Mockito.when(this.incomingPingService.acknowledgeIncomingPing("bad")).thenReturn(false);
    }


    //  ----------------- getHealthCheck -----------------

    @Test
    void test_getHealthCheck_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/system/healthcheck"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.domain", is(this.domain)))
                .andExpect(jsonPath("$.data.baseApiDomain", is(this.baseApiDomain)))
                .andExpect(jsonPath("$.data.version", is(this.version)))
                .andExpect(jsonPath("$.data.apiVersion", is(this.apiVersion)));
    }


    //  ----------------- postContact -----------------

    @Test
    void test_postContact_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post("/api/v1/system/contact").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_postContact_success() throws Exception {

        Map<String, Object> data =
                Map.of(
                        "contact",
                        Map.of(
                                "name", "Test User",
                                "email", "test@email.com",
                                "subject", "Greetings",
                                "message", "Hello There, General Kenobi"
                        )
                );

        this.mockMvc.perform(post("/api/v1/system/contact").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }


    //  ----------------- postReport -----------------

    @Test
    void test_postReport_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post("/api/v1/system/report").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_postReport_success() throws Exception {

        Map<String, Object> data =
                Map.of(
                        "report",
                        Map.of(
                                "name", "Test User",
                                "email", "test@email.com",
                                "severity", "moderate",
                                "message", "Hello There, General Kenobi"
                        )
                );

        this.mockMvc.perform(post("/api/v1/system/report").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }


    //  ----------------- postAcknowledgeIncomingPing -----------------

    @Test
    void test_postAcknowledgeIncomingPing_fail() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("systemName", List.of("bad"));

        this.mockMvc.perform(post("/api/v1/system/acknowledge").queryParams(map).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    void test_postAcknowledgeIncomingPing_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("systemName", List.of("good"));

        this.mockMvc.perform(post("/api/v1/system/acknowledge").queryParams(map).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }
}
