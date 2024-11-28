package com.bluebell.planter.core.models.nonentities.records.account;

import com.bluebell.planter.core.models.entities.account.Account;

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
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record AccountStatistics(double balance, double averageProfit, double averageLoss, int numberOfTrades, double rrr, double lots, double expectancy, double winPercentage, double profitFactor, double retention, double sharpeRatio) {
}
