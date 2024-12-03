package com.bluebell.planter.core.models.nonentities.records.portfolio;

import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.entities.security.User;

import java.util.List;

/**
 * Class representation of an overview of a {@link User}'s {@link Account}s
 *
 * @param isNew if true, this portfolio is new
 * @param netWorth net worth (total balance of all {@link Account}s
 * @param trades total trades taken across all {@link Account}s
 * @param deposits total deposits taken across all {@link Account}s
 * @param withdrawals total withdrawals taken across all {@link Account}s
 * @param statistics {@link PortfolioStatistics}
 * @param equity {@link List} of {@link PortfolioEquityPoint}s
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record Portfolio(boolean isNew, double netWorth, long trades, long deposits, long withdrawals, PortfolioStatistics statistics, List<PortfolioEquityPoint> equity) {
}
