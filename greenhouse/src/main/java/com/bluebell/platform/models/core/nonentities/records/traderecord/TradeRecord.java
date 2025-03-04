package com.bluebell.platform.models.core.nonentities.records.traderecord;


import java.time.LocalDate;
import java.util.List;

import com.bluebell.platform.enums.system.TradeRecordTimeInterval;
import com.bluebell.platform.models.core.entities.trade.Trade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

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
@Schema(title = "TradeRecord", name = "TradeRecord", description = "An aggregation of trades over a time period")
public record TradeRecord(
        @Getter @Schema(description = "Start of time period") LocalDate start,
        @Getter @Schema(description = "End of time period") LocalDate end,
        @Getter @Schema(description = "Net profit over time period") double netProfit,
        @Getter @Schema(description = "Lowest point (drawdown) over period") double lowestPoint,
        @Getter @Schema(description = "Points gained over period") double pointsGained,
        @Getter @Schema(description = "Points lost over period") double pointsLost,
        @Getter @Schema(description = "Net points over period") double points,
        @Getter @Schema(description = "Largest win of period") double largestWin,
        @Getter @Schema(description = "Average win over period") double winAverage,
        @Getter @Schema(description = "Largest loss of period") double largestLoss,
        @Getter @Schema(description = "Average loss over period") double lossAverage,
        @Getter @Schema(description = "Number of trades won expressed as a percentage over period") int winPercentage,
        @Getter @Schema(description = "Trades won over period") int wins,
        @Getter @Schema(description = "Trades lost over period") int losses,
        @Getter @Schema(description = "Trades taken over period") int trades,
        @Getter @Schema(description = "Profitability over period") double profitability,
        @Getter @Schema(description = "Percentage of points retained over period") int retention,
        @Getter @Schema(description = "Aggregation period") TradeRecordTimeInterval interval,
        @Getter @Schema(description = "List of equity points for use on a chart") List<TradeRecordEquityPoint> equityPoints
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
