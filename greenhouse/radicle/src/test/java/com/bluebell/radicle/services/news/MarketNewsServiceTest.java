package com.bluebell.radicle.services.news;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.news.MarketNewsSeverity;
import com.bluebell.platform.enums.system.Country;
import com.bluebell.radicle.AbstractGenericTest;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.integration.models.dto.forexfactory.CalendarNewsDayDTO;
import com.bluebell.radicle.integration.models.dto.forexfactory.CalendarNewsDayEntryDTO;
import com.bluebell.radicle.integration.services.forexfactory.ForexFactoryIntegrationService;
import com.bluebell.radicle.repositories.news.MarketNewsRepository;
import com.bluebell.radicle.repositories.news.MarketNewsSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link MarketNewsService}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class MarketNewsServiceTest extends AbstractGenericTest {

    @Autowired
    private MarketNewsService marketNewsService;

    @MockBean
    private MarketNewsRepository marketNewsRepository;

    @MockBean
    private MarketNewsSlotRepository marketNewsSlotRepository;

    @MockBean
    private ForexFactoryIntegrationService forexFactoryIntegrationService;

    @BeforeEach
    public void setUp() {
        Mockito.when(this.marketNewsRepository.findNewsWithinInterval(any(), any())).thenReturn(List.of(generateMarketNews()));
        Mockito.when(this.marketNewsRepository.findById(1L)).thenReturn(Optional.of(generateMarketNews()));
        Mockito.when(this.marketNewsRepository.save(any())).thenReturn(generateMarketNews());
        Mockito.when(this.marketNewsRepository.findMarketNewsByDate(any())).thenReturn(generateMarketNews());
        Mockito.when(this.marketNewsSlotRepository.save(any())).thenReturn(generateTestMarketNewsSlot());
    }


    //  ----------------- findNewsWithinInterval -----------------

    @Test
    void test_findNewsWithinInterval_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.marketNewsService.findNewsWithinInterval(null, LocalDate.MAX, ""))
                .withMessage(CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.marketNewsService.findNewsWithinInterval(LocalDate.MIN, null, ""))
                .withMessage(CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_findNewsWithinIntervalWithLocales_basLocales() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.marketNewsService.findNewsWithinInterval(LocalDate.MIN, LocalDate.MAX, ""))
                .withMessage(CorePlatformConstants.Validation.DataIntegrity.BAD_LOCALE_ENUM);
    }

    @Test
    void test_findNewsWithinInterval_success() {
        assertThat(this.marketNewsService.findNewsWithinInterval(LocalDate.MIN, LocalDate.MAX))
                .isNotNull()
                .first()
                .extracting("date")
                .isEqualTo(LocalDate.of(2023, 1, 19));
    }

    @Test
    void test_findNewsWithinIntervalWithLocales_success() {
        assertThat(this.marketNewsService.findNewsWithinInterval(LocalDate.MIN, LocalDate.MAX, "CAN", "USD"))
                .isNotNull()
                .first()
                .extracting("date")
                .isEqualTo(LocalDate.of(2023, 1, 19));
    }


    //  ----------------- findMarketNewsForDate -----------------

    @Test
    void test_findMarketNewsForDate_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.marketNewsService.findMarketNewsForDate(null))
                .withMessage(CorePlatformConstants.Validation.DateTime.DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_findMarketNewsForDate_success() {
        assertThat(this.marketNewsService.findMarketNewsForDate(LocalDate.MIN))
                .isNotNull()
                .get()
                .extracting("date")
                .isEqualTo(LocalDate.of(2023, 1, 19));
    }


    //  ----------------- fetchMarketNews -----------------

    @Test
    void test_fetchMarketNews_failure() {
        Mockito.when(this.forexFactoryIntegrationService.getCurrentWeekNews()).thenReturn(List.of());
        assertThat(this.marketNewsService.fetchMarketNews())
                .isFalse();
    }

    @Test
    void test_fetchMarketNews_success() {

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
