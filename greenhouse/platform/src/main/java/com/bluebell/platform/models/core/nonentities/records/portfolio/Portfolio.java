package com.bluebell.platform.models.core.nonentities.records.portfolio;


import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.security.User;
import lombok.Getter;

import java.util.List;

/**
 * Class representation of an overview of a {@link User}'s {@link Account}s
 *
 * @param newPortfolio if true, this portfolio is new
 * @param netWorth net worth (total balance of all {@link Account}s
 * @param trades total trades taken across all {@link Account}s
 * @param deposits total deposits taken across all {@link Account}s
 * @param withdrawals total withdrawals taken across all {@link Account}s
 * @param statistics {@link PortfolioStatistics}
 * @param equity {@link List} of {@link PortfolioEquityPoint}s
 * @author Stephen Prizio
 * @version 0.1.0
 */
public record Portfolio(
        @Getter boolean newPortfolio,
        @Getter double netWorth,
        @Getter long trades,
        @Getter long deposits,
        @Getter long withdrawals,
        @Getter PortfolioStatistics statistics,
        @Getter List<PortfolioEquityPoint> equity
) { }
