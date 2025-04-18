package com.bluebell.platform.models.core.entities.market;


import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.entities.GenericEntity;
import com.bluebell.platform.services.MathService;
import com.bluebell.radicle.enums.DataSource;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Class representation of a market price for an interval of time
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@Getter
@Entity
@Builder
@Table(name = "market_prices", uniqueConstraints = @UniqueConstraint(name = "UniqueDateAndTimeIntervalAndDataSourceAndSymbol", columnNames = {"price_date", "market_price_time_interval", "data_source", "symbol"}))
@NoArgsConstructor
@AllArgsConstructor
public class MarketPrice implements GenericEntity, Comparable<MarketPrice> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "price_date", nullable = false)
    private LocalDateTime date;

    @Setter
    @Column(name = "market_price_time_interval", nullable = false)
    private MarketPriceTimeInterval interval;

    @Setter
    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Setter
    @Column
    private double open;

    @Setter
    @Column
    private double high;

    @Setter
    @Column
    private double low;

    @Setter
    @Column
    private double close;

    @Setter
    @Column
    private long volume;

    @Setter
    @Column(name = "data_source", nullable = false)
    private DataSource dataSource;


    //  METHODS

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
        final double topArea = mathService.subtract(this.high, mathService.divide(this.getFullSize(true), 2.0));
        return this.open >= topArea && this.close >= topArea;
    }

    /**
     * A tombstone is the inverse of a hammer
     *
     * @return true if tomb
     */
    public boolean isTombstone() {
        final MathService mathService = new MathService();
        final double bottomArea = mathService.add(this.low, mathService.divide(this.getFullSize(true), 2.0));
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
        return this.high > anotherPrice.high && this.low < anotherPrice.low;
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
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;

        MarketPrice that = (MarketPrice) object;
        return Double.compare(open, that.open) == 0 && Double.compare(high, that.high) == 0 && Double.compare(low, that.low) == 0 && Double.compare(close, that.close) == 0 && volume == that.volume && date.equals(that.date) && interval == that.interval && symbol.equals(that.symbol) && dataSource == that.dataSource;
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + interval.hashCode();
        result = 31 * result + symbol.hashCode();
        result = 31 * result + Double.hashCode(open);
        result = 31 * result + Double.hashCode(high);
        result = 31 * result + Double.hashCode(low);
        result = 31 * result + Double.hashCode(close);
        result = 31 * result + Long.hashCode(volume);
        result = 31 * result + dataSource.hashCode();
        return result;
    }
}
