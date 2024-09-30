package com.bluebell.planter.api.converters.news;

import com.bluebell.planter.api.converters.GenericDTOConverter;
import com.bluebell.planter.api.models.dto.news.MarketNewsEntryDTO;
import com.bluebell.planter.core.models.entities.news.MarketNewsEntry;
import com.bluebell.planter.core.services.platform.UniqueIdentifierService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Converter that converts {@link MarketNewsEntry}s into {@link MarketNewsEntryDTO}s
 *
 * @author Stephen Prizio
 * @version 0.0.4
 */
@Component("marketNewsEntryDTOConverter")
public class MarketNewsEntryDTOConverter implements GenericDTOConverter<MarketNewsEntry, MarketNewsEntryDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public MarketNewsEntryDTO convert(final MarketNewsEntry entity) {

        if (entity == null) {
            return new MarketNewsEntryDTO();
        }

        final MarketNewsEntryDTO marketNewsEntryDTO = new MarketNewsEntryDTO();

        marketNewsEntryDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        marketNewsEntryDTO.setContent(entity.getContent());
        marketNewsEntryDTO.setSeverity(entity.getSeverity().getDescription());
        marketNewsEntryDTO.setSeverityLevel(entity.getSeverity().getLevel());
        marketNewsEntryDTO.setCountry(entity.getCountry().getCurrency().getIsoCode());
        marketNewsEntryDTO.setForecast(entity.getForecast());
        marketNewsEntryDTO.setPrevious(entity.getPrevious());

        return marketNewsEntryDTO;
    }
}
