package com.bluebell.planter.core.models.entities.trade;

import com.bluebell.planter.core.enums.trade.info.TradeType;
import com.bluebell.planter.core.enums.trade.platform.TradePlatform;
import com.bluebell.planter.core.models.entities.GenericEntity;
import com.bluebell.planter.core.models.entities.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Class representation of a trade in the market, a buy or sell exchange
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
@Entity
@Table(name = "trades", uniqueConstraints = @UniqueConstraint(name = "UniqueTradeIdAndAccount", columnNames = {"trade_id", "account_id"}))
public class Trade implements GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "trade_id")
    private String tradeId;

    @Setter
    @Column
    private String product;

    @Setter
    @Column
    private TradePlatform tradePlatform;

    @Setter
    @Column
    private TradeType tradeType;

    @Setter
    @Column
    private LocalDateTime tradeOpenTime;

    @Setter
    @Column
    private LocalDateTime tradeCloseTime;

    @Setter
    @Column
    private double lotSize;

    @Setter
    @Column
    private double openPrice;

    @Setter
    @Column
    private double closePrice;

    @Setter
    @Column
    private double netProfit;

    @Setter
    @Column
    private double stopLoss;

    @Setter
    @Column
    private double takeProfit;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Account account;
}
