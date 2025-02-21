package com.bluebell.planter.converters.news;

import com.bluebell.planter.converters.GenericDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.news.MarketNewsEntryDTO;
import com.bluebell.platform.models.api.dto.news.MarketNewsSlotDTO;
import com.bluebell.platform.models.core.entities.news.MarketNewsSlot;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Comparator;

/**
 * Converter that converts {@link MarketNewsSlot}s into {@link MarketNewsSlotDTO}s
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Component("marketNewsSlotDTOConverter")
public class MarketNewsSlotDTOConverter implements GenericDTOConverter<MarketNewsSlot, MarketNewsSlotDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;

    @Resource(name = "marketNewsEntryDTOConverter")
    private MarketNewsEntryDTOConverter marketNewsEntryDTOConverter;


    //  METHODS

    @Override
    public MarketNewsSlotDTO convert(final MarketNewsSlot entity) {

        if (entity == null) {
            return new MarketNewsSlotDTO();
        }

        final MarketNewsSlotDTO marketNewsSlotDTO = new MarketNewsSlotDTO();

        marketNewsSlotDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        marketNewsSlotDTO.setTime(entity.getTime());
        marketNewsSlotDTO.setEntries(this.marketNewsEntryDTOConverter.convertAll(entity.getEntries()).stream().sorted(Comparator.comparing(MarketNewsEntryDTO::getSeverityLevel)).toList());
        marketNewsSlotDTO.setActive(false);

        return marketNewsSlotDTO;
    }
}
