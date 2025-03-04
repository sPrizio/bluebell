package com.bluebell.platform.models.core.nonentities.records.account;


import com.bluebell.platform.models.core.entities.account.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

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
 * @author Stephen Prizio
 * @version 0.1.0
 */
@Schema(title = "AccountInsights", name = "AccountInsights", description = "Basic insights into an account's performance")
public record AccountInsights(
        @Getter @Schema(description = "Number of days traded") int tradingDays,
        @Getter @Schema(description = "Current profit or loss") double currentPL,
        @Getter @Schema(description = "Largest loss") double biggestLoss,
        @Getter @Schema(description = "Largest win") double largestGain,
        @Getter @Schema(description = "Largest overall losing period throughout life of account") double drawdown,
        @Getter @Schema(description = "Highest profitable point reached") double maxProfit,
        @Getter @Schema(description = "P&L expressed as percentage") double currentPLDelta,
        @Getter @Schema(description = "Largest loss expressed as a percentage") double biggestLossDelta,
        @Getter @Schema(description = "Largest win expressed as a percentage") double largestGainDelta,
        @Getter @Schema(description = "Drawdown expressed as a percentage") double drawdownDelta,
        @Getter @Schema(description = "Highest profit attained expressed as a percentage") double maxProfitDelta
) {
}
