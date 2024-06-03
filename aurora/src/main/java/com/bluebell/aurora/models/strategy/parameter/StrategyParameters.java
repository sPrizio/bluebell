package com.bluebell.aurora.models.strategy.parameter;

/**
 * Generic strategy parameter skeleton
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public interface StrategyParameters {

    /**
     * Get target profit
     *
     * @return {@link Integer}
     */
    int targetProfit();

    /**
     * Get stop loss
     *
     * @return {@link Integer}
     */
    int stopLoss();

    /**
     * Get start hour
     *
     * @return {@link Integer}
     */
    int startHour();

    /**
     * Get start minute
     *
     * @return {@link Integer}
     */
    int startMinute();

    /**
     * Get lot size
     *
     * @return {@link Double}
     */
    double lotSize();

    /**
     * Get price per point
     *
     * @return {@link Double}
     */
    double pricePerPoint();
}
