package com.bluebell.platform.models.core.nonentities.records.trade;

import com.bluebell.platform.models.core.entities.trade.Trade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * Class representation of various {@link Trade} statistical measures regarding its performance
 *
 * @param dayOfWeek              day of week
 * @param rrr                    risk to reward ratio
 * @param risk                   Risk in points/dollars
 * @param riskEquityPercentage   Risk expressed as a percentage
 * @param reward                 Reward in points/dollars
 * @param rewardEquityPercentage Reward expressed as a percentage
 * @param duration               Trade duration (in seconds)
 * @param drawdown               Drawdown (non-realized risk)
 * @param drawdownPercentage     Drawdown expressed as a percentage
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Builder
@Schema(title = "TradeInsights", name = "TradeInsights", description = "Basic insights about a trade's performance")
public record TradeInsights(
        @Getter @Schema(description = "Day of the week") String dayOfWeek,
        @Getter @Schema(description = "Risk to Reward ratio") double rrr,
        @Getter @Schema(description = "Risk in points/dollars") double risk,
        @Getter @Schema(description = "Risk expressed as a percentage") double riskEquityPercentage,
        @Getter @Schema(description = "Reward in points/dollars") double reward,
        @Getter @Schema(description = "Reward expressed as a percentage") double rewardEquityPercentage,
        @Getter @Schema(description = "Trade duration (in seconds)") long duration,
        @Getter @Schema(description = "Drawdown (non-realized risk)") double drawdown,
        @Getter @Schema(description = "Drawdown expressed as a percentage") double drawdownPercentage
) {
}
