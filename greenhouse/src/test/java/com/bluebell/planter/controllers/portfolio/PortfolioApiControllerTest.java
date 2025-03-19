package com.bluebell.planter.controllers.portfolio;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.constants.ApiConstants;
import com.bluebell.planter.converters.portfolio.PortfolioDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.portfolio.CreateUpdatePortfolioDTO;
import com.bluebell.platform.models.api.dto.portfolio.PortfolioDTO;
import com.bluebell.radicle.security.constants.SecurityConstants;
import com.bluebell.radicle.services.portfolio.PortfolioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link PortfolioApiController}
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class PortfolioApiControllerTest extends AbstractPlanterTest {

    private static final String PORTFOLIO_UID = "portfolioUid";

    @MockitoBean
    private PortfolioDTOConverter portfolioDTOConverter;

    @MockitoBean
    private PortfolioService portfolioService;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Mockito.when(this.portfolioService.findPortfolioByUid("1234")).thenReturn(Optional.of(generateTestPortfolio()));
        Mockito.when(this.portfolioService.findPortfolioByUid("5678")).thenReturn(Optional.empty());
        Mockito.when(this.portfolioService.createPortfolio(any(), any())).thenReturn(generateTestPortfolio());
        Mockito.when(this.portfolioDTOConverter.convert(any())).thenReturn(PortfolioDTO.builder().name("Test").active(true).defaultPortfolio(true).build());
        Mockito.when(this.portfolioService.deletePortfolio(any())).thenReturn(true);
    }


    //  ----------------- getPortfolioForUid -----------------

    @Test
    void test_getPortfolioForUid_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/portfolio/get")
                        .queryParam(PORTFOLIO_UID, "1234")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        this.mockMvc.perform(get("/api/v1/portfolio/get")
                        .queryParam(PORTFOLIO_UID, "5678")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)));
    }


    //  ----------------- postCreateNewPortfolio -----------------

    @Test
    void test_postCreateNewAccount_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post("/api/v1/portfolio/create-portfolio")
                        .contentType(MediaType.APPLICATION_JSON).
                        content(new ObjectMapper().writeValueAsString(Map.of("hello", "world")))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_postCreateNewAccount_success() throws Exception {

        final CreateUpdatePortfolioDTO data = CreateUpdatePortfolioDTO
                .builder()
                .name("Test")
                .active(false)
                .defaultPortfolio(true)
                .build();

        this.mockMvc.perform(post("/api/v1/portfolio/create-portfolio")
                        .requestAttr(SecurityConstants.USER_REQUEST_KEY, generateTestUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.active", is(true)));
    }


    //  ----------------- putUpdatePortfolio -----------------

    @Test
    void test_putUpdatePortfolio_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(put("/api/v1/portfolio/update-portfolio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PORTFOLIO_UID, "1234")
                        .content(new ObjectMapper().writeValueAsString(Map.of("hello", "world")))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_putUpdatePortfolio_portfolioNotFound_success() throws Exception {

        final CreateUpdatePortfolioDTO data = CreateUpdatePortfolioDTO
                .builder()
                .name("Test")
                .active(false)
                .defaultPortfolio(true)
                .build();

        this.mockMvc.perform(put("/api/v1/portfolio/update-portfolio")
                        .requestAttr(SecurityConstants.USER_REQUEST_KEY, generateTestUser())
                        .queryParam(PORTFOLIO_UID, "5678")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Portfolio not found")));
    }

    @Test
    void test_putUpdatePortfolio_success() throws Exception {

        final CreateUpdatePortfolioDTO data = CreateUpdatePortfolioDTO
                .builder()
                .name("Test")
                .active(false)
                .defaultPortfolio(true)
                .build();

        this.mockMvc.perform(put("/api/v1/portfolio/update-portfolio")
                        .requestAttr(SecurityConstants.USER_REQUEST_KEY, generateTestUser())
                        .queryParam(PORTFOLIO_UID, "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.active", is(true)));
    }


    //  ----------------- deletePortfolio -----------------

    @Test
    void test_deletePortfolio_missingPortfolio() throws Exception {
        this.mockMvc.perform(delete("/api/v1/portfolio/delete-portfolio")
                        .queryParam(PORTFOLIO_UID, "5678")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Portfolio not found")));
    }

    @Test
    void test_deletePortfolio_success() throws Exception {
        this.mockMvc.perform(delete("/api/v1/portfolio/delete-portfolio")
                        .queryParam(PORTFOLIO_UID, "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }
}
