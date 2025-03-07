package com.bluebell.planter.converters.trade;

import com.bluebell.planter.converters.GenericDTOConverter;
import com.bluebell.planter.converters.account.AccountDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.trade.TradeDTO;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.services.MathService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Converter for {@link Trade}s into {@link TradeDTO}s
 *
 * @author Stephen Prizio
 * @version 0.1.1
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

        final MathService mathService = new MathService();
        return TradeDTO
                .builder()
                .uid(this.uniqueIdentifierService.generateUid(entity))
                .tradeId(entity.getTradeId())
                .tradePlatform(entity.getTradePlatform())
                .product(entity.getProduct())
                .tradeType(entity.getTradeType())
                .openPrice(entity.getOpenPrice())
                .closePrice(entity.getClosePrice())
                .tradeOpenTime(entity.getTradeOpenTime())
                .tradeCloseTime(entity.getTradeCloseTime())
                .lotSize(entity.getLotSize())
                .netProfit(entity.getNetProfit())
                .points(Math.abs(mathService.subtract(entity.getOpenPrice(), entity.getClosePrice())))
                .stopLoss(entity.getStopLoss())
                .takeProfit(entity.getTakeProfit())
                .account(this.accountDTOConverter.convert(entity.getAccount()))
                .build();
    }
}
