package com.bluebell.planter.converters.news;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.news.MarketNewsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link MarketNewsDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class MarketNewsDTOConverterTest extends AbstractPlanterTest {

    @Autowired
    private MarketNewsDTOConverter marketNewsDTOConverter;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @MockitoBean
    private MarketNewsSlotDTOConverter marketNewsSlotDTOConverter;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.marketNewsSlotDTOConverter.convertAll(any())).thenReturn(Collections.emptyList());
    }


    //  ----------------- convert -----------------

    @Test
    void test_convert_success_emptyResult() {
        assertThat(this.marketNewsDTOConverter.convert(null))
                .isNotNull()
                .satisfies(MarketNewsDTO::isEmpty);

    }

    @Test
    void test_convert_success() {
        assertThat(this.marketNewsDTOConverter.convert(generateMarketNews()))
                .isNotNull()
                .extracting("date")
                .isEqualTo(LocalDate.of(2023, 1, 19));
    }


    //  ----------------- convertAll -----------------

    @Test
    void test_convertAll_success() {
        assertThat(this.marketNewsDTOConverter.convertAll(List.of(generateMarketNews())))
                .isNotEmpty()
                .first()
                .extracting("date")
                .isEqualTo(LocalDate.of(2023, 1, 19));
    }
}