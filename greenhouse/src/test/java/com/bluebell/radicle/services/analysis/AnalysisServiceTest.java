package com.bluebell.radicle.services.analysis;

import java.time.DayOfWeek;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.bluebell.platform.enums.analysis.AnalysisFilter;
import com.bluebell.platform.enums.analysis.TradeDurationFilter;
import com.bluebell.platform.enums.time.PlatformTimeInterval;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.AbstractGenericTest;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Testing class for {@link AnalysisService}
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class AnalysisServiceTest extends AbstractGenericTest {

    private Account account;

    @Autowired
    private AnalysisService analysisService;

    @BeforeEach
    void setUp() {
        this.account = generateTestAccount();
        final List<Trade> trades = List.of(generateTestBuyTrade(), generateTestSellTrade());
        this.account.setTrades(trades);
        this.account.setBalance(1010.35);
    }


    //  ----------------- computeTimeBucketAnalysis -----------------

    @Test
    void test_computeTimeBucketAnalysis_success() {

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.analysisService.computeTimeBucketAnalysis(null, PlatformTimeInterval.FIVE_MINUTE, AnalysisFilter.PROFIT, true));

        assertThat(this.analysisService.computeTimeBucketAnalysis(this.account, PlatformTimeInterval.FIVE_MINUTE, AnalysisFilter.PROFIT, true))
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .element(24)
                .extracting("label", "value", "count")
                .containsExactly("11:30", 14.85, 1);
    }


    //  ----------------- computeWeekdayAnalysis -----------------

    @Test
    void test_computeWeekdayAnalysis_success() {

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.analysisService.computeWeekdayAnalysis(null, AnalysisFilter.PROFIT));

        assertThat(this.analysisService.computeWeekdayAnalysis(this.account, AnalysisFilter.PROFIT))
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .element(2)
                .extracting("label", "value", "count")
                .containsExactly("Wednesday", 10.35, 2);
    }


    //  ----------------- computeWeekdayTimeBucketAnalysis -----------------

    @Test
    void test_computeWeekdayTimeBucketAnalysis_success() {

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.analysisService.computeWeekdayTimeBucketAnalysis(null, DayOfWeek.WEDNESDAY, PlatformTimeInterval.FIFTEEN_MINUTE, AnalysisFilter.PROFIT));

        assertThat(this.analysisService.computeWeekdayTimeBucketAnalysis(this.account, DayOfWeek.WEDNESDAY, PlatformTimeInterval.FIFTEEN_MINUTE, AnalysisFilter.POINTS))
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .element(3)
                .extracting("label", "value", "count")
                .containsExactly("10:15", -3.97, 1);

        assertThat(this.analysisService.computeWeekdayTimeBucketAnalysis(this.account, DayOfWeek.MONDAY, PlatformTimeInterval.FIFTEEN_MINUTE, AnalysisFilter.POINTS))
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .isEmpty();
    }


    //  ----------------- computeTradeDurationAnalysis -----------------

    @Test
    void test_computeTradeDurationAnalysis_success() {

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.analysisService.computeTradeDurationAnalysis(null, AnalysisFilter.PROFIT, TradeDurationFilter.ALL));

        assertThat(this.analysisService.computeTradeDurationAnalysis(this.account, AnalysisFilter.PROFIT, TradeDurationFilter.ALL))
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .element(0)
                .extracting("label", "value", "count")
                .containsExactly("5", 10.35, 2);

        assertThat(this.analysisService.computeTradeDurationAnalysis(this.account, AnalysisFilter.PROFIT, TradeDurationFilter.WINS))
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .element(0)
                .extracting("label", "value", "count")
                .containsExactly("5", 14.85, 1);

        assertThat(this.analysisService.computeTradeDurationAnalysis(this.account, AnalysisFilter.PROFIT, TradeDurationFilter.LOSSES))
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .element(0)
                .extracting("label", "value", "count")
                .containsExactly("5", -4.5, 1);
    }
}
