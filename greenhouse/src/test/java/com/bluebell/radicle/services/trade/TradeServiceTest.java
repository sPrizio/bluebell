package com.bluebell.radicle.services.trade;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.api.dto.trade.CreateUpdateTradeDTO;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link TradeService}
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class TradeServiceTest extends AbstractGenericTest {

    private static final String TEST_SYMBOL = "test_symbol";

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
        Mockito.when(this.tradeRepository.findAllTradesForSymbolWithinDatePaged(any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(TEST_TRADE_1, TEST_TRADE_2)));
        Mockito.when(this.tradeRepository.findAllTradesForTypeWithinDatePaged(any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(TEST_TRADE_1, TEST_TRADE_2)));
        Mockito.when(this.tradeRepository.findAllTradesForSymbolAndTypeWithinDatePaged(any(), any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(TEST_TRADE_1, TEST_TRADE_2)));
        Mockito.when(this.tradeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
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


    //  ----------------- findAllTradesForSymbolWithinTimespan (paged) -----------------

    @Test
    void test_findAllTradesForSymbolWithinTimespan_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesForSymbolWithinTimespan(null, LocalDateTime.MAX, generateTestAccount(), TEST_SYMBOL, 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .withMessage(CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesForSymbolWithinTimespan(LocalDateTime.MIN, null, generateTestAccount(), TEST_SYMBOL, 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .withMessage(CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesForSymbolWithinTimespan(LocalDateTime.MAX, LocalDateTime.MIN, generateTestAccount(), TEST_SYMBOL, 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .withMessage(CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesForSymbolWithinTimespan(LocalDateTime.MIN, LocalDateTime.MAX, generateTestAccount(), null, 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .withMessage(CorePlatformConstants.Validation.MarketPrice.SYMBOL_CANNOT_BE_NULL);
    }

    @Test
    void test_findAllTradesForSymbolWithinTimespan_success() {
        assertThat(this.tradeService.findAllTradesForSymbolWithinTimespan(TEST1, TEST2, generateTestAccount(), TEST_SYMBOL, 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .hasSize(2)
                .extracting("openPrice", "closePrice", "netProfit")
                .contains(Tuple.tuple(13083.41, 13098.67, 14.85), Tuple.tuple(13160.09, 13156.12, -4.50));
    }


    //  ----------------- findAllTradesForTradeTypeWithinTimespan (paged) -----------------

    @Test
    void test_findAllTradesForTradeTypeWithinTimespan_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesForTradeTypeWithinTimespan(null, LocalDateTime.MAX, generateTestAccount(), TradeType.BUY, 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .withMessage(CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesForTradeTypeWithinTimespan(LocalDateTime.MIN, null, generateTestAccount(), TradeType.BUY, 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .withMessage(CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesForTradeTypeWithinTimespan(LocalDateTime.MAX, LocalDateTime.MIN, generateTestAccount(), TradeType.BUY, 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .withMessage(CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesForTradeTypeWithinTimespan(LocalDateTime.MIN, LocalDateTime.MAX, generateTestAccount(), null, 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .withMessage(CorePlatformConstants.Validation.Trade.TRADE_TYPE_CANNOT_BE_NULL);
    }

    @Test
    void test_findAllTradesForTradeTypeWithinTimespan_success() {
        assertThat(this.tradeService.findAllTradesForSymbolWithinTimespan(TEST1, TEST2, generateTestAccount(), TEST_SYMBOL, 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .hasSize(2)
                .extracting("openPrice", "closePrice", "netProfit")
                .contains(Tuple.tuple(13083.41, 13098.67, 14.85), Tuple.tuple(13160.09, 13156.12, -4.50));
    }


    //  ----------------- findAllTradesForSymbolAndTradeTypeWithinTimespan (paged) -----------------

    @Test
    void test_findAllTradesForSymbolAndTradeTypeWithinTimespan_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesForSymbolAndTradeTypeWithinTimespan(null, LocalDateTime.MAX, generateTestAccount(), TEST_SYMBOL, TradeType.BUY, 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .withMessage(CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesForSymbolAndTradeTypeWithinTimespan(LocalDateTime.MIN, null, generateTestAccount(), TEST_SYMBOL, TradeType.BUY, 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .withMessage(CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesForSymbolAndTradeTypeWithinTimespan(LocalDateTime.MAX, LocalDateTime.MIN, generateTestAccount(), TEST_SYMBOL, TradeType.BUY, 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .withMessage(CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesForSymbolAndTradeTypeWithinTimespan(LocalDateTime.MIN, LocalDateTime.MAX, generateTestAccount(), null, TradeType.BUY, 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .withMessage(CorePlatformConstants.Validation.MarketPrice.SYMBOL_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesForSymbolAndTradeTypeWithinTimespan(LocalDateTime.MIN, LocalDateTime.MAX, generateTestAccount(), TEST_SYMBOL, null, 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
                .withMessage(CorePlatformConstants.Validation.Trade.TRADE_TYPE_CANNOT_BE_NULL);
    }

    @Test
    void test_findAllTradesForSymbolAndTradeTypeWithinTimespan_success() {
        assertThat(this.tradeService.findAllTradesForSymbolAndTradeTypeWithinTimespan(TEST1, TEST2, generateTestAccount(), "test", TradeType.BUY, 0, 10, Sort.by(Sort.Direction.ASC, "tradeOpenTime", "tradeCloseTime")))
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


    //  ----------------- createNewTrade -----------------

    @Test
    void test_createNewTrade_missingAccount() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.createNewTrade(null, null))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    void test_createNewTrade_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.tradeService.createNewTrade(null, generateTestAccount()))
                .withMessage("The required data for creating a Trade entity was null or empty");
    }

    @Test
    void test_createNewTrade_success() {

        final CreateUpdateTradeDTO data = CreateUpdateTradeDTO
                .builder()
                .tradeId("123LOL")
                .tradePlatform(TradePlatform.BLUEBELL.getCode())
                .product("Test Equity")
                .tradeType(TradeType.BUY.getCode())
                .closePrice(125.36)
                .tradeCloseTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .tradeOpenTime(LocalDateTime.now().minusMinutes(5).format(DateTimeFormatter.ISO_DATE_TIME))
                .lotSize(0.25)
                .netProfit(89.63)
                .openPrice(102.74)
                .stopLoss(89.55)
                .takeProfit(125.36)
                .build();

        assertThat(this.tradeService.createNewTrade(data, generateTestAccount()))
                .isNotNull()
                .extracting("tradeId", "openPrice", "tradePlatform")
                .containsExactly("123LOL", 102.74, TradePlatform.BLUEBELL);
    }

    @Test
    void test_createNewTrade_success_randomId() {

        final CreateUpdateTradeDTO data = CreateUpdateTradeDTO
                .builder()
                .tradePlatform(TradePlatform.BLUEBELL.getCode())
                .product("Test Equity")
                .tradeType(TradeType.BUY.getCode())
                .closePrice(125.36)
                .tradeCloseTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .tradeOpenTime(LocalDateTime.now().minusMinutes(5).format(DateTimeFormatter.ISO_DATE_TIME))
                .lotSize(0.25)
                .netProfit(89.63)
                .openPrice(102.74)
                .stopLoss(89.55)
                .takeProfit(125.36)
                .build();

        final Trade trade = this.tradeService.createNewTrade(data, generateTestAccount());
        assertThat(trade)
                .isNotNull()
                .extracting("takeProfit", "openPrice", "tradePlatform")
                .containsExactly(125.36, 102.74, TradePlatform.BLUEBELL);

        assertThat(trade.getTradeId()).startsWith("B-");
    }


    //  ----------------- updateTrade -----------------

    @Test
    void test_updateTrade_missingTrade() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.updateTrade(null, null, null))
                .withMessage(CorePlatformConstants.Validation.Trade.TRADE_CANNOT_BE_NULL);
    }

    @Test
    void test_updateTrade_missingAccount() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.updateTrade(generateTestBuyTrade(), null, null))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    void test_updateTrade_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.tradeService.updateTrade(generateTestBuyTrade(), CreateUpdateTradeDTO.builder().build(), generateTestAccount()))
                .withMessage("The required data for updating a Trade entity was null or empty");
    }

    @Test
    void test_updateTrade_success() {

        final CreateUpdateTradeDTO data = CreateUpdateTradeDTO
                .builder()
                .tradeId("123LOL")
                .tradePlatform(TradePlatform.BLUEBELL.getCode())
                .product("Test Equity")
                .tradeType(TradeType.BUY.getCode())
                .closePrice(125.36)
                .tradeCloseTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .tradeOpenTime(LocalDateTime.now().minusMinutes(5).format(DateTimeFormatter.ISO_DATE_TIME))
                .lotSize(0.25)
                .netProfit(89.63)
                .openPrice(102.74)
                .stopLoss(89.55)
                .takeProfit(125.36)
                .build();

        assertThat(this.tradeService.updateTrade(generateTestBuyTrade(), data, generateTestAccount()))
                .isNotNull()
                .extracting("tradeId", "openPrice", "tradePlatform")
                .containsExactly("123LOL", 102.74, TradePlatform.BLUEBELL);
    }
}
