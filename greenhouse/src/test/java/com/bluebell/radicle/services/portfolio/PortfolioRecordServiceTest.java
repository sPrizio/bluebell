package com.bluebell.radicle.services.portfolio;

import com.bluebell.AbstractGenericTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioRecord;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link PortfolioService}
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class PortfolioRecordServiceTest extends AbstractGenericTest {

    @Autowired
    private PortfolioRecordService portfolioRecordService;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.retrieveId(any())).thenReturn(1L);
    }


    //  ----------------- getSinglePortfolioRecord -----------------

    @Test
    void test_getSinglePortfolioRecord_success() {

        assertThatExceptionOfType(IllegalParameterException.class).isThrownBy(() -> this.portfolioRecordService.getSinglePortfolioRecord("1234", null));
        assertThatExceptionOfType(IllegalParameterException.class).isThrownBy(() -> this.portfolioRecordService.getSinglePortfolioRecord(null, null));

        final User user = generateTestUser();
        user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0).setTrades(List.of(generateTestBuyTrade(), generateTestSellTrade()));
        user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0).setBalance(1010.35);
        user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0).setTransactions(List.of(generateTestTransactionDeposit(user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0)), generateTestTransactionDeposit(user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0)), generateTestTransactionWithdrawal(user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0))));
        final PortfolioRecord portfolioRecord = this.portfolioRecordService.getSinglePortfolioRecord("1234", user);

        assertThat(portfolioRecord).isNotNull();
        assertThat(portfolioRecord)
                .extracting("newPortfolio", "netWorth", "trades", "deposits", "withdrawals")
                .containsExactly(false, 1010.35, 2L, 2L, 1L);

        assertThat(portfolioRecord.equity())
                .isNotEmpty()
                .element(5)
                .extracting("portfolio")
                .isEqualTo(1000.00);

        assertThat(portfolioRecord.statistics())
                .extracting("deltaNetWorth", "deltaTrades", "deltaDeposits", "deltaWithdrawals")
                .containsExactly(1.04, 200.0, 100.0, 100.0);
    }


    //  ----------------- getComprehensivePortfolioRecord -----------------

    @Test
    void test_getComprehensivePortfolioRecord_success() {

        assertThatExceptionOfType(IllegalParameterException.class).isThrownBy(() -> this.portfolioRecordService.getComprehensivePortfolioRecord(null));

        final User user = generateTestUser();
        user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0).setTrades(List.of(generateTestBuyTrade(), generateTestSellTrade()));
        user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0).setBalance(1010.35);
        user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0).setTransactions(List.of(generateTestTransactionDeposit(user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0)), generateTestTransactionDeposit(user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0)), generateTestTransactionWithdrawal(user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList().get(0))));
        final PortfolioRecord portfolioRecord = this.portfolioRecordService.getComprehensivePortfolioRecord(user);

        assertThat(portfolioRecord).isNotNull();
        assertThat(portfolioRecord)
                .extracting("newPortfolio", "netWorth", "trades", "deposits", "withdrawals")
                .containsExactly(false, 1010.35, 2L, 2L, 1L);

        assertThat(portfolioRecord.equity())
                .isNotEmpty()
                .element(5)
                .extracting("portfolio")
                .isEqualTo(1000.00);

        assertThat(portfolioRecord.statistics())
                .extracting("deltaNetWorth", "deltaTrades", "deltaDeposits", "deltaWithdrawals")
                .containsExactly(1.04, 200.0, 100.0, 100.0);
    }
}
