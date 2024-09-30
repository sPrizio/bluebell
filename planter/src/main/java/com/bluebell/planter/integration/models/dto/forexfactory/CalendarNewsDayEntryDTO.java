package com.bluebell.planter.integration.models.dto.forexfactory;

import com.bluebell.planter.core.enums.news.MarketNewsSeverity;
import com.bluebell.planter.core.enums.system.Country;
import com.bluebell.planter.integration.models.dto.GenericIntegrationDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalTime;

/**
 * Class representation of an entry of news in a news day
 *
 * @author Stephen Prizio
 * @version 0.0.4
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
