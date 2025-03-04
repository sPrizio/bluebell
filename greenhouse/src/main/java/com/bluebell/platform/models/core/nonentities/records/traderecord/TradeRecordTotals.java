package com.bluebell.platform.models.core.nonentities.records.traderecord;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Represents a simple statistical analysis of a {@link List} of {@link TradeRecord}s
 *
 * @param count         number of records
 * @param trades        number of trades
 * @param tradesWon     number of trades won
 * @param tradesLost    number of trades lost
 * @param winPercentage win percentage
 * @param netProfit     net profit
 * @param netPoints     net points
 * @author Stephen Prizio
 * @version 0.1.0
 */
@Schema(title = "TradeRecordTotals", name = "TradeRecordTotals", description = "Aggregated statistics about a collection of trade records")
public record TradeRecordTotals(
        @Getter @Schema(description = "Number of trade records") int count,
        @Getter @Schema(description = "Number of trades taken") int trades,
        @Getter @Schema(description = "Number of trades won") int tradesWon,
        @Getter @Schema(description = "Number of trades lost") int tradesLost,
        @Getter @Schema(description = "Number of trades won expressed as a percentage") int winPercentage,
        @Getter @Schema(description = "Net profit / loss") double netProfit,
        @Getter @Schema(description = "Net points gained / lost") double netPoints
) { }
