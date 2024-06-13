package com.bluebell.radicle.models;


import com.bluebell.radicle.enums.RadicleTimeInterval;
import com.bluebell.radicle.services.MathService;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representation of a market price for an interval of time
 *
 * @param date start date
 * @param interval {@link RadicleTimeInterval}
 * @param open open price
 * @param high highest price
 * @param low lowest price
 * @param close close price
 * @author Stephen Prizio
 * @version 0.0.1
 */
public record MarketPrice(
        LocalDateTime date,
        RadicleTimeInterval interval,
        double open,
        double high,
        double low,
        double close
) implements Comparable<MarketPrice> {


    //  CONSTRUCTORS

    public MarketPrice() {
        this(LocalDateTime.MIN, RadicleTimeInterval.ONE_DAY, 0.0, 0.0, 0.0, 0.0);
    }


    //  METHODS

    /**
     * Returns true if this is a bullish price movement
     *
     * @return true if the close was GREATER than the open
     */
    public boolean isBullish() {
        return this.close > this.open;
    }

    /**
     * Returns true if this is a bearish price movement
     *
     * @return true if close was LESS than the open
     */
    public boolean isBearish() {
        return this.close < this.open;
    }

    /**
     * A doji is a price movement where the open and close are within a small distance from each other and a fraction of the overall movement
     * Note: Here we assume doji candles to have bodies less than 10% of the overall movement
     *
     * @return true if the distance between the open and close is less than 10% from the overall highs and lows
     */
    public boolean isDoji() {

        final MathService mathService = new MathService();
        final double body = Math.abs(mathService.subtract(this.open, this. close));
        final double wicks = Math.abs(mathService.subtract(this.high, this.low));

        return mathService.wholePercentage(body, wicks) <= 10;
    }

    /**
     * If the market price is not indecisive, it will have a larger enough difference in open and close
     *
     * @return inverse of doji
     */
    public boolean hasFullBody() {
        return !this.isDoji();
    }

    @Override
    public int compareTo(MarketPrice o) {
        return this.date.compareTo(o.date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketPrice that = (MarketPrice) o;
        return Objects.equals(this.date, that.date) && this.interval == that.interval;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.date, this.interval);
    }
}
