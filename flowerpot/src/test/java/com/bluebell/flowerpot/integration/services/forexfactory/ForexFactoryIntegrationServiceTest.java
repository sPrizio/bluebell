package com.bluebell.flowerpot.integration.services.forexfactory;

import com.bluebell.flowerpot.integration.client.forexfactory.ForexFactoryIntegrationClient;
import com.bluebell.flowerpot.integration.models.dto.forexfactory.CalendarNewsDayEntryDTO;
import com.bluebell.flowerpot.integration.translators.forexfactory.CalendarNewsDayEntryTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link ForexFactoryIntegrationService}
 *
 * @author Stephen Prizio
 * @version 0.0.4
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ForexFactoryIntegrationServiceTest {

    @Mock
    private CalendarNewsDayEntryTranslator calendarNewsDayEntryTranslator;

    @Mock
    private ForexFactoryIntegrationClient forexFactoryIntegrationClient;

    @InjectMocks
    private ForexFactoryIntegrationService forexFactoryIntegrationService;

    @Before
    public void setUp() {

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

        CalendarNewsDayEntryDTO entry = new CalendarNewsDayEntryDTO();
        entry.setTitle("Unemployment Rate");

        Mockito.when(this.calendarNewsDayEntryTranslator.translate(any())).thenReturn(entry);
    }


    //  ----------------- getCurrentWeekNews -----------------

    @Test
    public void test_getCurrentWeekNews_success() {
        assertThat(this.forexFactoryIntegrationService.getCurrentWeekNews())
                .isNotNull();
    }
}
