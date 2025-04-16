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
        @Schema(description = "Trade ID") String tradeId,
        @Schema(description = "Trade platform") String tradePlatform,
        @Schema(description = "Trade product") String product,
        @Schema(description = "Trade type") String tradeType,
        @Schema(description = "Trade close price") double closePrice,
        @Schema(description = "Trade close time") String tradeCloseTime,
        @Schema(description = "Trade open time") String tradeOpenTime,
        @Schema(description = "Trade lot size") double lotSize,
        @Schema(description = "Trade net profit") double netProfit,
        @Schema(description = "Trade open price") double openPrice,
        @Schema(description = "Trade stop loss") double stopLoss,
        @Schema(description = "Trade take profit") double takeProfit
) { }
