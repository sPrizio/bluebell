package com.bluebell.radicle.runners;

import com.bluebell.platform.models.core.entities.news.MarketNews;
import com.bluebell.platform.models.core.entities.news.MarketNewsEntry;
import com.bluebell.platform.models.core.entities.news.MarketNewsSlot;
import com.bluebell.radicle.integration.models.dto.forexfactory.CalendarNewsDayDTO;
import com.bluebell.radicle.integration.models.dto.forexfactory.CalendarNewsDayEntryDTO;
import com.bluebell.radicle.integration.models.responses.forexfactory.CalendarNewsEntryResponse;
import com.bluebell.radicle.integration.translators.forexfactory.CalendarNewsDayEntryTranslator;
import com.bluebell.radicle.repositories.news.MarketNewsEntryRepository;
import com.bluebell.radicle.repositories.news.MarketNewsRepository;
import com.bluebell.radicle.repositories.news.MarketNewsSlotRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * Generates testing {@link MarketNews}
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Component
@Profile("dev")
@ConditionalOnProperty(name = "bluebell.cmdlr.market.data", havingValue = "true", matchIfMissing = true)
public class MarketNewsRunner extends AbstractRunner implements CommandLineRunner, Ordered {

    final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${bluebell.cmdlr.order.market-news}")
    private int order;

    @Resource(name = "calendarNewsDayEntryTranslator")
    private CalendarNewsDayEntryTranslator calendarNewsDayEntryTranslator;

    @Resource(name = "marketNewsRepository")
    private MarketNewsRepository marketNewsRepository;

    @Resource(name = "marketNewsEntryRepository")
    private MarketNewsEntryRepository marketNewsEntryRepository;

    @Resource(name = "marketNewsSlotRepository")
    private MarketNewsSlotRepository marketNewsSlotRepository;


    //  METHODS

    @Override
    @Transactional
    public void run(String... args) {

        logStart();

        try {
            generate();

            final LocalDate now = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            this.marketNewsRepository.findAll().forEach(marketNews -> {
                if (marketNews.getDate().equals(LocalDate.of(2025, 3, 9))) {
                    marketNews.setDate(now);
                    this.marketNewsRepository.save(marketNews);
                } else if (marketNews.getDate().equals(LocalDate.of(2025, 3, 10))) {
                    marketNews.setDate(now.plusDays(1));
                    this.marketNewsRepository.save(marketNews);
                } else if (marketNews.getDate().equals(LocalDate.of(2025, 3, 11))) {
                    marketNews.setDate(now.plusDays(2));
                    this.marketNewsRepository.save(marketNews);
                } else if (marketNews.getDate().equals(LocalDate.of(2025, 3, 12))) {
                    marketNews.setDate(now.plusDays(3));
                    this.marketNewsRepository.save(marketNews);
                } else if (marketNews.getDate().equals(LocalDate.of(2025, 3, 13))) {
                    marketNews.setDate(now.plusDays(4));
                    this.marketNewsRepository.save(marketNews);
                } else if (marketNews.getDate().equals(LocalDate.of(2025, 3, 14))) {
                    marketNews.setDate(now.plusDays(5));
                    this.marketNewsRepository.save(marketNews);
                }
            });
        } catch (Exception e) {
            //  DO NOTHING
        }

        logEnd();
    }

    @Override
    public int getOrder() {
        return this.order;
    }


    //  HELPERS

