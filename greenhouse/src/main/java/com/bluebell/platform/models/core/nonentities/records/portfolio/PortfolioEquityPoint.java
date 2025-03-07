package com.bluebell.platform.models.core.nonentities.records.portfolio;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

/**
 * Class representation of a portfolio's value at a point in time
 *
 * @param date date in time
 * @param portfolio total balance / net worth
 * @param accounts {@link List} of {@link PortfolioAccountEquityPoint}
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Builder
@Schema(title = "PortfolioEquityPoint", name = "PortfolioEquityPoint", description = "Data representation of a point of equity for a portfolio for a unit of time")
public record PortfolioEquityPoint(
        @Getter @Schema(description = "Date of equity view") LocalDate date,
        @Getter @Schema(description = "Amount of equity of the portfolio for the date") double portfolio,
        @Getter @Schema(description = "List of account equity data points for each account in the portfolio") List<PortfolioAccountEquityPoint> accounts
) { }
