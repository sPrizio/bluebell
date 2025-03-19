package com.bluebell.platform.models.core.nonentities.records.portfolio;


import com.bluebell.platform.models.core.entities.account.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * Class representation of an {@link Account}'s value in a {@link PortfolioRecord}
 *
 * @param name {@link Account} name
 * @param value {@link Account} value (balance)
 * @author Stephen Prizio
 * @version 0.1.2
 */
@Builder
@Schema(title = "PortfolioAccountEquityPoint", name = "PortfolioAccountEquityPoint", description = "A data point of an account's value within a portfolio for a unit of time")
public record PortfolioAccountEquityPoint(
        @Getter @Schema(description = "Account name") String name,
        @Getter @Schema(description = "Account value") double value
) { }
