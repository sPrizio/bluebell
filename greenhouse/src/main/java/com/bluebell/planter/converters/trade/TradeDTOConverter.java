package com.bluebell.planter.converters.trade;

import com.bluebell.planter.converters.GenericDTOConverter;
import com.bluebell.planter.converters.account.AccountDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.trade.TradeDTO;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import com.bluebell.platform.services.MathService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Converter for {@link Trade}s into {@link TradeDTO}s
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Component("tradeDTOConverter")
public class TradeDTOConverter implements GenericDTOConverter<Trade, TradeDTO> {

    @Resource(name = "accountDTOConverter")
    private AccountDTOConverter accountDTOConverter;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public TradeDTO convert(final Trade entity) {

        if (entity == null) {
            return TradeDTO.builder().build();
        }

        return TradeDTO
                .builder()
                .uid(this.uniqueIdentifierService.generateUid(entity))
                .tradeId(entity.getTradeId())
                .tradePlatform(EnumDisplay.builder().code(entity.getTradePlatform().getCode()).label(entity.getTradePlatform().getLabel()).build())
                .product(entity.getProduct())
                .tradeType(EnumDisplay.builder().code(entity.getTradeType().getCode()).label(entity.getTradeType().getLabel()).build())
                .openPrice(entity.getOpenPrice())
                .closePrice(entity.getClosePrice())
                .tradeOpenTime(entity.getTradeOpenTime())
                .tradeCloseTime(entity.getTradeCloseTime())
                .lotSize(entity.getLotSize())
                .netProfit(entity.getNetProfit())
                .points(calculatePoints(entity))
                .stopLoss(entity.getStopLoss())
                .takeProfit(entity.getTakeProfit())
                .account(this.accountDTOConverter.convert(entity.getAccount()))
                .build();
    }


    //  HELPERS

    /**
     * Safely calculates the points for the given trade
     *
     * @param entity {@link Trade}
     * @return points
     */
    private double calculatePoints(final Trade entity) {
        final MathService mathService = new MathService();
        if (entity.getClosePrice() != 0) {
            return Math.abs(mathService.subtract(entity.getOpenPrice(), entity.getClosePrice()));
        }

        return 0.0;
    }
}
