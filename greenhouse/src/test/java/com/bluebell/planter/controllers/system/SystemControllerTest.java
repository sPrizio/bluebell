package com.bluebell.planter.controllers.system;

import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bluebell.planter.constants.ApiConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Testing class for {@link SystemController}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class SystemControllerTest {

    @Autowired
    private MockMvc mockMvc;


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
}
