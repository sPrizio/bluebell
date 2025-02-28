package com.bluebell.platform.models.core.nonentities.records.traderecord;


import lombok.Getter;

/**
 * Class representation of a {@link TradeRecord}'s equity per count of trades, meant to be used on a chart
 *
 * @param count trade count
 * @param amount account balance
 * @param points account points
 * @param cumAmount cumulative account balance
 * @param cumPoints cumulative points
 * @author Stephen Prizio
 * @version 0.1.0
 */
public record TradeRecordEquityPoint(
        @Getter int count,
        @Getter double amount,
        @Getter double points,
        @Getter double cumAmount,
        @Getter double cumPoints
) { }
