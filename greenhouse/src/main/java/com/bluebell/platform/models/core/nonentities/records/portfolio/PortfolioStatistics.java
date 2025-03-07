package com.bluebell.platform.models.core.nonentities.records.portfolio;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * Class representation of a {@link Portfolio}'s statistics
 *
 * @param deltaNetWorth % change in net worth
 * @param deltaTrades % change in trades
 * @param deltaDeposits % change in deposits
 * @param deltaWithdrawals % change in withdrawals
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Builder
@Schema(title = "PortfolioStatistics", name = "PortfolioStatistics", description = "Basic statistical measures about a client's portfolio")
public record PortfolioStatistics(
        @Getter @Schema(description = "Net worth expressed as a percentage") double deltaNetWorth,
        @Getter @Schema(description = "Number of trades expressed as a percentage") double deltaTrades,
        @Getter @Schema(description = "Number of deposits expressed as a percentage") double deltaDeposits,
        @Getter @Schema(description = "Number of withdrawals expressed as a percentage") double deltaWithdrawals) {
}
