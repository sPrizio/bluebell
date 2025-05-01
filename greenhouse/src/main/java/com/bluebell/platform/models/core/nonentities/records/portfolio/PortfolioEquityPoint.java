package com.bluebell.platform.models.core.nonentities.records.portfolio;


import com.bluebell.platform.models.core.nonentities.portfolio.PortfolioAccountEquityPoint;
import com.bluebell.platform.services.MathService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representation of a portfolio's value at a point in time
 *
 * @param date date in time
 * @param portfolio total balance / net worth
 * @param accounts {@link List} of {@link PortfolioAccountEquityPoint}
 * @author Stephen Prizio
 * @version 0.2.0
 */
@Builder
@Schema(title = "PortfolioEquityPoint", name = "PortfolioEquityPoint", description = "Data representation of a point of equity for a portfolio for a unit of time")
public record PortfolioEquityPoint(
        @Getter @Schema(description = "Date of equity view") LocalDate date,
        @Getter @Schema(description = "Amount of equity of the portfolio for the date") double portfolio,
        @Getter @Schema(description = "List of account equity data points for each account in the portfolio") List<PortfolioAccountEquityPoint> accounts
) {

    /**
     * Merges a {@link PortfolioEquityPoint} with another {@link PortfolioEquityPoint}
     *
     * @param other {@link PortfolioEquityPoint} to merge
     * @return merged {@link PortfolioEquityPoint}
     */
    public PortfolioEquityPoint merge(final PortfolioEquityPoint other) {
        final MathService mathService = new MathService();
        final List<PortfolioAccountEquityPoint> points = new ArrayList<>(this.accounts);
        points.addAll(other.accounts);

        return PortfolioEquityPoint
                .builder()
                .date(this.date)
                .portfolio(mathService.add(this.portfolio, other.portfolio))
                .accounts(points)
                .build();
    }
}
