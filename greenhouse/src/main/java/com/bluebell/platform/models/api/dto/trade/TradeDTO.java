package com.bluebell.platform.models.api.dto.trade;

import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.api.dto.account.AccountDTO;
import com.bluebell.platform.models.core.entities.trade.Trade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

/**
 * A DTO representation of a {@link Trade}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Setter
@Getter
@Builder
@Schema(title = "TradeDTO", name = "TradeDTO", description = "Data representation of a trade in the system")
public class TradeDTO implements GenericDTO {

    @Schema(description = "Trade uid")
    private @Builder.Default String uid = StringUtils.EMPTY;

    @Schema(description = "Trade id (from the trading platform)")
    private String tradeId;

    @Schema(description = "Product or equity that was traded")
    private String product;

    @Schema(description = "Trade platform")
    private TradePlatform tradePlatform;

    @Schema(description = "Type of trade")
    private TradeType tradeType;

    @Schema(description = "Date & time of trade open")
    private LocalDateTime tradeOpenTime;

    @Schema(description = "Date & time of trade closure")
    private LocalDateTime tradeCloseTime;

    @Schema(description = "Size of trade")
    private double lotSize;

    @Schema(description = "Price at trade open")
    private double openPrice;

    @Schema(description = "Price at trade close")
    private double closePrice;

    @Schema(description = "Gain/Loss on trade")
    private double netProfit;

    @Schema(description = "Points gained or lost on trade")
    private double points;

    @Schema(description = "Stop loss price")
    private double stopLoss;

    @Schema(description = "Take profit price")
    private double takeProfit;

    @Schema(description = "Account that took the trade")
    private AccountDTO account;
}

