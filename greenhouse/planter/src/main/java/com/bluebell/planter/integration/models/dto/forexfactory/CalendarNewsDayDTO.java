package com.bluebell.planter.integration.models.dto.forexfactory;

import com.bluebell.planter.integration.models.dto.GenericIntegrationDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * Class representation of a day that contains news
 *
 * @author Stephen Prizio
 * @version 0.0.4
 */
@Getter
@Setter
public class CalendarNewsDayDTO implements GenericIntegrationDTO {

    private LocalDate date;

    private List<CalendarNewsDayEntryDTO> entries;


    //  METHODS

    @Override
    public boolean isEmpty() {
        return this.date == null || CollectionUtils.isEmpty(this.entries);
    }
}
