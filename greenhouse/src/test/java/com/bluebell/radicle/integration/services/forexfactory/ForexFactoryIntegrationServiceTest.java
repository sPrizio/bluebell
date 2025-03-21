package com.bluebell.radicle.integration.services.forexfactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.bluebell.radicle.integration.client.forexfactory.ForexFactoryIntegrationClient;
import com.bluebell.radicle.integration.models.dto.forexfactory.CalendarNewsDayEntryDTO;
import com.bluebell.radicle.integration.translators.forexfactory.CalendarNewsDayEntryTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;

/**
 * Testing class for {@link ForexFactoryIntegrationService}
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class ForexFactoryIntegrationServiceTest {

    @Mock
    private CalendarNewsDayEntryTranslator calendarNewsDayEntryTranslator;

    @Mock
    private ForexFactoryIntegrationClient forexFactoryIntegrationClient;

    @InjectMocks
    private ForexFactoryIntegrationService forexFactoryIntegrationService;

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(forexFactoryIntegrationService, "calendarUrl", "https://nfs.faireconomy.media/ff_calendar_thisweek.json");
        Mockito.when(this.forexFactoryIntegrationClient.get("https://nfs.faireconomy.media/ff_calendar_thisweek.json", new LinkedMultiValueMap<>())).thenReturn(
                """
                                [
                                    {
                                        "title":"Unemployment Rate",
                                        "country":"JPY",
                                        "date":"2023-05-29T19:30:00-04:00",
                                        "impact":"Low",
                                        "forecast":"2.7%",
                                        "previous":"2.8%"
                                    }
                                ]
                        """
        );

        Mockito.when(this.calendarNewsDayEntryTranslator.translate(any())).thenReturn(CalendarNewsDayEntryDTO.builder().title("Unemployment Rate").build());
    }


    //  ----------------- getCurrentWeekNews -----------------

    @Test
    void test_getCurrentWeekNews_success() {
        assertThat(this.forexFactoryIntegrationService.getCurrentWeekNews())
                .isNotNull();
    }
}
