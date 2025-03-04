package com.bluebell.platform.models.core.nonentities.records.portfolio;


import java.util.List;

import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.security.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

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
@Schema(title = "Portfolio", name = "Portfolio", description = "DTO representation of a client's portfolio, including basic statistics")
public record Portfolio(
        @Getter @Schema(description = "Is the portfolio new?") boolean newPortfolio,
        @Getter @Schema(description = "Net worth of the portfolio") double netWorth,
        @Getter @Schema(description = "Number of trades taken in the portfolio") long trades,
        @Getter @Schema(description = "Number of deposits") long deposits,
        @Getter @Schema(description = "Number of withdrawals") long withdrawals,
        @Getter @Schema(description = "Basic statics about the portfolio") PortfolioStatistics statistics,
        @Getter @Schema(description = "List of portfolio equity points") List<PortfolioEquityPoint> equity
) { }
