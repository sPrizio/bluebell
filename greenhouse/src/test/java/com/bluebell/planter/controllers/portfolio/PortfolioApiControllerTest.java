package com.bluebell.planter.controllers.portfolio;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.radicle.services.portfolio.PortfolioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
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
 * @version 0.1.2
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
    }

}
