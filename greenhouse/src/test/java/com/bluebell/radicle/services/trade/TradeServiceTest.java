package com.bluebell.radicle.services.trade;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.repositories.trade.TradeRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link TradeService}
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class TradeServiceTest extends AbstractGenericTest {

    private final Account TEST_ACCOUNT = generateTestAccount();

    private final Portfolio TEST_PORTFOLIO = generateTestPortfolio();

    private final User TEST_USER = generateTestUser();

    private static final LocalDateTime TEST1 = LocalDate.of(2022, 8, 24).atStartOfDay();
    private static final LocalDateTime TEST2 = LocalDate.of(2022, 8, 25).atStartOfDay();

    private final Trade TEST_TRADE_1 = generateTestBuyTrade();
    private final Trade TEST_TRADE_2 = generateTestSellTrade();

    @MockitoBean
    private TradeRepository tradeRepository;

    @Autowired
    private TradeService tradeService;

    @BeforeEach
    void setUp() {
        TEST_PORTFOLIO.setAccounts(List.of(TEST_ACCOUNT));
        TEST_USER.setPortfolios(List.of(TEST_PORTFOLIO));
        Mockito.when(this.tradeRepository.findAllByTradeTypeAndAccountOrderByTradeOpenTimeAsc(TradeType.BUY, TEST_ACCOUNT)).thenReturn(List.of(TEST_TRADE_1));
        Mockito.when(this.tradeRepository.findAllTradesWithinDate(TEST1, TEST1.plusYears(1).toLocalDate().atStartOfDay(), TEST_ACCOUNT)).thenReturn(List.of(TEST_TRADE_1, TEST_TRADE_2));
        Mockito.when(this.tradeRepository.findAllTradesWithinDate(TEST1, TEST2, TEST_ACCOUNT)).thenReturn(List.of(TEST_TRADE_1, TEST_TRADE_2));
        Mockito.when(this.tradeRepository.findTradeByTradeIdAndAccount("testId1", TEST_ACCOUNT)).thenReturn(TEST_TRADE_1);
        Mockito.when(this.tradeRepository.findAllTradesWithinDatePaged(any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(TEST_TRADE_1, TEST_TRADE_2)));
        Mockito.when(this.tradeRepository.findTradeByTradeIdAndAccount("123", TEST_ACCOUNT)).thenReturn(generateTestBuyTrade());
    }


    //  ----------------- findAllByTradeType -----------------

    @Test
    void test_findAllByTradeType_missingParamTradeType() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllByTradeType(null, generateTestAccount()))
                .withMessage("tradeType cannot be null");
    }

    @Test
    void test_findAllByTradeType_success() {
        assertThat(this.tradeService.findAllByTradeType(TradeType.BUY, TEST_ACCOUNT))
                .hasSize(1)
                .extracting("openPrice", "closePrice", "netProfit")
                .containsExactly(Tuple.tuple(13083.41, 13098.67, 14.85));
    }


    //  ----------------- findAllTradesWithinTimespan -----------------

    @Test
    void test_findAllTradesWithinTimespan_missingParamStart() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesWithinTimespan(null, LocalDateTime.MAX, generateTestAccount()))
                .withMessage(CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_findAllTradesWithinTimespan_missingParamEnd() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesWithinTimespan(LocalDateTime.MAX, null, generateTestAccount()))
                .withMessage(CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_findAllTradesWithinTimespan_invalidInterval() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesWithinTimespan(LocalDateTime.MAX, LocalDateTime.MIN, generateTestAccount()))
                .withMessage(CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    void test_findAllTradesWithinTimespan_success() {
        assertThat(this.tradeService.findAllTradesWithinTimespan(TEST1, TEST2, TEST_ACCOUNT))
                .hasSize(2)
                .extracting("openPrice", "closePrice", "netProfit")
                .contains(Tuple.tuple(13083.41, 13098.67, 14.85), Tuple.tuple(13160.09, 13156.12, -4.50));
    }


    //  ----------------- findAllTradesWithinTimespan (paged) -----------------

    @Test
    void test_findAllTradesWithinTimespan_paged_missingParamStart() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesWithinTimespan(null, LocalDateTime.MAX, generateTestAccount(), 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .withMessage(CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_findAllTradesWithinTimespan_paged_missingParamEnd() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesWithinTimespan(LocalDateTime.MAX, null, generateTestAccount(), 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .withMessage(CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_findAllTradesWithinTimespan_paged_invalidInterval() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesWithinTimespan(LocalDateTime.MAX, LocalDateTime.MIN, generateTestAccount(), 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .withMessage(CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    void test_findAllTradesWithinTimespan_paged_success() {
        assertThat(this.tradeService.findAllTradesWithinTimespan(TEST1, TEST2, generateTestAccount(), 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .hasSize(2)
                .extracting("openPrice", "closePrice", "netProfit")
                .contains(Tuple.tuple(13083.41, 13098.67, 14.85), Tuple.tuple(13160.09, 13156.12, -4.50));
    }


    //  ----------------- findTradeByTradeId -----------------

    @Test
    void test_findTradeByTradeId_missingParamStart() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findTradeByTradeId(null, TEST_ACCOUNT))
                .withMessage("tradeId cannot be null");
    }

    @Test
    void test_findTradeByTradeId_success() {
        assertThat(this.tradeService.findTradeByTradeId("testId1", TEST_ACCOUNT))
                .map(Trade::getTradeId)
                .hasValue("testId1");
    }
}
