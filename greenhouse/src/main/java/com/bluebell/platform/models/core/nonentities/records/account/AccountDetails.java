package com.bluebell.platform.models.core.nonentities.records.account;

import java.util.List;

import com.bluebell.platform.models.core.entities.account.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

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
 * @version 0.1.1
 */
@Builder
@Schema(title = "Account Details", name = "Account Details", description = "An entity containing account metrics and insights including statistical measures of performance, data for charting, consistency scores, etc.")
public record AccountDetails(
        @JsonIgnore Account account,
        @Schema(description = "Consistency score") int consistency,
        @Schema(description = "List of account equity points over time") List<AccountEquityPoint> equity,
        @Schema(description = "Insights about the account's performance") AccountInsights insights,
        @Schema(description = "Basic statistics about an account's performance") AccountStatistics statistics,
        @Schema(description = "Canadian risk-free-rate, used for calculating sharpe ratios") double riskFreeRate
) { }
