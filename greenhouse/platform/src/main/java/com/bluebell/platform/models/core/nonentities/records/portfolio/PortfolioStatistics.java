package com.bluebell.platform.models.core.nonentities.records.portfolio;

/**
 * Class representation of a {@link Portfolio}'s statistics
 *
 * @param deltaNetWorth % change in net worth
 * @param deltaTrades % change in trades
 * @param deltaDeposits % change in deposits
 * @param deltaWithdrawals % change in withdrawals
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record PortfolioStatistics(double deltaNetWorth, double deltaTrades, double deltaDeposits, double deltaWithdrawals) {
}
