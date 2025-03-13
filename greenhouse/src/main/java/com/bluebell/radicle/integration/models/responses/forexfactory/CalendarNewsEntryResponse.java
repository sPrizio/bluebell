package com.bluebell.radicle.integration.models.responses.forexfactory;

import com.bluebell.radicle.integration.models.responses.GenericIntegrationResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * Class representation of a single news entry in a particular day
 *
 * @param title    title of event
 * @param country  country that it affects
 * @param date     date of news
 * @param impact   severity (likelihood of market movement)
 * @param forecast expected value
 * @param previous previously reported value
 * @param url      url link
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Builder
@JsonIgnoreProperties
public record CalendarNewsEntryResponse(
        @Getter String title,
        @Getter String country,
        @Getter String date,
        @Getter String impact,
        @Getter String forecast,
        @Getter String previous,
        @Getter String url
) implements GenericIntegrationResponse, Comparable<CalendarNewsEntryResponse> {


    //  METHODS

    @Override
    public boolean isEmpty() {
        return StringUtils.isEmpty(this.title) || StringUtils.isEmpty(this.country) || StringUtils.isEmpty(this.date);
    }

    @Override
    public int compareTo(CalendarNewsEntryResponse o) {
        return this.date.compareTo(o.date);
    }
}
