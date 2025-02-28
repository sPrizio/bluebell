package com.bluebell.platform.models.core.nonentities.records.account;


import com.bluebell.platform.models.core.entities.account.Account;
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
public record AccountStatistics(
        @Getter double balance,
        @Getter double averageProfit,
        @Getter double averageLoss,
        @Getter int numberOfTrades,
        @Getter double rrr,
        @Getter double lots,
        @Getter double expectancy,
        @Getter int winPercentage,
        @Getter double profitFactor,
        @Getter int retention,
        @Getter double sharpeRatio,
        @Getter long tradeDuration,
        @Getter long winDuration,
        @Getter long lossDuration,
        @Getter double assumedDrawdown
) {
}
