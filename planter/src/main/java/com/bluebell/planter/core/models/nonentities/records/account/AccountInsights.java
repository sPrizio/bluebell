package com.bluebell.planter.core.models.nonentities.records.account;

import com.bluebell.planter.core.models.entities.account.Account;

/**
 * Class representation of an {@link Account's} insights, i.e. look at its performance
 *
 * @param tradingDays days traded
 * @param trades trades taken
 * @param maxDailyLoss max loss incurred in 1 day
 * @param maxTotalLoss total loss incurred on the account
 * @param maxDailyProfit max gain experienced in 1 day
 * @param maxProfit total gain experienced on the account
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record AccountInsights(int tradingDays, int trades, double maxDailyLoss, double maxTotalLoss, double maxDailyProfit, double maxProfit) {
}
