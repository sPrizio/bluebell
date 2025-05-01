package com.bluebell.platform.models.core.nonentities.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Represents an account's balance history for a period of time
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@Builder
@Schema(title = "AccountBalanceHistory", name = "AccountBalanceHistory", description = "A collection of historical balance values for an account")
public record AccountBalanceHistory(
        @Getter @Schema(description = "Start of period") LocalDate start,
        @Getter @Schema(description = "End of period") LocalDate end,
        @Getter @Schema(description = "Final balance of period") double balance,
        @Getter @Schema(description = "Change in balance for period") double delta,
        @Getter @Schema(description = "Delta has a whole percentage") double normalized
) { }
