package com.bluebell.planter.integration.models.responses.forexfactory;

import com.bluebell.planter.integration.models.responses.GenericIntegrationResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * @version 0.0.4
 */
@JsonIgnoreProperties
public record CalendarNewsEntryResponse(
        String title,
        String country,
        String date,
        String impact,
        String forecast,
        String previous,
        String url
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
