package com.bluebell.platform.models.core.nonentities.market;


import java.time.LocalDateTime;
import java.util.Objects;

import com.bluebell.platform.enums.time.PlatformTimeInterval;
import com.bluebell.platform.services.MathService;

/**
 * Representation of a market price for an interval of time
 *
 * @param date start date
 * @param interval {@link PlatformTimeInterval}
 * @param open open price
 * @param high highest price
 * @param low lowest price
 * @param close close price
 * @author Stephen Prizio
 * @version 0.0.9
 */
public record MarketPrice(
        LocalDateTime date,
        PlatformTimeInterval interval,
        double open,
        double high,
        double low,
        double close
) implements Comparable<MarketPrice> {


    //  CONSTRUCTORS

    public MarketPrice() {
        this(LocalDateTime.MIN, PlatformTimeInterval.ONE_DAY, 0.0, 0.0, 0.0, 0.0);
    }


    //  METHODS

    //  TODO: need to find a way to override this and make it dynamic to sprout or to the data source to include a standard error margin

    /**
     * Returns true if this is a bullish price movement
     *
     * @return true if the close was GREATER than the open
     */
    public boolean isBullish() {
        return this.close > this.open && Math.abs(this.close - this.open) > 1.25;
    }

    /**
     * Returns true if this is a bearish price movement
     *
     * @return true if close was LESS than the open
     */
    public boolean isBearish() {
        return this.close < this.open && Math.abs(this.close - this.open) > 1.25;
    }

    /**
     * A hammer is when the body of the candle is in the upper half of the overall candle
     *
     * @return true if candle
     */
    public boolean isHammer() {
        final MathService mathService = new MathService();
        final double topArea = mathService.subtract(this.high(), mathService.divide(this.getFullSize(true), 2.0));
        return this.open >= topArea && this.close >= topArea;
    }

    /**
     * A tombstone is the inverse of a hammer
     *
     * @return true if tomb
     */
    public boolean isTombstone() {
        final MathService mathService = new MathService();
        final double bottomArea = mathService.add(this.low(), mathService.divide(this.getFullSize(true), 2.0));
        return this.open <= bottomArea && this.close <= bottomArea;
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

    /**
     * Returns the change between the open and close
     *
     * @param agnostic if true, return as an absolute value
     * @return body size
     */
    public double getBodySize(final boolean agnostic) {

        final MathService mathService = new MathService();
        if (agnostic) {
            return Math.abs(mathService.subtract(this.open, this.close));
        } else {
            return mathService.subtract(this.close, this.open);
        }
    }

    /**
     * returns the total change between high and low
     *
     * @param agnostic if ture, return as an absolute value
     * @return candle size
     */
    public double getFullSize(final boolean agnostic) {

        final MathService mathService = new MathService();
        if (agnostic) {
            return Math.abs(mathService.subtract(this.high, this.low));
        } else {
            return mathService.subtract(this.high, this.low);
        }
    }

    /**
     * Returns true if the price contains the necessary data
     *
     * @return true if open and close are not zero
     */
    public boolean isNotEmpty() {
        return this.open != 0.0 && this.close != 0.0;
    }

    /**
     * Disregard prices that engulf another in both directions
     *
     * @param anotherPrice {@link MarketPrice}
     * @return true if candle engulfs given candle
     */
    public boolean isMutuallyExclusive(final MarketPrice anotherPrice) {
        return this.high > anotherPrice.high() && this.low < anotherPrice.low();
    }

    /**
     * Returns true if the current market price is bullish or has a hammer formation
     *
     * @return true / false
     */
    public boolean hasBullishIndication() {
        return isBullish() || isHammer();
    }

    /**
     * Returns true if the current market price is bearish or has a tombstone formation
     *
     * @return true / false
     */
    public boolean hasBearishIndication() {
        return isBearish() || isTombstone();
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
