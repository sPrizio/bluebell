package com.bluebell.planter.converters.news;

import com.bluebell.planter.converters.GenericDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.news.MarketNewsDTO;
import com.bluebell.platform.models.api.dto.news.MarketNewsSlotDTO;
import com.bluebell.platform.models.core.entities.news.MarketNews;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * Converter that converts {@link MarketNews} into {@link MarketNewsDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0.0
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
            final List<MarketNewsSlotDTO> activeSlots =
                    marketNewsDTO.getSlots()
                            .stream()
                            .filter(slot -> now.toLocalTime().isBefore(slot.getTime()))
                            .sorted(Comparator.comparing(MarketNewsSlotDTO::getTime))
                            .toList();

            if (CollectionUtils.isNotEmpty(activeSlots)) {
                activeSlots.get(0).setActive(true);
            }
        }

        return marketNewsDTO;
    }
}
