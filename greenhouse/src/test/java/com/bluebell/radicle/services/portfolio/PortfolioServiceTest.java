package com.bluebell.radicle.services.portfolio;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.nonentities.records.portfolio.Portfolio;
import com.bluebell.AbstractGenericTest;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Testing class for {@link PortfolioService}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class PortfolioServiceTest extends AbstractGenericTest {

    @Autowired
    private PortfolioService portfolioService;


    //  ----------------- getPortfolio -----------------

    @Test
    void test_getPortfolio_success() {

        assertThatExceptionOfType(IllegalParameterException.class).isThrownBy(() -> this.portfolioService.getPortfolio(null));

        final User user = generateTestUser();
        user.getAccounts().get(0).setTrades(List.of(generateTestBuyTrade(), generateTestSellTrade()));
        user.getAccounts().get(0).setBalance(1010.35);
        user.getAccounts().get(0).setTransactions(List.of(generateTestTransactionDeposit(user.getAccounts().get(0)), generateTestTransactionDeposit(user.getAccounts().get(0)), generateTestTransactionWithdrawal(user.getAccounts().get(0))));
        final Portfolio portfolio = this.portfolioService.getPortfolio(user);

        assertThat(portfolio).isNotNull();
        assertThat(portfolio)
                .extracting("newPortfolio", "netWorth", "trades", "deposits", "withdrawals")
                .containsExactly(false, 1010.35, 2L, 2L, 1L);

        assertThat(portfolio.equity())
                .isNotEmpty()
                .element(5)
                .extracting("portfolio")
                .isEqualTo(1010.35);

        assertThat(portfolio.statistics())
                .extracting("deltaNetWorth", "deltaTrades", "deltaDeposits", "deltaWithdrawals")
                .containsExactly(1.04, 200.0, 100.0, 100.0);
    }
}
