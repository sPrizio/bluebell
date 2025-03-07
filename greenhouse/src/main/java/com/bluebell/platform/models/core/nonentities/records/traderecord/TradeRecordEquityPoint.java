package com.bluebell.platform.models.core.nonentities.records.traderecord;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
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
 * @version 0.1.1
 */
@Builder
@Schema(title = "TradeRecordEquityPoint", name = "TradeRecordEquityPoint", description = "A data representation of a cumulative view of trading performance in a trade record for use on a graph")
public record TradeRecordEquityPoint(
        @Getter @Schema(description = "Number in a sequence of points (x-coordinate)") int count,
        @Getter @Schema(description = "Net profit") double amount,
        @Getter @Schema(description = "Net points") double points,
        @Getter @Schema(description = "Cumulative profit") double cumAmount,
        @Getter @Schema(description = "Cumulative points") double cumPoints
) { }
