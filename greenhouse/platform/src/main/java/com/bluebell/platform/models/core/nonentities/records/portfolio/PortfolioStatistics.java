package com.bluebell.platform.models.core.nonentities.records.portfolio;

import lombok.Getter;

/**
 * Class representation of a {@link Portfolio}'s statistics
 *
 * @param deltaNetWorth % change in net worth
 * @param deltaTrades % change in trades
 * @param deltaDeposits % change in deposits
 * @param deltaWithdrawals % change in withdrawals
 * @author Stephen Prizio
 * @version 0.1.0
 */
public record PortfolioStatistics(
        @Getter double deltaNetWorth,
        @Getter double deltaTrades,
        @Getter double deltaDeposits,
        @Getter double deltaWithdrawals) {
}
