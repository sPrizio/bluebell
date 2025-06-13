package com.bluebell.platform.models.core.entities.trade;

import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.core.entities.GenericEntity;
import com.bluebell.platform.models.core.entities.account.Account;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Class representation of a trade in the market, a buy or sell exchange
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Getter
@Entity
@Builder
@Table(name = "trades", uniqueConstraints = @UniqueConstraint(name = "UniqueTradeIdAndAccount", columnNames = {"trade_id", "account_id"}))
@NoArgsConstructor
@AllArgsConstructor
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
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Account account;


    //  METHODS

    /**
     * Helper method for checking the trade is closed and not active
     *
     * @return true if an open and close time are specified
     */
    public boolean isClosed() {
        return this.tradeOpenTime != null && this.tradeCloseTime != null;
    }

    /**
     * Helper method for checking the trade is active
     *
     * @return true if an open time is specified and close time is empty
     */
    public boolean isOpen() {
        return this.tradeOpenTime != null && this.tradeCloseTime == null;
    }
}
