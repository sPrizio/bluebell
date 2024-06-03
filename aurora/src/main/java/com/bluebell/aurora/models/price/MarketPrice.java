package com.bluebell.aurora.models.price;

import com.bluebell.aurora.enums.TimeInterval;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representation of a market price for an interval of time
 *
 * @param date start date
 * @param interval {@link TimeInterval}
 * @param open open price
 * @param high highest price
 * @param low lowest price
 * @param close close price
 * @author Stephen Prizio
 * @version 0.0.1
 */
public record MarketPrice(
        LocalDateTime date,
        TimeInterval interval,
        double open,
        double high,
        double low,
        double close
) implements Comparable<MarketPrice> {


    //  CONSTRUCTORS

    public MarketPrice() {
        this(LocalDateTime.MIN, TimeInterval.ONE_DAY, 0.0, 0.0, 0.0, 0.0);
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
