package com.bluebell.radicle.services.portfolio;

import com.bluebell.AbstractGenericTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.radicle.repositories.portfolio.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link PortfolioService}
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class PortfolioServiceTest extends AbstractGenericTest {

    @MockitoBean
    private PortfolioRepository portfolioRepository;

    @Autowired
    private PortfolioService portfolioService;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.retrieveId("1234")).thenReturn(1L);
        Mockito.when(this.uniqueIdentifierService.retrieveId("5678")).thenReturn(-1L);
        Mockito.when(this.portfolioRepository.findById(any())).thenReturn(Optional.of(generateTestPortfolio()));
    }


    //  ----------------- findPortfolioByUid -----------------

    @Test
    void test_findPortfolioByUid_badUid() {
        assertThat(this.portfolioService.findPortfolioByUid(null)).isEmpty();
    }

    @Test
    void test_findPortfolioByUid_success() {
        assertThat(this.portfolioService.findPortfolioByUid("1234")).isNotEmpty();
    }
}
