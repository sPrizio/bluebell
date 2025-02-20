package com.bluebell.planter.core.services.news;

import com.bluebell.planter.AbstractGenericTest;
import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.platform.enums.news.MarketNewsSeverity;
import com.bluebell.planter.core.enums.system.Country;
import com.bluebell.planter.core.exceptions.system.EntityCreationException;
import com.bluebell.planter.core.exceptions.system.EntityModificationException;
import com.bluebell.planter.core.exceptions.validation.IllegalParameterException;
import com.bluebell.planter.core.exceptions.validation.MissingRequiredDataException;
import com.bluebell.planter.core.repositories.news.MarketNewsRepository;
import com.bluebell.planter.core.repositories.news.MarketNewsSlotRepository;
import com.bluebell.planter.core.services.platform.UniqueIdentifierService;
import com.bluebell.planter.integration.models.dto.forexfactory.CalendarNewsDayDTO;
import com.bluebell.planter.integration.models.dto.forexfactory.CalendarNewsDayEntryDTO;
import com.bluebell.planter.integration.services.forexfactory.ForexFactoryIntegrationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link MarketNewsService}
 *
 * @author Stephen Prizio
 * @version 0.0.4
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MarketNewsServiceTest extends AbstractGenericTest {

    @Autowired
    private MarketNewsService marketNewsService;

    @MockBean
    private MarketNewsRepository marketNewsRepository;

    @MockBean
    private MarketNewsSlotRepository marketNewsSlotRepository;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @MockBean
    private ForexFactoryIntegrationService forexFactoryIntegrationService;

    @Before
    public void setUp() {
        Mockito.when(this.marketNewsRepository.findNewsWithinInterval(any(), any())).thenReturn(List.of(generateMarketNews()));
        Mockito.when(this.marketNewsRepository.findById(1L)).thenReturn(Optional.of(generateMarketNews()));
        Mockito.when(this.marketNewsRepository.save(any())).thenReturn(generateMarketNews());
        Mockito.when(this.marketNewsRepository.findMarketNewsByDate(any())).thenReturn(generateMarketNews());
        Mockito.when(this.marketNewsSlotRepository.save(any())).thenReturn(generateTestMarketNewsSlot());
        Mockito.when(this.uniqueIdentifierService.retrieveIdForUid("test")).thenReturn(1L);
    }


    //  ----------------- findNewsWithinInterval -----------------

    @Test
    public void test_findNewsWithinInterval_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.marketNewsService.findNewsWithinInterval(null, LocalDate.MAX, ""))
                .withMessage(CoreConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.marketNewsService.findNewsWithinInterval(LocalDate.MIN, null, ""))
                .withMessage(CoreConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    public void test_findNewsWithinIntervalWithLocales_basLocales() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.marketNewsService.findNewsWithinInterval(LocalDate.MIN, LocalDate.MAX, ""))
                .withMessage(CoreConstants.Validation.DataIntegrity.BAD_LOCALE_ENUM);
    }

    @Test
    public void test_findNewsWithinInterval_success() {
        assertThat(this.marketNewsService.findNewsWithinInterval(LocalDate.MIN, LocalDate.MAX))
                .isNotNull()
                .first()
                .extracting("date")
                .isEqualTo(LocalDate.of(2023, 1, 19));
    }

    @Test
    public void test_findNewsWithinIntervalWithLocales_success() {
        assertThat(this.marketNewsService.findNewsWithinInterval(LocalDate.MIN, LocalDate.MAX, "CAN", "USD"))
                .isNotNull()
                .first()
                .extracting("date")
                .isEqualTo(LocalDate.of(2023, 1, 19));
    }


    //  ----------------- findMarketNewsForDate -----------------

    @Test
    public void test_findMarketNewsForDate_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.marketNewsService.findMarketNewsForDate(null))
                .withMessage(CoreConstants.Validation.DateTime.DATE_CANNOT_BE_NULL);
    }

    @Test
    public void test_findMarketNewsForDate_success() {
        assertThat(this.marketNewsService.findMarketNewsForDate(LocalDate.MIN))
                .isNotNull()
                .get()
                .extracting("date")
                .isEqualTo(LocalDate.of(2023, 1, 19));
    }


    //  ----------------- createMarketNews -----------------

    @Test
    public void test_createMarketNews_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.marketNewsService.createMarketNews(null))
                .withMessage("The required data for creating a MarketNews entity was null or empty");
    }

    @Test
    public void test_createMarketNews_erroneousCreation() {
        Map<String, Object> map = Map.of("bad", "input");
        assertThatExceptionOfType(EntityCreationException.class)
                .isThrownBy(() -> this.marketNewsService.createMarketNews(map))
                .withMessage("A MarketNews could not be created : Cannot invoke \"java.util.Map.get(Object)\" because \"news\" is null");
    }

    @Test
    public void test_createMarketNews_success() {

        Map<String, Object> data =
                Map.of(
                        "marketNews",
                        Map.of(
                                "date", "2022-09-05",
                                "slots", List.of(
                                        Map.of(
                                                "time", LocalTime.of(14, 0, 0),
                                                "entries", List.of(
                                                        Map.of(
                                                                "content", "Test News Entry 1",
                                                                "severity", 3
                                                        ),
                                                        Map.of(
                                                                "content", "Test News Entry 2",
                                                                "severity", 2
                                                        )
                                                )
                                        ),
                                        Map.of(
                                                "time", LocalTime.of(8, 30, 0),
                                                "entries", List.of(
                                                        Map.of(
                                                                "content", "Test News Entry 3",
                                                                "severity", 1
                                                        )
                                                )
                                        )
                                )
                        )
                );

        assertThat(this.marketNewsService.createMarketNews(data))
                .isNotNull()
                .extracting("date")
                .isEqualTo(LocalDate.of(2023, 1, 19));
    }


    //  ----------------- updateMarketNews -----------------

    @Test
    public void test_updateMarketNews_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.marketNewsService.updateMarketNews("test", null))
                .withMessage("The required data for updating a MarketNews entity was null or empty");
    }

    @Test
    public void test_updateMarketNews_erroneousUpdate() {
        Map<String, Object> map = Map.of("bad", "input");
        assertThatExceptionOfType(EntityModificationException.class)
                .isThrownBy(() -> this.marketNewsService.updateMarketNews("test", map))
                .withMessage("An error occurred while modifying the MarketNews : Cannot invoke \"java.util.Map.get(Object)\" because \"news\" is null");
    }

    @Test
    public void test_updateMarketNews_success() {

        Map<String, Object> data =
                Map.of(
                        "marketNews",
                        Map.of(
                                "date", "2022-09-05",
                                "slots", List.of(
                                        Map.of(
                                                "time", LocalTime.of(14, 0, 0),
                                                "entries", List.of(
                                                        Map.of(
                                                                "content", "Test News Entry 1",
                                                                "severity", 3
                                                        ),
                                                        Map.of(
                                                                "content", "Test News Entry 2",
                                                                "severity", 2
                                                        )
                                                )
                                        ),
                                        Map.of(
                                                "time", LocalTime.of(8, 30, 0),
                                                "entries", List.of(
                                                        Map.of(
                                                                "content", "Test News Entry 3",
                                                                "severity", 1
                                                        )
                                                )
                                        )
                                )
                        )
                );

        assertThat(this.marketNewsService.updateMarketNews("test", data))
                .isNotNull()
                .extracting("date")
                .isEqualTo(LocalDate.of(2023, 1, 19));
    }


    //  ----------------- deleteRetrospective -----------------

    @Test
    public void test_deleteMarketNews_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.marketNewsService.deleteMarketNews(null))
                .withMessage(CoreConstants.Validation.DataIntegrity.UID_CANNOT_BE_NULL);
    }

    @Test
    public void test_deleteMarketNews_unknown_success() {
        assertThat(this.marketNewsService.deleteMarketNews("unknown"))
                .isFalse();
    }

    @Test
    public void test_deleteMarketNews_success() {
        assertThat(this.marketNewsService.deleteMarketNews("test"))
                .isTrue();
    }


    //  ----------------- fetchMarketNews -----------------

    @Test
    public void test_fetchMarketNews_failure() {
        Mockito.when(this.forexFactoryIntegrationService.getCurrentWeekNews()).thenReturn(List.of());
        assertThat(this.marketNewsService.fetchMarketNews())
                .isFalse();
    }

    @Test
    public void test_fetchMarketNews_success() {

        final CalendarNewsDayEntryDTO entryDTO = new CalendarNewsDayEntryDTO();
        entryDTO.setTitle("Test");
        entryDTO.setTime(LocalTime.MIN);
        entryDTO.setImpact(MarketNewsSeverity.DANGEROUS);
        entryDTO.setCountry(Country.CANADA);
        entryDTO.setForecast("-2.5%");
        entryDTO.setPrevious("-2.9%");

        final CalendarNewsDayDTO dto = new CalendarNewsDayDTO();
        dto.setDate(LocalDate.MIN);
        dto.setEntries(List.of(entryDTO));

        Mockito.when(this.forexFactoryIntegrationService.getCurrentWeekNews()).thenReturn(List.of(dto));
        assertThat(this.marketNewsService.fetchMarketNews())
                .isTrue();
    }
}
