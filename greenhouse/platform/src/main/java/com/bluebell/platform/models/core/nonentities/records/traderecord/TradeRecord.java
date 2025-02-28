package com.bluebell.platform.models.core.nonentities.records.traderecord;


import com.bluebell.platform.enums.system.TradeRecordTimeInterval;
import com.bluebell.platform.models.core.entities.trade.Trade;
import lombok.Getter;

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
 * @version 0.1.0
 */
public record TradeRecord(
        @Getter LocalDate start,
        @Getter LocalDate end,
        @Getter double netProfit,
        @Getter double lowestPoint,
        @Getter double pointsGained,
        @Getter double pointsLost,
        @Getter double points,
        @Getter double largestWin,
        @Getter double winAverage,
        @Getter double largestLoss,
        @Getter double lossAverage,
        @Getter int winPercentage,
        @Getter int wins,
        @Getter int losses,
        @Getter int trades,
        @Getter double profitability,
        @Getter int retention,
        @Getter TradeRecordTimeInterval interval,
        @Getter List<TradeRecordEquityPoint> equityPoints
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
