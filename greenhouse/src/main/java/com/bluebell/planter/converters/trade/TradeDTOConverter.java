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
 * @version 0.0.9
 */
@Component("tradeDTOConverter")
public class TradeDTOConverter implements GenericDTOConverter<Trade, TradeDTO> {

    private final MathService mathService = new MathService();

    @Resource(name = "accountDTOConverter")
    private AccountDTOConverter accountDTOConverter;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public TradeDTO convert(final Trade entity) {

        if (entity == null) {
            return new TradeDTO();
        }

        final TradeDTO tradeDTO = new TradeDTO();

        tradeDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        tradeDTO.setTradeId(entity.getTradeId());
        tradeDTO.setTradePlatform(entity.getTradePlatform());
        tradeDTO.setProduct(entity.getProduct());
        tradeDTO.setTradeType(entity.getTradeType());
        tradeDTO.setOpenPrice(entity.getOpenPrice());
        tradeDTO.setClosePrice(entity.getClosePrice());
        tradeDTO.setTradeOpenTime(entity.getTradeOpenTime());
        tradeDTO.setTradeCloseTime(entity.getTradeCloseTime());
        tradeDTO.setLotSize(entity.getLotSize());
        tradeDTO.setNetProfit(entity.getNetProfit());
        tradeDTO.setPoints(Math.abs(this.mathService.subtract(entity.getOpenPrice(), entity.getClosePrice())));
        tradeDTO.setStopLoss(entity.getStopLoss());
        tradeDTO.setTakeProfit(entity.getTakeProfit());
        tradeDTO.setAccount(this.accountDTOConverter.convert(entity.getAccount()));

        return tradeDTO;
    }
}
