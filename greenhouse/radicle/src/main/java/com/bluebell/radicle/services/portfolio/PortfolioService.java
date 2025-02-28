package com.bluebell.radicle.services.portfolio;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.platform.models.core.nonentities.records.portfolio.Portfolio;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioAccountEquityPoint;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioEquityPoint;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioStatistics;
import com.bluebell.platform.services.MathService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link Portfolio}
 *
 * @author Stephen Prizio
 * @version 0.1.0
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

        validateParameterIsNotNull(user, CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);

        final List<Account> accounts = user.getAccounts();
        List<PortfolioEquityPoint> equityPoints = computeEquityPoints(user);
        boolean isNew = false;
        final LocalDate limit = user.getAccounts().stream().map(Account::getLastTraded).max(LocalDateTime::compareTo).orElse(LocalDateTime.now()).with(TemporalAdjusters.firstDayOfNextMonth()).toLocalDate();
        final double netWorth = this.mathService.getDouble(user.getAccounts().stream().mapToDouble(Account::getBalance).sum());

        if (CollectionUtils.isEmpty(equityPoints) || CollectionUtils.isEmpty(equityPoints.get(0).accounts())) {
            isNew = true;
            equityPoints = List.of(new PortfolioEquityPoint(limit, netWorth, Collections.emptyList()), new PortfolioEquityPoint(limit, netWorth, Collections.emptyList()));
        }

        return new Portfolio(
                isNew,
                accounts.stream().mapToDouble(Account::getBalance).sum(),
                accounts.stream().map(Account::getTrades).mapToLong(List::size).sum(),
                accounts.stream().map(Account::getTransactions).filter(CollectionUtils::isNotEmpty).flatMap(List::stream).filter(tr -> tr.getTransactionType() == TransactionType.DEPOSIT).count(),
                accounts.stream().map(Account::getTransactions).filter(CollectionUtils::isNotEmpty).flatMap(List::stream).filter(tr -> tr.getTransactionType() == TransactionType.WITHDRAWAL).count(),
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

        final LocalDateTime limit = user.getAccounts().stream().map(Account::getLastTraded).max(LocalDateTime::compareTo).orElse(LocalDateTime.now()).with(TemporalAdjusters.firstDayOfNextMonth()).minusMonths(1);

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
                        .filter(CollectionUtils::isNotEmpty)
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

        final List<Transaction> allDeposits = allTransactions.stream().filter(tr -> tr.getTransactionType().equals(TransactionType.DEPOSIT)).toList();
        final List<Transaction> allWithdrawals = allTransactions.stream().filter(tr -> tr.getTransactionType().equals(TransactionType.WITHDRAWAL)).toList();
        final List<Transaction> differenceDeposits = differenceTransactions.stream().filter(tr -> tr.getTransactionType().equals(TransactionType.DEPOSIT)).toList();
        final List<Transaction> differenceWithdrawals = differenceTransactions.stream().filter(tr -> tr.getTransactionType().equals(TransactionType.WITHDRAWAL)).toList();

        return new PortfolioStatistics(
                safeDivide(netProfit, BigDecimal.valueOf(netAccount).subtract(BigDecimal.valueOf(netProfit)).doubleValue()),
                safeDivide(differenceTrades.size(), BigDecimal.valueOf(allTrades.size()).subtract(BigDecimal.valueOf(differenceTrades.size())).doubleValue()),
                safeDivide(differenceDeposits.size(), allDeposits.size()),
                safeDivide(differenceWithdrawals.size(), allWithdrawals.size())
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
        final LocalDateTime limit = user.getAccounts().stream().map(Account::getLastTraded).max(LocalDateTime::compareTo).orElse(LocalDateTime.now()).with(TemporalAdjusters.firstDayOfNextMonth());


        //  only look at accounts that were last traded within the timespan to reduce number of trades considered
        final List<Account> relevantAccounts = user.getAccounts().stream().filter(acc -> isWithinTimespan(limit.minusMonths(6), limit, acc.getLastTraded())).toList();
        LocalDateTime compare = limit;
        double starterBalance = this.mathService.getDouble(user.getAccounts().stream().mapToDouble(Account::getBalance).sum());

        while (compare.isAfter(limit.minusMonths(6))) {
            LocalDateTime start = compare;
            LocalDateTime end = compare.plusMonths(1);

            final List<PortfolioAccountEquityPoint> accountEquityPoints = new ArrayList<>();
            for (final Account account : relevantAccounts) {
                final List<Trade> relevant = account.getTrades().stream().filter(tr -> isWithinTimespan(start, end, tr.getTradeCloseTime())).toList();
                accountEquityPoints.add(new PortfolioAccountEquityPoint(account.getName(), this.mathService.getDouble(relevant.stream().mapToDouble(Trade::getNetProfit).sum())));
            }

            starterBalance -= this.mathService.getDouble(accountEquityPoints.stream().mapToDouble(PortfolioAccountEquityPoint::value).sum());
            points.add(new PortfolioEquityPoint(compare.toLocalDate(), starterBalance, accountEquityPoints));


            compare = compare.minusMonths(1);
        }

        return points.stream().sorted(Comparator.comparing(PortfolioEquityPoint::date)).toList();
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

    /**
     * Safely divides 2 doubles
     *
     * @param a dividend
     * @param b divisor
     * @return quotient
     */
    private double safeDivide(final double a, final double b) {
        if (b == 0.0) {
            return a * 100.0;
        }

        return this.mathService.getDouble(BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), 25, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100.0)).doubleValue());
    }
}
