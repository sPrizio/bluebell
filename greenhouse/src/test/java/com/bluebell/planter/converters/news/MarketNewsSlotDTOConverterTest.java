package com.bluebell.planter.converters.news;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.news.MarketNewsSlotDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Testing class for {@link MarketNewsSlotDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class MarketNewsSlotDTOConverterTest extends AbstractPlanterTest {

    @Autowired
    private MarketNewsSlotDTOConverter marketNewsSlotDTOConverter;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @MockitoBean
    private MarketNewsEntryDTOConverter marketNewsEntryDTOConverter;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.marketNewsEntryDTOConverter.convertAll(any())).thenReturn(Collections.emptyList());
    }


    //  ----------------- convert -----------------

    @Test
    void test_convert_success_emptyResult() {
        assertThat(this.marketNewsSlotDTOConverter.convert(null))
                .isNotNull()
                .satisfies(MarketNewsSlotDTO::isEmpty);

    }

    @Test
    void test_convert_success() {
        assertThat(this.marketNewsSlotDTOConverter.convert(generateTestMarketNewsSlot()))
                .isNotNull()
                .extracting("time")
                .isEqualTo(LocalTime.of(13, 10));
    }


    //  ----------------- convertAll -----------------

    @Test
    void test_convertAll_success() {
        assertThat(this.marketNewsSlotDTOConverter.convertAll(List.of(generateTestMarketNewsSlot())))
                .isNotEmpty()
                .first()
                .extracting("time")
                .isEqualTo(LocalTime.of(13, 10));
    }
}