package com.bluebell.anther.models.trade;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

import com.bluebell.anther.strategies.Strategy;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.services.MathService;
import lombok.Getter;
import lombok.Setter;

/**
 * Class representation of a trade taken in a {@link Strategy}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
@Setter
public class AntherTrade implements Comparable<AntherTrade> {

    private final MathService mathService = new MathService();

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

    public AntherTrade(final TradeType tradeType, final double lotSize, final LocalDateTime tradeOpenTime, final double openPrice, final double stopLoss, final double takeProfit) {
        this.id = UUID.randomUUID() + "_" + Base64.getEncoder().encodeToString(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME).getBytes());
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

    /**
     * Calculates the total profit for this trade
     *
     * @param pricePerPoint price per point
     * @return price per point * net points
     */
    public double calculateProfit(final double pricePerPoint) {
        return this.mathService.multiply(getPoints(), pricePerPoint);
    }

    @Override
    public int compareTo(AntherTrade o) {
        return this.tradeOpenTime.compareTo(o.tradeOpenTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AntherTrade trade = (AntherTrade) o;
        return this.id.equals(trade.id) && this.tradeType == trade.tradeType;
    }

    @Override
    public int hashCode() {
        int result = this.id.hashCode();
        result = 31 * result + this.tradeType.hashCode();
        return result;
    }
}
