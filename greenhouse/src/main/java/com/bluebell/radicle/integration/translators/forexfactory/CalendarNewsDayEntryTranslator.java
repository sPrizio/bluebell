package com.bluebell.radicle.integration.translators.forexfactory;

import com.bluebell.platform.enums.news.MarketNewsSeverity;
import com.bluebell.platform.enums.system.Country;
import com.bluebell.radicle.integration.models.dto.forexfactory.CalendarNewsDayEntryDTO;
import com.bluebell.radicle.integration.models.responses.forexfactory.CalendarNewsEntryResponse;
import com.bluebell.radicle.integration.translators.GenericTranslator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Translates a {@link CalendarNewsEntryResponse}s into a {@link CalendarNewsDayEntryDTO}s
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Component("calendarNewsDayEntryTranslator")
public class CalendarNewsDayEntryTranslator implements GenericTranslator<CalendarNewsEntryResponse, CalendarNewsDayEntryDTO> {


    //  METHODS

    @Override
    public CalendarNewsDayEntryDTO translate(final CalendarNewsEntryResponse response) {

        if (response != null && !response.isEmpty()) {
            return CalendarNewsDayEntryDTO
                    .builder()
                    .country(Country.getByCurrencyCode(response.country()))
                    .time(LocalDateTime.parse(response.date(), DateTimeFormatter.ISO_DATE_TIME).toLocalTime())
                    .forecast(response.forecast())
                    .title(response.title())
                    .impact(MarketNewsSeverity.getFromLabel(response.impact()))
                    .previous(response.previous())
                    .build();
        }

        return CalendarNewsDayEntryDTO.builder().build();
    }
}
