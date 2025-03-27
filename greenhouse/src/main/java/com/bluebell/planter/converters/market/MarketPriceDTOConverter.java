package com.bluebell.planter.converters.market;

import com.bluebell.planter.converters.GenericDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.market.MarketPriceDTO;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Converts {@link MarketPrice}s into {@link MarketPriceDTO}s
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
@Component("marketPriceDTOConverter")
public class MarketPriceDTOConverter implements GenericDTOConverter<MarketPrice, MarketPriceDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public MarketPriceDTO convert(final MarketPrice entity) {

        if (entity == null) {
            return MarketPriceDTO.builder().build();
        }

        return MarketPriceDTO
                .builder()
                .uid(this.uniqueIdentifierService.generateUid(entity))
                .date(entity.getDate())
                .timeInterval(EnumDisplay.builder().code(entity.getInterval().getCode()).label(entity.getInterval().getLabel()).build())
                .open(entity.getOpen())
                .high(entity.getHigh())
                .low(entity.getLow())
                .close(entity.getClose())
                .volume(entity.getVolume())
                .dataSource(EnumDisplay.builder().code(entity.getDataSource().getCode()).label(entity.getDataSource().getLabel()).build())
                .build();
    }
}
