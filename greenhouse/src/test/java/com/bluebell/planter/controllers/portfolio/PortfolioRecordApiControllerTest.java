package com.bluebell.planter.controllers.portfolio;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.radicle.services.portfolio.PortfolioRecordService;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link PortfolioRecordApiController}
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class PortfolioRecordApiControllerTest extends AbstractPlanterTest {

    @MockitoBean
    private PortfolioRecordService portfolioRecordService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Mockito.when(this.portfolioRecordService.getPortfolioRecord(any())).thenReturn(generatePortfolioRecord());
    }


    //  ----------------- getPortfolioRecord -----------------

    @Test
    void test_getPortfolioRecord_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/portfolio-record/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.netWorth", is(1000000.0)));
    }
}
