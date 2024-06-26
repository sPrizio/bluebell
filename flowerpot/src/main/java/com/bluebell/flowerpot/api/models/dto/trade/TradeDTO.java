package com.bluebell.flowerpot.api.models.dto.trade;

import com.bluebell.flowerpot.api.models.dto.GenericDTO;
import com.bluebell.flowerpot.api.models.dto.account.AccountDTO;
import com.bluebell.flowerpot.core.enums.trade.info.TradeType;
import com.bluebell.flowerpot.core.enums.trade.platform.TradePlatform;
import com.bluebell.flowerpot.core.models.entities.trade.Trade;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * A DTO representation of a {@link Trade}
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
@Setter
@Getter
public class TradeDTO implements GenericDTO {

    private String uid;

    private String tradeId;

    private String product;

    private TradePlatform tradePlatform;

    private TradeType tradeType;

    private LocalDateTime tradeOpenTime;

    private LocalDateTime tradeCloseTime;

    private double lotSize;

    private double openPrice;

    private double closePrice;

    private double netProfit;

    private double points;

    private double stopLoss;

    private double takeProfit;

    private AccountDTO account;
}

