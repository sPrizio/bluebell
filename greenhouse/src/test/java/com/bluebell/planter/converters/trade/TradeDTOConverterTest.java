package com.bluebell.planter.converters.trade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.api.dto.trade.TradeDTO;
import com.bluebell.platform.services.MathService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Testing class for {@link TradeDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class TradeDTOConverterTest extends AbstractPlanterTest {

    @Autowired
    private TradeDTOConverter tradeDTOConverter;

    @MockitoBean
    private MathService mathService;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.mathService.subtract(anyDouble(), anyDouble())).thenReturn(0.0);
    }


    //  ----------------- convert -----------------

    @Test
    void test_convert_success_emptyResult() {
        assertThat(this.tradeDTOConverter.convert(null))
                .isNotNull()
                .satisfies(TradeDTO::isEmpty);

    }

    @Test
    void test_convert_success() {
        assertThat(this.tradeDTOConverter.convert(generateTestBuyTrade()))
                .isNotNull()
                .extracting("tradeId", "tradePlatform", "tradeType", "tradeOpenTime", "tradeCloseTime", "lotSize", "openPrice", "closePrice", "netProfit")
                .containsExactly(
                        "testId1",
                        TradePlatform.CMC_MARKETS,
                        TradeType.BUY,
                        LocalDateTime.of(2022, 8, 24, 11, 32, 58),
                        LocalDateTime.of(2022, 8, 24, 11, 37, 24),
                        0.75,
                        13083.41,
                        13098.67,
                        14.85
                );

    }


    //  ----------------- convertAll -----------------

    @Test
    void test_convertAll_success() {
        assertThat(this.tradeDTOConverter.convertAll(List.of(generateTestBuyTrade())))
                .isNotEmpty()
                .first()
                .extracting("tradeId", "tradePlatform", "tradeType", "tradeOpenTime", "tradeCloseTime", "lotSize", "openPrice", "closePrice", "netProfit")
                .containsExactly(
                        "testId1",
                        TradePlatform.CMC_MARKETS,
                        TradeType.BUY,
                        LocalDateTime.of(2022, 8, 24, 11, 32, 58),
                        LocalDateTime.of(2022, 8, 24, 11, 37, 24),
                        0.75,
                        13083.41,
                        13098.67,
                        14.85
                );
    }
}
