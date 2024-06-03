package com.bluebell.aurora.models.trade;

import com.bluebell.aurora.enums.TradeType;
import com.bluebell.aurora.strategies.Strategy;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Class representation of a trade taken in a {@link Strategy}
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
@Setter
public class Trade implements Comparable<Trade> {

    private String id;

    private TradeType tradeType;

    private double lotSize;

    private LocalDateTime tradeOpenTime;

    private LocalDateTime tradeCloseTime;

    private double openPrice;

    private double closePrice;

    private double stopLoss;

    private double takeProfit;


    //  CONSTRUCTORS

    public Trade(final TradeType tradeType, final double lotSize, final LocalDateTime tradeOpenTime, final double openPrice, final double stopLoss, final double takeProfit) {
        this.id = UUID.randomUUID().toString();
        this.tradeType = tradeType;
        this.lotSize = lotSize;
        this.tradeOpenTime = tradeOpenTime;
        this.openPrice = openPrice;
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
    }


    //  METHODS

    /**
     * Returns the net points gained/lost by comparing the open and closing price
     *
     * @return close price - open price
     */
    public double getPoints() {

        if (tradeType == TradeType.BUY) {
            return this.closePrice - this.openPrice;
        }

        return this.openPrice - this.closePrice;
    }

    /**
     * Returns the duration of the trade in seconds
     *
     * @return time between trade open and close time
     */
    public long getTradeDuration() {

        if (this.tradeCloseTime == null) {
            return 0L;
        }

        return Math.abs(ChronoUnit.MINUTES.between(this.tradeOpenTime, this.tradeCloseTime));
    }

    @Override
    public int compareTo(Trade o) {
        return this.tradeOpenTime.compareTo(o.tradeOpenTime);
    }
}
