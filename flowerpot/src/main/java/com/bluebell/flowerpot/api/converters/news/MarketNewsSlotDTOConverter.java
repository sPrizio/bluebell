package com.bluebell.flowerpot.api.converters.news;

import com.bluebell.flowerpot.api.converters.GenericDTOConverter;
import com.bluebell.flowerpot.api.models.dto.news.MarketNewsEntryDTO;
import com.bluebell.flowerpot.api.models.dto.news.MarketNewsSlotDTO;
import com.bluebell.flowerpot.core.models.entities.news.MarketNewsSlot;
import com.bluebell.flowerpot.core.services.platform.UniqueIdentifierService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Comparator;

/**
 * Converter that converts {@link MarketNewsSlot}s into {@link MarketNewsSlotDTO}s
 *
 * @author Stephen Prizio
 * @version 0.0.4
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
