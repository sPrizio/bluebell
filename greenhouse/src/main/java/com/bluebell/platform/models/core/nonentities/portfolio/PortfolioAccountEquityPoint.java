package com.bluebell.platform.models.core.nonentities.portfolio;


import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Class representation of an {@link Account}'s value in a {@link PortfolioRecord}
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@Builder
@Schema(title = "PortfolioAccountEquityPoint", name = "PortfolioAccountEquityPoint", description = "A data point of an account's value within a portfolio for a unit of time")
public class PortfolioAccountEquityPoint {

    @Getter
    @Schema(description = "Account name")
    private String name;

    @Getter
    @Schema(description = "Account value")
    private double value;

    @Getter
    @Schema(description = "Account delta")
    private double delta;

    @Getter
    @Setter
    @Schema(description = "Normalized account value")
    private double normalized;
}
