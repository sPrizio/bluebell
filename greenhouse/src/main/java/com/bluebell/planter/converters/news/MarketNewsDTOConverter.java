package com.bluebell.planter.converters.news;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.bluebell.planter.converters.GenericDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.news.MarketNewsDTO;
import com.bluebell.platform.models.core.entities.news.MarketNews;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Converter that converts {@link MarketNews} into {@link MarketNewsDTO}s
 *
 * @author Stephen Prizio
 * @version 0.1.1
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
            return MarketNewsDTO.builder().build();
        }

        final MarketNewsDTO marketNewsDTO =
                MarketNewsDTO
                        .builder()
                        .uid(this.uniqueIdentifierService.generateUid(entity))
                        .date(entity.getDate())
                        .past(entity.getDate().isBefore(LocalDate.now()))
                        .active(entity.getDate().isEqual(LocalDate.now()))
                        .future(entity.getDate().isAfter(LocalDate.now()))
                        .slots(this.marketNewsSlotDTOConverter.convertAll(entity.getSlots()))
                        .build();

        final LocalDateTime now = LocalDateTime.now();
        if (now.toLocalDate().isEqual(entity.getDate())) {
            marketNewsDTO.getSlots().stream().filter(slot -> slot.getTime().isAfter(now.toLocalTime())).findFirst().ifPresent(slot -> slot.setActive(true));
        }

        return marketNewsDTO;
    }
}
