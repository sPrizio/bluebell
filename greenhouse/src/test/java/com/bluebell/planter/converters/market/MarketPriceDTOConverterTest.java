package com.bluebell.planter.converters.market;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.market.MarketPriceDTO;
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
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link MarketPriceDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class MarketPriceDTOConverterTest extends AbstractPlanterTest {

    @Autowired
    private MarketPriceDTOConverter marketPriceDTOConverter;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    void test_convert_success_emptyResult() {
        assertThat(this.marketPriceDTOConverter.convert(null))
                .isNotNull()
                .satisfies(MarketPriceDTO::isEmpty);

    }

    @Test
    void test_convert_success() {
        assertThat(this.marketPriceDTOConverter.convert(generateTestMarketPrice()))
                .isNotNull()
                .extracting("open", "volume")
                .containsExactly(11234.05, 5689L);

    }


    //  ----------------- convertAll -----------------

    @Test
    void test_convertAll_success() {
        assertThat(this.marketPriceDTOConverter.convertAll(List.of(generateTestMarketPrice())))
                .isNotEmpty()
                .first()
                .extracting("open", "volume")
                .containsExactly(11234.05, 5689L);
    }
}
