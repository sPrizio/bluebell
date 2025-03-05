package com.bluebell.planter.controllers.portfolio;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.radicle.services.portfolio.PortfolioService;
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

/**
 * Testing class for {@link PortfolioApiController}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class PortfolioApiControllerTest extends AbstractPlanterTest {

    @MockitoBean
    private PortfolioService portfolioService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        Mockito.when(this.portfolioService.getPortfolio(any())).thenReturn(generatePortfolio());
    }


    //  ----------------- getPortfolio -----------------

    @Test
    void test_getPortfolio_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/portfolio/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.netWorth", is(1000000.0)));
    }
}
