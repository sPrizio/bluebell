package com.bluebell.platform.models.core.nonentities.records.traderecord;


import com.bluebell.platform.enums.system.TradeRecordTimeInterval;
import com.bluebell.platform.models.core.entities.trade.Trade;

import java.time.LocalDate;
import java.util.List;

/**
 * Class representation of a collection of {@link Trade}s and their statistics
 *
 * @param start start of time span
 * @param end end of time span
 * @param netProfit net profit of period
 * @param lowestPoint largest draw-down point
 * @param pointsGained points earned
 * @param pointsLost points lost
 * @param largestWin largest win
 * @param winAverage average win size
 * @param largestLoss biggest loss
 * @param lossAverage average loss size
 * @param winPercentage percentage of trades won
 * @param wins trades won
 * @param losses trades lost
 * @param profitability points won / points lost
 * @param retention percentage of points won of total points
 * @param interval {@link TradeRecordTimeInterval}
 * @param trades total trades
 * @param points net points
 * @param equityPoints {@link List} of {@link TradeRecordEquityPoint}s
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record TradeRecord(
        LocalDate start,
        LocalDate end,
        double netProfit,
        double lowestPoint,
        double pointsGained,
        double pointsLost,
        double points,
        double largestWin,
        double winAverage,
        double largestLoss,
        double lossAverage,
        int winPercentage,
        int wins,
        int losses,
        int trades,
        double profitability,
        int retention,
        TradeRecordTimeInterval interval,
        List<TradeRecordEquityPoint> equityPoints
) implements Comparable<TradeRecord> {

    @Override
    public int compareTo(final TradeRecord o) {
        return this.start.compareTo(o.start());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        TradeRecord that = (TradeRecord) o;
        return this.end.equals(that.end) && this.start.equals(that.start) && this.interval == that.interval;
    }

    @Override
    public int hashCode() {
        int result = this.start.hashCode();
        result = 31 * result + this.end.hashCode();
        result = 31 * result + this.interval.hashCode();
        return result;
    }
}
