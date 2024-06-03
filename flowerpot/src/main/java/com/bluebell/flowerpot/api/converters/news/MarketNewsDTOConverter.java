package com.bluebell.flowerpot.api.converters.news;

import com.bluebell.flowerpot.api.converters.GenericDTOConverter;
import com.bluebell.flowerpot.api.models.dto.news.MarketNewsDTO;
import com.bluebell.flowerpot.core.models.entities.news.MarketNews;
import com.bluebell.flowerpot.core.services.platform.UniqueIdentifierService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Converter that converts {@link MarketNews} into {@link MarketNewsDTO}s
 *
 * @author Stephen Prizio
 * @version 0.0.4
 */
@Component("marketNewsDTOConverter")
public class MarketNewsDTOConverter implements GenericDTOConverter<MarketNews, MarketNewsDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;

    @Resource(name = "marketNewsSlotDTOConverter")
    private MarketNewsSlotDTOConverter marketNewsSlotDTOConverter;


    //  METHODS

    @Override
    public MarketNewsDTO convert(final MarketNews entity) {

        if (entity == null) {
            return new MarketNewsDTO();
        }

        final MarketNewsDTO marketNewsDTO = new MarketNewsDTO();

        marketNewsDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        marketNewsDTO.setDate(entity.getDate());
        marketNewsDTO.setPast(entity.getDate().isBefore(LocalDate.now()));
        marketNewsDTO.setActive(entity.getDate().isEqual(LocalDate.now()));
        marketNewsDTO.setFuture(entity.getDate().isAfter(LocalDate.now()));
        marketNewsDTO.setSlots(this.marketNewsSlotDTOConverter.convertAll(entity.getSlots()));

        final LocalDateTime now = LocalDateTime.now();
        if (now.toLocalDate().isEqual(entity.getDate())) {
            marketNewsDTO.getSlots().stream().filter(slot -> slot.getTime().isAfter(now.toLocalTime())).findFirst().ifPresent(slot -> slot.setActive(true));
        }

        return marketNewsDTO;
    }
}
