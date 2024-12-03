package com.bluebell.planter.core.services.portfolio;

import com.bluebell.planter.core.enums.transaction.TransactionType;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.entities.security.User;
import com.bluebell.planter.core.models.entities.trade.Trade;
import com.bluebell.planter.core.models.entities.transaction.Transaction;
import com.bluebell.planter.core.models.nonentities.records.portfolio.Portfolio;
import com.bluebell.planter.core.models.nonentities.records.portfolio.PortfolioAccountEquityPoint;
import com.bluebell.planter.core.models.nonentities.records.portfolio.PortfolioEquityPoint;
import com.bluebell.planter.core.models.nonentities.records.portfolio.PortfolioStatistics;
import com.bluebell.radicle.services.MathService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service-layer for {@link Portfolio}
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@Service("portfolioService")
public class PortfolioService {

    private final MathService mathService = new MathService();


    //  METHODS

    /**
     * Obtains the {@link User}'s {@link Portfolio}
     *
     * @param user {@link User}
     * @return {@link Portfolio}
     */
    public Portfolio getPortfolio(final User user) {

        final List<Account> accounts = user.getAccounts();
        List<PortfolioEquityPoint> equityPoints = computeEquityPoints(user);
        boolean isNew = false;
        final LocalDate limit = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
        final double netWorth = this.mathService.getDouble(user.getAccounts().stream().mapToDouble(Account::getBalance).sum());

        if (CollectionUtils.isEmpty(equityPoints) || CollectionUtils.isEmpty(equityPoints.getFirst().accounts())) {
            isNew = true;
            equityPoints = List.of(new PortfolioEquityPoint(limit, netWorth, Collections.emptyList()), new PortfolioEquityPoint(limit, netWorth, Collections.emptyList()));
        }

        return new Portfolio(
                isNew,
                accounts.stream().mapToDouble(Account::getBalance).sum(),
                accounts.stream().map(Account::getTrades).mapToLong(List::size).sum(),
                accounts.stream().map(Account::getTransactions).flatMap(List::stream).filter(tr -> tr.getTransactionType() == TransactionType.DEPOSIT).count(),
                accounts.stream().map(Account::getTransactions).flatMap(List::stream).filter(tr -> tr.getTransactionType() == TransactionType.WITHDRAWAL).count(),
                computePortfolioStatistics(user),
                equityPoints
        );
    }


    //  HELPERS

    /**
     * Computes the {@link PortfolioStatistics}
     *
     * @param user {@link User}
     * @return {@link PortfolioStatistics}
     */
    private PortfolioStatistics computePortfolioStatistics(final User user) {

        final LocalDateTime limit = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).minusDays(1).atStartOfDay();

        final List<Trade> allTrades =
                user.getAccounts()
                        .stream()
                        .map(Account::getTrades)
                        .flatMap(List::stream)
                        .toList();

        final List<Transaction> allTransactions =
                user.getAccounts()
                        .stream()
                        .map(Account::getTransactions)
                        .flatMap(List::stream)
                        .toList();

        final List<Trade> differenceTrades =
                allTrades
                        .stream()
                        .filter(tr -> tr.getTradeCloseTime().isAfter(limit))
                        .toList();

        final List<Transaction> differenceTransactions =
                allTransactions
                        .stream()
                        .filter(tr -> tr.getTransactionDate().isAfter(limit))
                        .toList();

        final double netAccount = user.getAccounts().stream().mapToDouble(Account::getBalance).sum();
        final double netProfit = differenceTrades.stream().mapToDouble(Trade::getNetProfit).sum();

        return new PortfolioStatistics(
                this.mathService.wholePercentage(netProfit, this.mathService.subtract(netAccount, netProfit)),
                this.mathService.divide(differenceTrades.size(), this.mathService.subtract(allTrades.size(), differenceTrades.size())),
                this.mathService.divide(differenceTransactions.stream().filter(tr -> tr.getTransactionType() == TransactionType.DEPOSIT).toList().size(), this.mathService.subtract(allTransactions.stream().filter(tr -> tr.getTransactionType() == TransactionType.DEPOSIT).toList().size(), differenceTransactions.stream().filter(tr -> tr.getTransactionType() == TransactionType.DEPOSIT).toList().size())),
                this.mathService.divide(differenceTransactions.stream().filter(tr -> tr.getTransactionType() == TransactionType.WITHDRAWAL).toList().size(), this.mathService.subtract(allTransactions.stream().filter(tr -> tr.getTransactionType() == TransactionType.WITHDRAWAL).toList().size(), differenceTransactions.stream().filter(tr -> tr.getTransactionType() == TransactionType.WITHDRAWAL).toList().size()))
        );
    }

    /**
     * Computes a {@link List} of {@link PortfolioEquityPoint}s
     *
     * @param user {@link User}
     * @return {@link List} of {@link PortfolioEquityPoint}s
     */
    private List<PortfolioEquityPoint> computeEquityPoints(final User user) {

        final List<PortfolioEquityPoint> points = new ArrayList<>();
        final LocalDateTime limit = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth()).atStartOfDay();
        LocalDateTime compare = limit.minusMonths(6);
        final LocalDateTime temp = compare;

        //  only look at accounts that were last traded within the timespan to reduce number of trades considered
        final List<Account> relevantAccounts = user.getAccounts().stream().filter(acc -> isWithinTimespan(temp, limit, acc.getLastTraded())).toList();
        while (compare.isBefore(limit)) {
            LocalDateTime start = compare;
            LocalDateTime end = compare.plusMonths(1);

            final List<PortfolioAccountEquityPoint> accountEquityPoints = new ArrayList<>();
            for (final Account account : relevantAccounts) {
                final List<Trade> relevant = account.getTrades().stream().filter(tr -> isWithinTimespan(start, end, tr.getTradeCloseTime())).toList();
                accountEquityPoints.add(new PortfolioAccountEquityPoint(account.getName(), relevant.stream().mapToDouble(Trade::getNetProfit).sum()));
            }

            points.add(new PortfolioEquityPoint(compare.toLocalDate(), this.mathService.getDouble(accountEquityPoints.stream().mapToDouble(PortfolioAccountEquityPoint::value).sum()), accountEquityPoints));
            compare = compare.plusMonths(1);
        }

        return points;
    }

    /**
     * Checks that the given {@link LocalDateTime} is contained within the given bounds
     *
     * @param start start of interval
     * @param end end of interval
     * @param compare compare date
     * @return true if within the time span, start is inclusive, end is exclusive
     */
    private boolean isWithinTimespan(final LocalDateTime start, final LocalDateTime end, final LocalDateTime compare) {
        if (start == null || end == null || compare == null) {
            return false;
        }

        return (compare.isAfter(start) || compare.isEqual(start)) && compare.isBefore(end);
    }
}
