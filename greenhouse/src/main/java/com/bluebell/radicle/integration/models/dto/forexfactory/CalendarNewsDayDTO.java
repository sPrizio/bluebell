package com.bluebell.radicle.integration.models.dto.forexfactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.bluebell.radicle.integration.models.dto.GenericIntegrationDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Class representation of a day that contains news
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
@Setter
@Builder
public class CalendarNewsDayDTO implements GenericIntegrationDTO {

    private LocalDate date;

    private @Builder.Default List<CalendarNewsDayEntryDTO> entries = new ArrayList<>();


    //  METHODS

    @Override
    public boolean isEmpty() {
        return this.date == null || CollectionUtils.isEmpty(this.entries);
    }
}
