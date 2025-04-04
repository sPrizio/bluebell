package com.bluebell.radicle.integration.models.dto.forexfactory;

import com.bluebell.platform.enums.news.MarketNewsSeverity;
import com.bluebell.platform.enums.system.Country;
import com.bluebell.radicle.integration.models.dto.GenericIntegrationDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalTime;

/**
 * Class representation of an entry of news in a news day
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
@Setter
@Builder
public class CalendarNewsDayEntryDTO implements GenericIntegrationDTO {

    private String title;

    private Country country;

    private LocalTime time;

    private MarketNewsSeverity impact;

    private String forecast;

    private String previous;


    //  METHODS

    @Override
    public boolean isEmpty() {
        return StringUtils.isEmpty(this.title) || this.country == null;
    }
}
