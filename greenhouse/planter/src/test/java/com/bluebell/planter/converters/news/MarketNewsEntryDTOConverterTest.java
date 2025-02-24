package com.bluebell.planter.converters.news;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.news.MarketNewsEntryDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link MarketNewsEntryDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MarketNewsEntryDTOConverterTest extends AbstractPlanterTest {

    @Autowired
    private MarketNewsEntryDTOConverter marketNewsEntryDTOConverter;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.marketNewsEntryDTOConverter.convert(null))
                .isNotNull()
                .satisfies(MarketNewsEntryDTO::isEmpty);

    }

    /*@Test
    public void test_convert_success() {
        assertThat(this.marketNewsEntryDTOConverter.convert(generateTestMarketNewsEntry()))
                .isNotNull()
                .extracting("content", "severity")
                .containsExactly("Test News Entry", "Dangerous");
    }


    //  ----------------- convertAll -----------------

    @Test
    public void test_convertAll_success() {
        assertThat(this.marketNewsEntryDTOConverter.convertAll(List.of(generateTestMarketNewsEntry())))
                .isNotEmpty()
                .first()
                .extracting("content", "severity")
                .containsExactly("Test News Entry", "Dangerous");
    }*/
}