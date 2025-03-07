package com.bluebell.planter.converters.news;

import com.bluebell.planter.converters.GenericDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.news.MarketNewsEntryDTO;
import com.bluebell.platform.models.core.entities.news.MarketNewsEntry;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Converter that converts {@link MarketNewsEntry}s into {@link MarketNewsEntryDTO}s
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Component("marketNewsEntryDTOConverter")
public class MarketNewsEntryDTOConverter implements GenericDTOConverter<MarketNewsEntry, MarketNewsEntryDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public MarketNewsEntryDTO convert(final MarketNewsEntry entity) {

        if (entity == null) {
            return MarketNewsEntryDTO.builder().build();
        }

        return MarketNewsEntryDTO
                .builder()
                .uid(this.uniqueIdentifierService.generateUid(entity))
                .content(entity.getContent())
                .severity(entity.getSeverity().getDescription())
                .severityLevel(entity.getSeverity().getLevel())
                .country(entity.getCountry().getCurrency().getIsoCode())
                .forecast(entity.getForecast())
                .previous(entity.getPrevious())
                .build();
    }
}
