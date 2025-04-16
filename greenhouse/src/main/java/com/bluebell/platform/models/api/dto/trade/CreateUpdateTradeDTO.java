package com.bluebell.platform.models.api.dto.trade;

import com.bluebell.platform.models.core.entities.trade.Trade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Request object creating and updating {@link Trade}s
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Builder
@Schema(title = "CreateUpdateTradeDTO", name = "CreateUpdateTradeDTO", description = "Payload for creating and updating trades")
public record CreateUpdateTradeDTO(
        String tradeId,
        String tradePlatform,
        String product,
        String tradeType,
        double closePrice,
        String tradeCloseTime,
        String tradeOpenTime,
        double lotSize,
        double netProfit,
        double openPrice,
        double stopLoss,
        double takeProfit
) { }