    /**
     * Obtains a {@link LocalDateTime} from the given string
     *
     * @param string date time string
     * @return {@link LocalDateTime}
     */
    private LocalDateTime getDateTime(final String string) {
        try {
            return LocalDateTime.parse(string, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }

    /**
     * Generates a {@link Map} of key-value pairs of {@link String} and {@link List} {@link CalendarNewsEntryResponse}s for the given list of {@link CalendarNewsEntryResponse}
     * The idea is to create a map of week days and populate each week day with all news entries for that day
     *
     * @param entries array of {@link CalendarNewsEntryResponse}
     * @return {@link Map}
     */
    private Map<LocalDate, List<CalendarNewsEntryResponse>> generateDataMap(final CalendarNewsEntryResponse[] entries) {

        final Map<LocalDate, List<CalendarNewsEntryResponse>> map = new HashMap<>();
        if (entries != null) {
            for (CalendarNewsEntryResponse data : entries) {
                final List<CalendarNewsEntryResponse> list;
                if (map.containsKey(LocalDateTime.parse(data.date(), DateTimeFormatter.ISO_DATE_TIME).toLocalDate())) {
                    list = new ArrayList<>(map.get(LocalDateTime.parse(data.date(), DateTimeFormatter.ISO_DATE_TIME).toLocalDate()));
                } else {
                    list = new ArrayList<>();
                }

                list.add(data);
                map.put(getDateTime(data.date()).toLocalDate(), list.stream().sorted(Comparator.comparing(CalendarNewsEntryResponse::date)).toList());
            }
        }

        return map;
    }

    /**
     * Generates the test data
     */
    private void generate() throws JsonProcessingException {
        final CalendarNewsEntryResponse[] entries = this.objectMapper.readValue("[{\"title\":\"Daylight Saving Time Shift\",\"country\":\"CAD\",\"date\":\"2025-03-09T03:00:00-04:00\",\"impact\":\"Holiday\",\"forecast\":\"\",\"previous\":\"\"},{\"title\":\"Daylight Saving Time Shift\",\"country\":\"USD\",\"date\":\"2025-03-09T03:00:00-04:00\",\"impact\":\"Holiday\",\"forecast\":\"\",\"previous\":\"\"},{\"title\":\"Average Cash Earnings y\\/y\",\"country\":\"JPY\",\"date\":\"2025-03-09T19:30:00-04:00\",\"impact\":\"Low\",\"forecast\":\"3.2%\",\"previous\":\"4.8%\"},{\"title\":\"Bank Lending y\\/y\",\"country\":\"JPY\",\"date\":\"2025-03-09T19:50:00-04:00\",\"impact\":\"Low\",\"forecast\":\"3.1%\",\"previous\":\"3.0%\"},{\"title\":\"Current Account\",\"country\":\"JPY\",\"date\":\"2025-03-09T19:50:00-04:00\",\"impact\":\"Low\",\"forecast\":\"1.97T\",\"previous\":\"2.73T\"},{\"title\":\"Leading Indicators\",\"country\":\"JPY\",\"date\":\"2025-03-10T01:00:00-04:00\",\"impact\":\"Low\",\"forecast\":\"108.1%\",\"previous\":\"108.9%\"},{\"title\":\"Economy Watchers Sentiment\",\"country\":\"JPY\",\"date\":\"2025-03-10T02:00:00-04:00\",\"impact\":\"Low\",\"forecast\":\"48.5\",\"previous\":\"48.6\"},{\"title\":\"German Industrial Production m\\/m\",\"country\":\"EUR\",\"date\":\"2025-03-10T03:00:00-04:00\",\"impact\":\"Low\",\"forecast\":\"1.6%\",\"previous\":\"-2.4%\"},{\"title\":\"German Trade Balance\",\"country\":\"EUR\",\"date\":\"2025-03-10T03:00:00-04:00\",\"impact\":\"Low\",\"forecast\":\"21.0B\",\"previous\":\"20.7B\"},{\"title\":\"SECO Consumer Climate\",\"country\":\"CHF\",\"date\":\"2025-03-10T03:59:00-04:00\",\"impact\":\"Low\",\"forecast\":\"-28\",\"previous\":\"-29\"},{\"title\":\"Eurogroup Meetings\",\"country\":\"EUR\",\"date\":\"2025-03-10T05:15:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"\"},{\"title\":\"Sentix Investor Confidence\",\"country\":\"EUR\",\"date\":\"2025-03-10T05:30:00-04:00\",\"impact\":\"Low\",\"forecast\":\"-9.1\",\"previous\":\"-12.7\"},{\"title\":\"Manufacturing Sales q\\/q\",\"country\":\"NZD\",\"date\":\"2025-03-10T17:45:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"-0.1%\"},{\"title\":\"Westpac Consumer Sentiment\",\"country\":\"AUD\",\"date\":\"2025-03-10T19:01:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"0.1%\"},{\"title\":\"Household Spending y\\/y\",\"country\":\"JPY\",\"date\":\"2025-03-10T19:30:00-04:00\",\"impact\":\"Low\",\"forecast\":\"3.7%\",\"previous\":\"2.7%\"},{\"title\":\"Final GDP Price Index y\\/y\",\"country\":\"JPY\",\"date\":\"2025-03-10T19:50:00-04:00\",\"impact\":\"Low\",\"forecast\":\"2.8%\",\"previous\":\"2.8%\"},{\"title\":\"Final GDP q\\/q\",\"country\":\"JPY\",\"date\":\"2025-03-10T19:50:00-04:00\",\"impact\":\"Low\",\"forecast\":\"0.7%\",\"previous\":\"0.7%\"},{\"title\":\"M2 Money Stock y\\/y\",\"country\":\"JPY\",\"date\":\"2025-03-10T19:50:00-04:00\",\"impact\":\"Low\",\"forecast\":\"1.4%\",\"previous\":\"1.3%\"},{\"title\":\"BRC Retail Sales Monitor y\\/y\",\"country\":\"GBP\",\"date\":\"2025-03-10T20:01:00-04:00\",\"impact\":\"Low\",\"forecast\":\"1.9%\",\"previous\":\"2.5%\"},{\"title\":\"NAB Business Confidence\",\"country\":\"AUD\",\"date\":\"2025-03-10T20:30:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"4\"},{\"title\":\"Prelim Machine Tool Orders y\\/y\",\"country\":\"JPY\",\"date\":\"2025-03-11T01:58:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"4.7%\"},{\"title\":\"ECOFIN Meetings\",\"country\":\"EUR\",\"date\":\"2025-03-11T05:15:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"\"},{\"title\":\"NFIB Small Business Index\",\"country\":\"USD\",\"date\":\"2025-03-11T06:00:00-04:00\",\"impact\":\"Low\",\"forecast\":\"100.9\",\"previous\":\"102.8\"},{\"title\":\"CB Leading Index m\\/m\",\"country\":\"GBP\",\"date\":\"2025-03-11T09:30:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"-0.1%\"},{\"title\":\"JOLTS Job Openings\",\"country\":\"USD\",\"date\":\"2025-03-11T10:00:00-04:00\",\"impact\":\"High\",\"forecast\":\"7.65M\",\"previous\":\"7.60M\"},{\"title\":\"President Trump Speaks\",\"country\":\"USD\",\"date\":\"2025-03-11T17:00:00-04:00\",\"impact\":\"Medium\",\"forecast\":\"\",\"previous\":\"\"},{\"title\":\"BSI Manufacturing Index\",\"country\":\"JPY\",\"date\":\"2025-03-11T19:50:00-04:00\",\"impact\":\"Low\",\"forecast\":\"6.5\",\"previous\":\"6.3\"},{\"title\":\"PPI y\\/y\",\"country\":\"JPY\",\"date\":\"2025-03-11T19:50:00-04:00\",\"impact\":\"Low\",\"forecast\":\"4.0%\",\"previous\":\"4.2%\"},{\"title\":\"ECB President Lagarde Speaks\",\"country\":\"EUR\",\"date\":\"2025-03-12T04:45:00-04:00\",\"impact\":\"Medium\",\"forecast\":\"\",\"previous\":\"\"},{\"title\":\"10-y Bond Auction\",\"country\":\"GBP\",\"date\":\"2025-03-12T06:09:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"4.81|2.8\"},{\"title\":\"German 10-y Bond Auction\",\"country\":\"EUR\",\"date\":\"2025-03-12T06:31:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"2.52|2.8\"},{\"title\":\"Core CPI m\\/m\",\"country\":\"USD\",\"date\":\"2025-03-12T08:30:00-04:00\",\"impact\":\"High\",\"forecast\":\"0.3%\",\"previous\":\"0.4%\"},{\"title\":\"CPI m\\/m\",\"country\":\"USD\",\"date\":\"2025-03-12T08:30:00-04:00\",\"impact\":\"High\",\"forecast\":\"0.3%\",\"previous\":\"0.5%\"},{\"title\":\"CPI y\\/y\",\"country\":\"USD\",\"date\":\"2025-03-12T08:30:00-04:00\",\"impact\":\"High\",\"forecast\":\"2.9%\",\"previous\":\"3.0%\"},{\"title\":\"BOC Rate Statement\",\"country\":\"CAD\",\"date\":\"2025-03-12T09:45:00-04:00\",\"impact\":\"High\",\"forecast\":\"\",\"previous\":\"\"},{\"title\":\"Overnight Rate\",\"country\":\"CAD\",\"date\":\"2025-03-12T09:45:00-04:00\",\"impact\":\"High\",\"forecast\":\"2.75%\",\"previous\":\"3.00%\"},{\"title\":\"German Buba President Nagel Speaks\",\"country\":\"EUR\",\"date\":\"2025-03-12T09:45:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"\"},{\"title\":\"BOC Press Conference\",\"country\":\"CAD\",\"date\":\"2025-03-12T10:30:00-04:00\",\"impact\":\"High\",\"forecast\":\"\",\"previous\":\"\"},{\"title\":\"Crude Oil Inventories\",\"country\":\"USD\",\"date\":\"2025-03-12T10:30:00-04:00\",\"impact\":\"Low\",\"forecast\":\"2.1M\",\"previous\":\"3.6M\"},{\"title\":\"10-y Bond Auction\",\"country\":\"USD\",\"date\":\"2025-03-12T13:01:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"4.63|2.5\"},{\"title\":\"Federal Budget Balance\",\"country\":\"USD\",\"date\":\"2025-03-12T14:00:00-04:00\",\"impact\":\"Low\",\"forecast\":\"-302.5B\",\"previous\":\"-128.6B\"},{\"title\":\"Visitor Arrivals m\\/m\",\"country\":\"NZD\",\"date\":\"2025-03-12T17:45:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"3.5%\"},{\"title\":\"MI Inflation Expectations\",\"country\":\"AUD\",\"date\":\"2025-03-12T20:00:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"4.6%\"},{\"title\":\"RICS House Price Balance\",\"country\":\"GBP\",\"date\":\"2025-03-12T20:01:00-04:00\",\"impact\":\"Low\",\"forecast\":\"20%\",\"previous\":\"22%\"},{\"title\":\"PPI m\\/m\",\"country\":\"CHF\",\"date\":\"2025-03-13T03:30:00-04:00\",\"impact\":\"Low\",\"forecast\":\"0.2%\",\"previous\":\"0.1%\"},{\"title\":\"New Loans\",\"country\":\"CNY\",\"date\":\"2025-03-13T04:03:00-04:00\",\"impact\":\"Medium\",\"forecast\":\"2150B\",\"previous\":\"5130B\"},{\"title\":\"M2 Money Supply y\\/y\",\"country\":\"CNY\",\"date\":\"2025-03-13T04:03:00-04:00\",\"impact\":\"Low\",\"forecast\":\"7.1%\",\"previous\":\"7.0%\"},{\"title\":\"Italian Quarterly Unemployment Rate\",\"country\":\"EUR\",\"date\":\"2025-03-13T05:00:00-04:00\",\"impact\":\"Low\",\"forecast\":\"6.2%\",\"previous\":\"6.1%\"},{\"title\":\"Industrial Production m\\/m\",\"country\":\"EUR\",\"date\":\"2025-03-13T06:00:00-04:00\",\"impact\":\"Low\",\"forecast\":\"0.5%\",\"previous\":\"-1.1%\"},{\"title\":\"Building Permits m\\/m\",\"country\":\"CAD\",\"date\":\"2025-03-13T08:30:00-04:00\",\"impact\":\"Low\",\"forecast\":\"-5.3%\",\"previous\":\"11.0%\"},{\"title\":\"Core PPI m\\/m\",\"country\":\"USD\",\"date\":\"2025-03-13T08:30:00-04:00\",\"impact\":\"High\",\"forecast\":\"0.3%\",\"previous\":\"0.3%\"},{\"title\":\"PPI m\\/m\",\"country\":\"USD\",\"date\":\"2025-03-13T08:30:00-04:00\",\"impact\":\"High\",\"forecast\":\"0.3%\",\"previous\":\"0.4%\"},{\"title\":\"Unemployment Claims\",\"country\":\"USD\",\"date\":\"2025-03-13T08:30:00-04:00\",\"impact\":\"High\",\"forecast\":\"226K\",\"previous\":\"221K\"},{\"title\":\"Natural Gas Storage\",\"country\":\"USD\",\"date\":\"2025-03-13T10:30:00-04:00\",\"impact\":\"Low\",\"forecast\":\"-46B\",\"previous\":\"-80B\"},{\"title\":\"30-y Bond Auction\",\"country\":\"USD\",\"date\":\"2025-03-13T13:01:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"4.75|2.3\"},{\"title\":\"German Buba President Nagel Speaks\",\"country\":\"EUR\",\"date\":\"2025-03-13T13:30:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"\"},{\"title\":\"BusinessNZ Manufacturing Index\",\"country\":\"NZD\",\"date\":\"2025-03-13T17:30:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"51.4\"},{\"title\":\"FPI m\\/m\",\"country\":\"NZD\",\"date\":\"2025-03-13T17:45:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"1.9%\"},{\"title\":\"German Final CPI m\\/m\",\"country\":\"EUR\",\"date\":\"2025-03-14T03:00:00-04:00\",\"impact\":\"Low\",\"forecast\":\"0.4%\",\"previous\":\"0.4%\"},{\"title\":\"German WPI m\\/m\",\"country\":\"EUR\",\"date\":\"2025-03-14T03:00:00-04:00\",\"impact\":\"Low\",\"forecast\":\"0.2%\",\"previous\":\"0.9%\"},{\"title\":\"GDP m\\/m\",\"country\":\"GBP\",\"date\":\"2025-03-14T03:00:00-04:00\",\"impact\":\"High\",\"forecast\":\"0.1%\",\"previous\":\"0.4%\"},{\"title\":\"Construction Output m\\/m\",\"country\":\"GBP\",\"date\":\"2025-03-14T03:00:00-04:00\",\"impact\":\"Low\",\"forecast\":\"-0.1%\",\"previous\":\"-0.2%\"},{\"title\":\"Goods Trade Balance\",\"country\":\"GBP\",\"date\":\"2025-03-14T03:00:00-04:00\",\"impact\":\"Low\",\"forecast\":\"-16.8B\",\"previous\":\"-17.4B\"},{\"title\":\"Index of Services 3m\\/3m\",\"country\":\"GBP\",\"date\":\"2025-03-14T03:00:00-04:00\",\"impact\":\"Low\",\"forecast\":\"0.3%\",\"previous\":\"0.2%\"},{\"title\":\"Industrial Production m\\/m\",\"country\":\"GBP\",\"date\":\"2025-03-14T03:00:00-04:00\",\"impact\":\"Low\",\"forecast\":\"-0.1%\",\"previous\":\"0.5%\"},{\"title\":\"Manufacturing Production m\\/m\",\"country\":\"GBP\",\"date\":\"2025-03-14T03:00:00-04:00\",\"impact\":\"Low\",\"forecast\":\"0.0%\",\"previous\":\"0.7%\"},{\"title\":\"French Final CPI m\\/m\",\"country\":\"EUR\",\"date\":\"2025-03-14T03:45:00-04:00\",\"impact\":\"Low\",\"forecast\":\"0.0%\",\"previous\":\"0.0%\"},{\"title\":\"Italian Industrial Production m\\/m\",\"country\":\"EUR\",\"date\":\"2025-03-14T05:00:00-04:00\",\"impact\":\"Low\",\"forecast\":\"1.5%\",\"previous\":\"-3.1%\"},{\"title\":\"Consumer Inflation Expectations\",\"country\":\"GBP\",\"date\":\"2025-03-14T05:30:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"3.0%\"},{\"title\":\"NIESR GDP Estimate\",\"country\":\"GBP\",\"date\":\"2025-03-14T08:03:00-04:00\",\"impact\":\"Low\",\"forecast\":\"\",\"previous\":\"0.3%\"},{\"title\":\"Manufacturing Sales m\\/m\",\"country\":\"CAD\",\"date\":\"2025-03-14T08:30:00-04:00\",\"impact\":\"Low\",\"forecast\":\"2.0%\",\"previous\":\"0.3%\"},{\"title\":\"Wholesale Sales m\\/m\",\"country\":\"CAD\",\"date\":\"2025-03-14T08:30:00-04:00\",\"impact\":\"Low\",\"forecast\":\"1.9%\",\"previous\":\"-0.2%\"},{\"title\":\"Prelim UoM Consumer Sentiment\",\"country\":\"USD\",\"date\":\"2025-03-14T10:00:00-04:00\",\"impact\":\"High\",\"forecast\":\"63.1\",\"previous\":\"67.8\"},{\"title\":\"Prelim UoM Inflation Expectations\",\"country\":\"USD\",\"date\":\"2025-03-14T10:00:00-04:00\",\"impact\":\"High\",\"forecast\":\"\",\"previous\":\"4.3%\"}]", CalendarNewsEntryResponse[].class);
        final List<CalendarNewsDayDTO> news = new ArrayList<>();
        final Map<LocalDate, List<CalendarNewsEntryResponse>> testMap = generateDataMap(entries);

        testMap.forEach((key, value) -> {
            final CalendarNewsDayDTO day = CalendarNewsDayDTO
                    .builder()
                    .date(key)
                    .entries(this.calendarNewsDayEntryTranslator.translateAll(value))
                    .build();
            news.add(day);
        });

        List<CalendarNewsDayDTO> newsDTO = news.stream().sorted(Comparator.comparing(CalendarNewsDayDTO::getDate)).toList();
        newsDTO = newsDTO.stream().filter(n -> CollectionUtils.isNotEmpty(n.getEntries())).toList();

        for (final CalendarNewsDayDTO day : newsDTO) {
            MarketNews marketNews = MarketNews.builder().build();
            final List<MarketNewsSlot> marketNewsSlots = new ArrayList<>();
            final Map<LocalTime, List<CalendarNewsDayEntryDTO>> map = new HashMap<>();

            for (final CalendarNewsDayEntryDTO entryDTO : day.getEntries()) {
                List<CalendarNewsDayEntryDTO> entryDTOS;
                if (map.containsKey(entryDTO.getTime())) {
                    entryDTOS = new ArrayList<>(map.get(entryDTO.getTime()));
                } else {
                    entryDTOS = new ArrayList<>();
                }

                entryDTOS.add(entryDTO);
                map.put(entryDTO.getTime(), entryDTOS);
            }

            marketNews = this.marketNewsRepository.save(marketNews);
            for (final Map.Entry<LocalTime, List<CalendarNewsDayEntryDTO>> entry : map.entrySet()) {
                MarketNewsSlot marketNewsSlot = this.marketNewsSlotRepository.save(getSlot(marketNews, entry.getKey()));
                marketNewsSlot.setTime(entry.getKey());
                marketNewsSlot.setNews(marketNews);

                marketNewsSlot = this.marketNewsSlotRepository.save(marketNewsSlot);

                List<MarketNewsEntry> marketNewsEntries = new ArrayList<>();
                for (final CalendarNewsDayEntryDTO val : entry.getValue()) {
                    MarketNewsEntry marketNewsEntry = this.marketNewsEntryRepository.save(getEntry(marketNewsSlot, val.getTitle()));
                    marketNewsEntry.setPrevious(val.getPrevious());
                    marketNewsEntry.setSlot(marketNewsSlot);
                    marketNewsEntry.setCountry(val.getCountry());
                    marketNewsEntry.setForecast(val.getForecast());
                    marketNewsEntry.setSeverity(val.getImpact());
                    marketNewsEntry.setContent(val.getTitle());
                    this.marketNewsEntryRepository.save(marketNewsEntry);
                    marketNewsEntries.add(marketNewsEntry);
                }

                marketNewsSlot.setEntries(marketNewsEntries);
                marketNewsSlots.add(this.marketNewsSlotRepository.save(marketNewsSlot));
            }

            marketNews.setDate(day.getDate());
            marketNews.setSlots(marketNewsSlots);
            this.marketNewsRepository.save(marketNews);
        }
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
            return MarketNewsSlot.builder().build();
        }

        return news.getSlots().stream().filter(slot -> slot.getTime().equals(time)).findFirst().orElse(MarketNewsSlot.builder().build());
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
            return MarketNewsEntry.builder().build();
        }

        return slot.getEntries().stream().filter(entry -> entry.getContent().equals(title)).findFirst().orElse(MarketNewsEntry.builder().build());
    }
}
