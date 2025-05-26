package com.bluebell.radicle.services.portfolio;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.radicle.repositories.portfolio.PortfolioRepository;
import com.bluebell.radicle.repositories.security.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration testing class for {@link PortfolioService}
 *
 * @author Stephen Prizio
 * @version 0.2.2
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class PortfolioServiceIntegrationTest extends AbstractGenericTest {

    private User user;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = generateTestUser();
        user.setPortfolios(null);
        user.setPhones(null);
        user = this.userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        this.userRepository.deleteAll();
    }


    //  ----------------- reassignPortfolios -----------------

    @Test
    void test_reassignPortfolios_success() {
        this.portfolioRepository.deleteAll();

        Portfolio portfolio1 = generateTestPortfolio();
        portfolio1.setAccounts(null);
        portfolio1.setId(null);
        portfolio1.setPortfolioNumber(1111L);
        portfolio1.setDefaultPortfolio(false);
        portfolio1.setUser(this.user);
        portfolio1 = this.portfolioRepository.save(portfolio1);

        Portfolio portfolio2 = generateTestPortfolio();
        portfolio2.setAccounts(null);
        portfolio2.setId(null);
        portfolio2.setPortfolioNumber(2222L);
        portfolio2.setDefaultPortfolio(false);
        portfolio2.setUser(this.user);
        portfolio2 = this.portfolioRepository.save(portfolio2);

        Portfolio portfolio3 = generateTestPortfolio();
        portfolio3.setAccounts(null);
        portfolio3.setId(null);
        portfolio3.setPortfolioNumber(3333L);
        portfolio3.setDefaultPortfolio(false);
        portfolio3.setUser(this.user);
        portfolio3 = this.portfolioRepository.save(portfolio3);

        Portfolio portfolio4 = generateTestPortfolio();
        portfolio4.setAccounts(null);
        portfolio4.setId(null);
        portfolio4.setPortfolioNumber(4444L);
        portfolio4.setDefaultPortfolio(false);
        portfolio4.setUser(this.user);
        portfolio4 = this.portfolioRepository.save(portfolio4);

        user.setPortfolios(List.of(portfolio1, portfolio2, portfolio3, portfolio4));
        user = this.userRepository.save(user);

        this.portfolioService.reassignPortfolios(this.user.getUsername());
        assertThat(this.portfolioRepository.findPortfolioByPortfolioNumber(portfolio1.getPortfolioNumber()).isDefaultPortfolio()).isTrue();

        this.portfolioService.deletePortfolio(portfolio1);
        this.portfolioService.deletePortfolio(portfolio3);

        this.portfolioService.reassignPortfolios(this.user.getUsername());

        assertThat(this.portfolioRepository.findPortfolioByPortfolioNumber(portfolio2.getPortfolioNumber()).isDefaultPortfolio()).isTrue();
    }
}
