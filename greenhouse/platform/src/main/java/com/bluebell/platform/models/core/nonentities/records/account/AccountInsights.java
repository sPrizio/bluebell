package com.bluebell.platform.models.core.nonentities.records.account;


import com.bluebell.platform.models.core.entities.account.Account;

/**
 * Class representation of an {@link Account 's} insights, i.e. look at its performance
 *
 * @param tradingDays      days traded
 * @param currentPL        current net profit
 * @param biggestLoss      biggest individual loss
 * @param largestGain      biggest individual win
 * @param drawdown         total account drawdown
 * @param maxProfit        highest ROI reached
 * @param currentPLDelta   current net profit expressed as a percentage
 * @param biggestLossDelta biggest individual loss expressed as a percentage
 * @param largestGainDelta largest individual gain expressed as a percentage
 * @param drawdownDelta    total account drawdown expressed as a percentage
 * @param maxProfitDelta   highest ROI reached expressed as a percentage
 */
public record AccountInsights(
        int tradingDays,
        double currentPL,
        double biggestLoss,
        double largestGain,
        double drawdown,
        double maxProfit,
        double currentPLDelta,
        double biggestLossDelta,
        double largestGainDelta,
        double drawdownDelta,
        double maxProfitDelta
) {
}
