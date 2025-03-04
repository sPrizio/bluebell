package com.bluebell.radicle.integration.models.dto.forexfactory;

import java.time.LocalTime;

import com.bluebell.platform.enums.news.MarketNewsSeverity;
import com.bluebell.platform.enums.system.Country;
import com.bluebell.radicle.integration.models.dto.GenericIntegrationDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * Class representation of an entry of news in a news day
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
@Setter
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
        return StringUtils.isEmpty(title) || country == null;
    }
}
