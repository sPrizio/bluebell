package com.bluebell.radicle.services.portfolio;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioRecord;
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
 * @version 0.1.2
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class PortfolioRecordServiceTest extends AbstractGenericTest {

    @Autowired
    private PortfolioRecordService portfolioRecordService;


    //  ----------------- getPortfolioRecord -----------------

    @Test
    void test_getPortfolioRecord_success() {

        assertThatExceptionOfType(IllegalParameterException.class).isThrownBy(() -> this.portfolioRecordService.getPortfolioRecord(null));

        final User user = generateTestUser();
        user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0).setTrades(List.of(generateTestBuyTrade(), generateTestSellTrade()));
        user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0).setBalance(1010.35);
        user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0).setTransactions(List.of(generateTestTransactionDeposit(user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0)), generateTestTransactionDeposit(user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0)), generateTestTransactionWithdrawal(user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0))));
        final PortfolioRecord portfolioRecord = this.portfolioRecordService.getPortfolioRecord(user);

        assertThat(portfolioRecord).isNotNull();
        assertThat(portfolioRecord)
                .extracting("newPortfolio", "netWorth", "trades", "deposits", "withdrawals")
                .containsExactly(false, 1010.35, 2L, 2L, 1L);

        assertThat(portfolioRecord.equity())
                .isNotEmpty()
                .element(5)
                .extracting("portfolio")
                .isEqualTo(1010.35);

        assertThat(portfolioRecord.statistics())
                .extracting("deltaNetWorth", "deltaTrades", "deltaDeposits", "deltaWithdrawals")
                .containsExactly(1.04, 200.0, 100.0, 100.0);
    }
}
