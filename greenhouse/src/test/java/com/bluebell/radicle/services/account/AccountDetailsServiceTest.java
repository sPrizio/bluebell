package com.bluebell.radicle.services.account;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.models.core.nonentities.records.account.AccountEquityPoint;
import com.bluebell.radicle.AbstractGenericTest;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Testing class for {@link AccountDetailsService}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class AccountDetailsServiceTest extends AbstractGenericTest {

    private Account account;

    @Autowired
    private AccountDetailsService accountDetailsService;

    @BeforeEach
    public void setUp() {
        this.account = generateTestAccount();
        final List<Trade> trades = List.of(generateTestBuyTrade(), generateTestSellTrade());
        this.account.setTrades(trades);
        this.account.setBalance(1010.35);
    }


    //  ----------------- calculateConsistencyScore -----------------

    @Test
    void test_calculateConsistencyScore_success() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountDetailsService.calculateConsistencyScore(null))
                .withMessageContaining(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        assertThat(this.accountDetailsService.calculateConsistencyScore(this.account))
                .isEqualTo(49);
    }


    //  ----------------- calculateEquityPoints -----------------

    @Test
    void test_calculateEquityPoints_noTrades_success() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountDetailsService.calculateEquityPoints(null))
                .withMessageContaining(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        final List<AccountEquityPoint> equityPoints = this.accountDetailsService.calculateEquityPoints(generateTestAccount());
        assertThat(equityPoints).hasSize(1);

        assertThat(equityPoints)
                .element(0)
                .extracting("amount", "cumAmount", "cumPoints")
                .containsExactly(1000.0, 1000.0, 0.0);
    }

    @Test
    void test_calculateEquityPoints_success() {

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountDetailsService.calculateEquityPoints(null))
                .withMessageContaining(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        final List<AccountEquityPoint> equityPoints = this.accountDetailsService.calculateEquityPoints(this.account);
        assertThat(equityPoints).hasSize(3);

        assertThat(equityPoints)
                .element(2)
                .extracting("amount", "cumAmount", "cumPoints")
                .containsExactly(14.85, 1010.35, 11.29);

        assertThat(equityPoints)
                .element(0)
                .extracting("amount", "cumAmount", "cumPoints")
                .containsExactly(0.0, 1000.0, 0.0);
    }


    //  ----------------- obtainInsights -----------------

    @Test
    void test_obtainInsights_success() {

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountDetailsService.obtainInsights(null))
                .withMessageContaining(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        assertThat(this.accountDetailsService.obtainInsights(this.account))
                .extracting(
                        "tradingDays",
                        "currentPL",
                        "biggestLoss",
                        "largestGain",
                        "drawdown",
                        "maxProfit",
                        "currentPLDelta",
                        "biggestLossDelta",
                        "largestGainDelta",
                        "drawdownDelta",
                        "maxProfitDelta"
                )
                .containsExactly(
                        1,
                        10.35,
                        -4.5,
                        14.85,
                        -4.5,
                        10.35,
                        1.04,
                        0.45,
                        1.48,
                        0.45,
                        1.04
                );
    }


    //  ----------------- obtainStatistics -----------------

    @Test
    void test_obtainStatistics_success() {

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountDetailsService.obtainStatistics(null))
                .withMessageContaining(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        assertThat(this.accountDetailsService.obtainStatistics(this.account))
                .extracting(
                        "balance",
                        "averageProfit",
                        "averageLoss",
                        "numberOfTrades",
                        "rrr",
                        "lots",
                        "expectancy",
                        "winPercentage",
                        "profitFactor",
                        "retention",
                        "sharpeRatio",
                        "tradeDuration",
                        "winDuration",
                        "lossDuration",
                        "assumedDrawdown"
                )
                .containsExactly(
                        1010.35,
                        14.85,
                        -4.5,
                        2,
                        0.0,
                        1.5,
                        5.18,
                        50,
                        3.3,
                        77,
                        0.0,
                        151L,
                        266L,
                        36L,
                        -9.0
                );
    }
}
