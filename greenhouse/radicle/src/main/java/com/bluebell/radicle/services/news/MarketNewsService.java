package com.bluebell.radicle.services.news;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.account.Currency;
import com.bluebell.platform.enums.system.Country;
import com.bluebell.platform.models.core.entities.news.MarketNews;
import com.bluebell.platform.models.core.entities.news.MarketNewsEntry;
import com.bluebell.platform.models.core.entities.news.MarketNewsSlot;
import com.bluebell.radicle.integration.models.dto.forexfactory.CalendarNewsDayDTO;
import com.bluebell.radicle.integration.models.dto.forexfactory.CalendarNewsDayEntryDTO;
import com.bluebell.radicle.integration.services.forexfactory.ForexFactoryIntegrationService;
import com.bluebell.radicle.repositories.news.MarketNewsEntryRepository;
import com.bluebell.radicle.repositories.news.MarketNewsRepository;
import com.bluebell.radicle.repositories.news.MarketNewsSlotRepository;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;


/**
 * Service-layer for {@link MarketNews}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@Service
public class MarketNewsService {

    @Resource(name = "forexFactoryIntegrationService")
    private ForexFactoryIntegrationService forexFactoryIntegrationService;

    @Resource(name = "marketNewsRepository")
    private MarketNewsRepository marketNewsRepository;

    @Resource(name = "marketNewsEntryRepository")
    private MarketNewsEntryRepository marketNewsEntryRepository;

    @Resource(name = "marketNewsSlotRepository")
    private MarketNewsSlotRepository marketNewsSlotRepository;


    //  METHODS

    /**
     * Returns a {@link List} of {@link MarketNews} within the given start & end dates
     *
     * @param start   {@link LocalDate} start
     * @param end     {@link LocalDate} end
     * @param locales locales / currencies
     * @return {@link List} of {@link MarketNews}
     */
    public List<MarketNews> findNewsWithinInterval(final LocalDate start, final LocalDate end, final String... locales) {

        validateParameterIsNotNull(start, CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);

        final List<MarketNews> marketNews = this.marketNewsRepository.findNewsWithinInterval(start, end);
        if (locales == null || locales.length == 0) {
            return marketNews;
        }

        if (!validateLocales(locales)) {
            throw new UnsupportedOperationException(CorePlatformConstants.Validation.DataIntegrity.BAD_LOCALE_ENUM);
        }

        marketNews.forEach(n -> {
            final List<MarketNewsSlot> slots = new ArrayList<>(n.getSlots());
            slots.forEach(s -> {
                final List<MarketNewsEntry> entries =
                        s.getEntries()
                                .stream()
                                .filter(e -> isValidLocale(e, locales))
                                .toList();
                s.setEntries(entries);
            });

            n.setSlots(new ArrayList<>(slots.stream().filter(s -> CollectionUtils.isNotEmpty(s.getEntries())).toList()));
        });

        return marketNews.stream().filter(n -> !n.getSlots().isEmpty()).toList();
    }

    /**
     * Returns an {@link Optional} {@link MarketNews} for the given date
     *
     * @param date {@link LocalDate}
     * @return {@link Optional} {@link MarketNews}
     */
    public Optional<MarketNews> findMarketNewsForDate(final LocalDate date) {
        validateParameterIsNotNull(date, CorePlatformConstants.Validation.DateTime.DATE_CANNOT_BE_NULL);
        return Optional.ofNullable(this.marketNewsRepository.findMarketNewsByDate(date));
    }

    /**
     * Pulls in market news data from Forex Factory
     *
     * @return true if no errors occurred
     */
    public boolean fetchMarketNews() {

        final List<CalendarNewsDayDTO> news = this.forexFactoryIntegrationService.getCurrentWeekNews();
        if (CollectionUtils.isEmpty(news)) {
            return false;
        }

        news.stream().filter(n -> CollectionUtils.isNotEmpty(n.getEntries())).forEach(day -> {
            final MarketNews marketNews = this.marketNewsRepository.save(getNews(day.getDate()));
            final Map<LocalTime, List<CalendarNewsDayEntryDTO>> map = new HashMap<>();
            final List<MarketNewsSlot> marketNewsSlots = new ArrayList<>();


            day.getEntries().forEach(entryDTO -> {
                final List<CalendarNewsDayEntryDTO> entryDTOS;
                if (map.containsKey(entryDTO.getTime())) {
                    entryDTOS = new ArrayList<>(map.get(entryDTO.getTime()));
                } else {
                    entryDTOS = new ArrayList<>();
                }

                entryDTOS.add(entryDTO);
                map.put(entryDTO.getTime(), entryDTOS);
            });

            map.forEach((key, value) -> {
                final MarketNewsSlot marketNewsSlot = this.marketNewsSlotRepository.save(getSlot(marketNews, key));
                marketNewsSlot.setNews(marketNews);
                marketNewsSlot.setTime(key);

                final List<MarketNewsEntry> marketNewsEntries = new ArrayList<>();
                value.forEach(val -> {
                    final MarketNewsEntry marketNewsEntry = this.marketNewsEntryRepository.save(getEntry(marketNewsSlot, val.getTitle()));
                    marketNewsEntry.setSlot(marketNewsSlot);
                    marketNewsEntry.setCountry(val.getCountry());
                    marketNewsEntry.setPrevious(val.getPrevious());
                    marketNewsEntry.setForecast(val.getForecast());
                    marketNewsEntry.setSeverity(val.getImpact());
                    marketNewsEntry.setContent(val.getTitle());
                    this.marketNewsEntryRepository.save(marketNewsEntry);
                    marketNewsEntries.add(marketNewsEntry);
                });

                marketNewsSlot.setEntries(marketNewsEntries);
                marketNewsSlots.add(this.marketNewsSlotRepository.save(marketNewsSlot));
            });

            marketNews.setDate(day.getDate());
            marketNews.setSlots(marketNewsSlots);
            this.marketNewsRepository.save(marketNews);
        });

        return true;
    }

    /**
     * Looks up a {@link MarketNews} for the given date or returns a new instance
     *
     * @param localDate date
     * @return {@link MarketNews}
     */
    private MarketNews getNews(final LocalDate localDate) {
        final Optional<MarketNews> news = findMarketNewsForDate(localDate);
        return news.orElseGet(MarketNews::new);
    }

    /**
     * Looks up a {@link MarketNewsSlot} for the given {@link MarketNews} and {@link LocalTime}
     * or returns a new instance
     *
     * @param news {@link MarketNews}
     * @param time {@link LocalTime}
     * @return {@link MarketNewsSlot}
     */
    private MarketNewsSlot getSlot(final MarketNews news, final LocalTime time) {

        if (news == null || CollectionUtils.isEmpty(news.getSlots())) {
            return new MarketNewsSlot();
        }

        return news.getSlots().stream().filter(slot -> slot.getTime().equals(time)).findFirst().orElse(new MarketNewsSlot());
    }

    /**
     * Looks up a {@link MarketNewsEntry} for the given {@link MarketNewsSlot} and title
     *
     * @param slot  {@link MarketNewsSlot}
     * @param title content
     * @return {@link MarketNewsEntry}
     */
    private MarketNewsEntry getEntry(final MarketNewsSlot slot, final String title) {

        if (slot == null || CollectionUtils.isEmpty(slot.getEntries())) {
            return new MarketNewsEntry();
        }

        return slot.getEntries().stream().filter(entry -> entry.getContent().equals(title)).findFirst().orElse(new MarketNewsEntry());
    }

    /**
     * Checks if the given strings are valid {@link Currency}s or {@link Country}s
     *
     * @param testLocales test enums
     * @return true if a country or currency
     */
    private boolean validateLocales(final String[] testLocales) {

        if (Arrays.stream(testLocales).anyMatch(s -> s.equalsIgnoreCase(Country.ALL_COUNTRIES.getIsoCode()))) {
            return true;
        }

        return Arrays.stream(testLocales).allMatch(l -> Currency.get(l) != Currency.NOT_APPLICABLE || Country.getByIsoCode(l) != Country.NOT_APPLICABLE);
    }

    /**
     * Validates that the given {@link MarketNewsEntry} pertains to one of the given test locales
     *
     * @param entry {@link MarketNewsEntry}
     * @param testLocales test locales
     * @return true if valid
     */
    private boolean isValidLocale(final MarketNewsEntry entry, final String[] testLocales) {

        if (Arrays.stream(testLocales).anyMatch(s -> s.equalsIgnoreCase(Country.ALL_COUNTRIES.getIsoCode()))) {
            return true;
        }

        for (final String test : testLocales) {
            Country testCountry = Country.getByIsoCode(test);
            Currency testCurrency = Currency.get(test);

            if (entry.getCountry().equals(testCountry) || entry.getCountry().getCurrency().equals(testCurrency)) {
                return true;
            }
        }

        return false;
    }
}
