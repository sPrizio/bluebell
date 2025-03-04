package com.bluebell.platform.models.core.nonentities.records.account;


import com.bluebell.platform.models.core.entities.account.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Class representation of various {@link Account}-related statistical measures regarding its performance
 *
 * @param balance account balance
 * @param averageProfit average profit per trade
 * @param averageLoss average loss per trade
 * @param numberOfTrades number of trades taken on the account
 * @param rrr risk to reward ratio
 * @param lots total size traded
 * @param expectancy average expected return on any single given trade
 * @param winPercentage percentage of trades won
 * @param profitFactor size of your profits in relation to your losses
 * @param retention percentage points retained in trading (>50% indicates profitability)
 * @param sharpeRatio sharpe ratio
 * @param tradeDuration average trade duration in seconds
 * @param winDuration average win trade duration in seconds
 * @param lossDuration average loss trade duration in seconds
 * @param assumedDrawdown calculated drawdown plus average loss
 * @author Stephen Prizio
 * @version 0.1.0
 */
@Schema(title = "AccountStatistics", name = "AccountStatistics", description = "Basic statistics about an account")
public record AccountStatistics(
        @Getter @Schema(description = "Account Balance") double balance,
        @Getter @Schema(description = "Average profit per trade") double averageProfit,
        @Getter @Schema(description = "Average loss per trade") double averageLoss,
        @Getter @Schema(description = "Number of trades taken on the account") int numberOfTrades,
        @Getter @Schema(description = "Risk to Reward ratio") double rrr,
        @Getter @Schema(description = "Cumulative size traded") double lots,
        @Getter @Schema(description = "Expected amount per trade") double expectancy,
        @Getter @Schema(description = "Trades won expressed as a percentage") int winPercentage,
        @Getter @Schema(description = "Factor of profits versus losses") double profitFactor,
        @Getter @Schema(description = "Percentage of points retained for each trade") int retention,
        @Getter @Schema(description = "Sharpe ratio") double sharpeRatio,
        @Getter @Schema(description = "Average duration of a trade (in seconds)") long tradeDuration,
        @Getter @Schema(description = "Average duration of winning trades (in seconds)") long winDuration,
        @Getter @Schema(description = "Average duration of losing trades (in seconds)") long lossDuration,
        @Getter @Schema(description = "Speculated drawdown") double assumedDrawdown
) {
}
