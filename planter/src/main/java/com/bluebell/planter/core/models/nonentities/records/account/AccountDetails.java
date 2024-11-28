package com.bluebell.planter.core.models.nonentities.records.account;

import com.bluebell.planter.core.models.entities.account.Account;

import java.util.List;

/**
 * Class representation of details about an {@link Account}
 *
 * @param account {@link Account}
 * @param consistency consistency score
 * @param equity {@link List} of {@link AccountEquityPoint}
 * @param insights {@link AccountInsights}
 * @param statistics {@link AccountStatistics}
 * @param riskFreeRate the risk-free rate by the government of Canada (as of November 28th, 2024)
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record AccountDetails(Account account, int consistency, List<AccountEquityPoint> equity, AccountInsights insights, AccountStatistics statistics, double riskFreeRate) {
}
