package com.bluebell.planter.core.models.nonentities.records.portfolio;

import java.time.LocalDate;
import java.util.List;

/**
 * Class representation of a portfolio's value at a point in time
 *
 * @param date date in time
 * @param portfolio total balance / net worth
 * @param accounts {@link List} of {@link PortfolioAccountEquityPoint}
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record PortfolioEquityPoint(LocalDate date, double portfolio, List<PortfolioAccountEquityPoint> accounts) {
}
