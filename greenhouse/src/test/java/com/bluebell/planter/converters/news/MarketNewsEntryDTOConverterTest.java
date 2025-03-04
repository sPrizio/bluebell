package com.bluebell.planter.converters.news;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.news.MarketNewsEntryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Testing class for {@link MarketNewsEntryDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class MarketNewsEntryDTOConverterTest extends AbstractPlanterTest {

    @Autowired
    private MarketNewsEntryDTOConverter marketNewsEntryDTOConverter;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    void test_convert_success_emptyResult() {
        assertThat(this.marketNewsEntryDTOConverter.convert(null))
                .isNotNull()
                .satisfies(MarketNewsEntryDTO::isEmpty);

    }

    @Test
    void test_convert_success() {
        assertThat(this.marketNewsEntryDTOConverter.convert(generateTestMarketNewsEntry()))
                .isNotNull()
                .extracting("content", "severity")
                .containsExactly("Test News Entry", "Dangerous");
    }


    //  ----------------- convertAll -----------------

    @Test
    void test_convertAll_success() {
        assertThat(this.marketNewsEntryDTOConverter.convertAll(List.of(generateTestMarketNewsEntry())))
                .isNotEmpty()
                .first()
                .extracting("content", "severity")
                .containsExactly("Test News Entry", "Dangerous");
    }
}