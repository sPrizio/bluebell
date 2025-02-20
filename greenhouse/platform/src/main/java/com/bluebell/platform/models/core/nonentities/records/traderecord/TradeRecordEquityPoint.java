package com.bluebell.platform.models.core.nonentities.records.traderecord;


/**
 * Class representation of a {@link TradeRecord}'s equity per count of trades, meant to be used on a chart
 *
 * @param count trade count
 * @param amount account balance
 * @param points account points
 * @param cumAmount cumulative account balance
 * @param cumPoints cumulative points
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record TradeRecordEquityPoint(int count, double amount, double points, double cumAmount, double cumPoints) {
}
