package com.bluebell.planter.integration.translators.forexfactory;

import com.bluebell.planter.core.enums.news.MarketNewsSeverity;
import com.bluebell.planter.core.enums.system.Country;
import com.bluebell.planter.integration.models.dto.forexfactory.CalendarNewsDayEntryDTO;
import com.bluebell.planter.integration.models.responses.forexfactory.CalendarNewsEntryResponse;
import com.bluebell.planter.integration.translators.GenericTranslator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Translates a {@link CalendarNewsEntryResponse}s into a {@link CalendarNewsDayEntryDTO}s
 *
 * @author Stephen Prizio
 * @version 0.0.4
 */
@Component("calendarNewsDayEntryTranslator")
public class CalendarNewsDayEntryTranslator implements GenericTranslator<CalendarNewsEntryResponse, CalendarNewsDayEntryDTO> {


    //  METHODS

    @Override
    public CalendarNewsDayEntryDTO translate(final CalendarNewsEntryResponse response) {

        final CalendarNewsDayEntryDTO calendarNewsDayEntryDTO = new CalendarNewsDayEntryDTO();

        if (response != null && !response.isEmpty()) {
            calendarNewsDayEntryDTO.setCountry(Country.getByCurrencyCode(response.country()));
            calendarNewsDayEntryDTO.setTime(LocalDateTime.parse(response.date(), DateTimeFormatter.ISO_DATE_TIME).toLocalTime());
            calendarNewsDayEntryDTO.setForecast(response.forecast());
            calendarNewsDayEntryDTO.setTitle(response.title());
            calendarNewsDayEntryDTO.setImpact(MarketNewsSeverity.getFromDescription(response.impact()));
            calendarNewsDayEntryDTO.setPrevious(response.previous());
        }

        return calendarNewsDayEntryDTO;
    }
}
