package com.bluebell.planter.converters.trade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.trade.TradeDTO;
import com.bluebell.platform.services.MathService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Testing class for {@link TradeDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TradeDTOConverterTest extends AbstractPlanterTest {

    @Autowired
    private TradeDTOConverter tradeDTOConverter;

    @MockBean
    private MathService mathService;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.mathService.subtract(anyDouble(), anyDouble())).thenReturn(0.0);
    }


    //  ----------------- convert -----------------

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.tradeDTOConverter.convert(null))
                .isNotNull()
                .satisfies(TradeDTO::isEmpty);

    }

    /*@Test
    public void test_convert_success() {
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
    public void test_convertAll_success() {
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
    }*/
}
