package com.bluebell.planter.core.services.news;

import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.account.Currency;
import com.bluebell.platform.enums.news.MarketNewsSeverity;
import com.bluebell.planter.core.enums.system.Country;
import com.bluebell.planter.core.exceptions.system.EntityCreationException;
import com.bluebell.planter.core.exceptions.system.EntityModificationException;
import com.bluebell.planter.core.exceptions.validation.MissingRequiredDataException;
import com.bluebell.planter.core.exceptions.validation.NoResultFoundException;
import com.bluebell.planter.core.models.entities.news.MarketNews;
import com.bluebell.planter.core.models.entities.news.MarketNewsEntry;
import com.bluebell.planter.core.models.entities.news.MarketNewsSlot;
import com.bluebell.planter.core.repositories.news.MarketNewsEntryRepository;
import com.bluebell.planter.core.repositories.news.MarketNewsRepository;
import com.bluebell.planter.core.repositories.news.MarketNewsSlotRepository;
import com.bluebell.planter.core.services.platform.UniqueIdentifierService;
import com.bluebell.planter.integration.models.dto.forexfactory.CalendarNewsDayDTO;
import com.bluebell.planter.integration.models.dto.forexfactory.CalendarNewsDayEntryDTO;
import com.bluebell.planter.integration.services.forexfactory.ForexFactoryIntegrationService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.bluebell.planter.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link MarketNews}
 *
 * @author Stephen Prizio
 * @version 0.0.4
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

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


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

        validateParameterIsNotNull(start, CoreConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);

        final List<MarketNews> marketNews = this.marketNewsRepository.findNewsWithinInterval(start, end);
        if (locales == null || locales.length == 0) {
            return marketNews;
        }

        if (!validateLocales(locales)) {
            throw new UnsupportedOperationException(CoreConstants.Validation.DataIntegrity.BAD_LOCALE_ENUM);
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
        validateParameterIsNotNull(date, CoreConstants.Validation.DateTime.DATE_CANNOT_BE_NULL);
        return Optional.ofNullable(this.marketNewsRepository.findMarketNewsByDate(date));
    }

    /**
     * Returns a {@link MarketNews} for the given uid
     *
     * @param uid uid
     * @return {@link Optional} {@link MarketNews}
     */
    public Optional<MarketNews> findMarketNewsForUid(final String uid) {
        validateParameterIsNotNull(uid, CoreConstants.Validation.DataIntegrity.UID_CANNOT_BE_NULL);
        return this.marketNewsRepository.findById(this.uniqueIdentifierService.retrieveIdForUid(uid));
    }

    /**
     * Creates a new {@link MarketNews} from the given {@link Map} of data
     *
     * @param data {@link Map}
     * @return newly created {@link MarketNews}
     */
    public MarketNews createMarketNews(final Map<String, Object> data) {

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for creating a MarketNews entity was null or empty");
        }

        try {
            return applyChanges(new MarketNews(), data);
        } catch (Exception e) {
            throw new EntityCreationException(String.format("A MarketNews could not be created : %s", e.getMessage()), e);
        }
    }

    /**
     * Updates an existing {@link MarketNews} with the given {@link Map} of data. Update methods are designed to be idempotent
     *
     * @param uid  String uid
     * @param data {@link Map}
     * @return updated {@link MarketNews}
     */
    public MarketNews updateMarketNews(final String uid, final Map<String, Object> data) {

        validateParameterIsNotNull(uid, CoreConstants.Validation.DataIntegrity.UID_CANNOT_BE_NULL);

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for updating a MarketNews entity was null or empty");
        }

        try {
            MarketNews marketNews =
                    findMarketNewsForUid(uid)
                            .orElseThrow(() -> new NoResultFoundException(String.format("No MarketNews found for uid %s", uid)));

            return applyChanges(marketNews, data);
        } catch (Exception e) {
            throw new EntityModificationException(String.format("An error occurred while modifying the MarketNews : %s", e.getMessage()), e);
        }
    }

    /**
     * Deletes the {@link MarketNews} for the given uid
     *
     * @param uid uid
     * @return true if deleted, false if not
     */
    public boolean deleteMarketNews(final String uid) {

        validateParameterIsNotNull(uid, CoreConstants.Validation.DataIntegrity.UID_CANNOT_BE_NULL);

        Optional<MarketNews> marketNews = findMarketNewsForUid(uid);
        if (marketNews.isPresent()) {
            MarketNews temp = clearSubData(marketNews.get());
            this.marketNewsRepository.delete(temp);

            return true;
        }

        return false;
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


    //  HELPERS

    /**
     * Applies changes to the given {@link MarketNews} with the given data
     *
     * @param marketNews {@link MarketNews}
     * @param data       {@link Map}
     * @return updated {@link MarketNews}
     */
    private MarketNews applyChanges(MarketNews marketNews, final Map<String, Object> data) {

        Map<String, Object> news = (Map<String, Object>) data.get("marketNews");

        marketNews.setDate(LocalDate.parse(news.get("date").toString(), DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)));
        if (news.containsKey("slots")) {
            marketNews = clearSubData(marketNews);

            List<MarketNewsSlot> slots = new ArrayList<>();
            List<Map<String, Object>> slotJson = (List<Map<String, Object>>) news.get("slots");

            for (Map<String, Object> json : slotJson) {
                MarketNewsSlot slot = new MarketNewsSlot();
                slot.setTime(LocalTime.parse(json.get("time").toString(), DateTimeFormatter.ofPattern(CoreConstants.SHORT_TIME_FORMAT)));
                slot = this.marketNewsSlotRepository.save(slot);

                if (json.containsKey("entries")) {
                    List<MarketNewsEntry> entries = new ArrayList<>();
                    List<Map<String, Object>> entryJson = (List<Map<String, Object>>) json.get("entries");

                    for (Map<String, Object> eJson : entryJson) {
                        MarketNewsEntry marketNewsEntry = new MarketNewsEntry();
                        marketNewsEntry.setSeverity(MarketNewsSeverity.get(Integer.parseInt(eJson.get("severity").toString())));
                        marketNewsEntry.setContent(eJson.get("content").toString());

                        marketNewsEntry = this.marketNewsEntryRepository.save(marketNewsEntry);
                        slot.addEntry(marketNewsEntry);
                        entries.add(marketNewsEntry);
                    }
                }

                marketNews.addSlot(slot);
                slots.add(slot);
            }
        }

        return this.marketNewsRepository.save(marketNews);
    }

    /**
     * Removes all {@link MarketNewsSlot}s & {@link MarketNewsEntry}s from the given {@link MarketNews}
     *
     * @param marketNews {@link MarketNews}
     * @return emptied {@link MarketNews}
     */
    private MarketNews clearSubData(final MarketNews marketNews) {

        List<MarketNewsSlot> slots = marketNews.getSlots() != null ? new ArrayList<>(marketNews.getSlots()) : new ArrayList<>();
        for (MarketNewsSlot slot : slots) {
            List<MarketNewsEntry> entries = slot.getEntries() != null ? new ArrayList<>(slot.getEntries()) : new ArrayList<>();
            for (MarketNewsEntry entry : entries) {
                slot.removeEntry(entry);
                this.marketNewsEntryRepository.delete(entry);
            }

            this.marketNewsSlotRepository.save(slot);
            marketNews.removeSlot(slot);
            this.marketNewsSlotRepository.delete(slot);
        }

        return this.marketNewsRepository.save(marketNews);
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
