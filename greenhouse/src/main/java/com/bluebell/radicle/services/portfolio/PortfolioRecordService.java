package com.bluebell.radicle.services.portfolio;

import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioAccountEquityPoint;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioEquityPoint;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioRecord;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioStatistics;
import com.bluebell.platform.services.MathService;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link PortfolioRecord}s
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@Service("portfolioRecordService")
public class PortfolioRecordService {

    private final MathService mathService = new MathService();

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    /**
     * Obtains the {@link User}'s {@link PortfolioRecord} for the matching portfolio
     *
     * @param portfolioUid portfolio uid
     * @param user {@link User}
     * @return {@link PortfolioRecord}
     */
    public PortfolioRecord getSinglePortfolioRecord(final String portfolioUid, final User user) {

        if (StringUtils.isEmpty(portfolioUid)) {
            throw new IllegalParameterException("Portfolio uid cannot be empty");
        }

        validateParameterIsNotNull(user, CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);
        final long id = this.uniqueIdentifierService.retrieveId(portfolioUid);
        final List<Account> accounts = user.getActivePortfolios().stream().filter(p -> p.getId().equals(id)).map(Portfolio::getActiveAccounts).flatMap(List::stream).toList();

        if (CollectionUtils.isEmpty(accounts)) {
            throw new UnsupportedOperationException(String.format("No accounts for portfolio with uid %s", portfolioUid));
        }

        return generateRecord(accounts);
    }

    /**
     * Obtains the {@link User}'s {@link PortfolioRecord} for all portfolios
     *
     * @param user {@link User}
     * @return {@link PortfolioRecord}
     */
    public PortfolioRecord getComprehensivePortfolioRecord(final User user) {
        validateParameterIsNotNull(user, CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);
        final List<Account> accounts = user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).toList();
        return generateRecord(accounts);
    }


    //  HELPERS

    /**
     * Generates a {@link PortfolioRecord} for the given accounts
     *
     * @param accounts {@link List} of {@link Account}s
     * @return {@link PortfolioRecord}
     */
    private PortfolioRecord generateRecord(final List<Account> accounts) {

        List<PortfolioEquityPoint> equityPoints = computeEquityPoints(accounts);
        boolean isNew = false;
        final LocalDate limit = accounts.stream().map(Account::getLastTraded).max(LocalDateTime::compareTo).orElse(LocalDateTime.now()).with(TemporalAdjusters.firstDayOfNextMonth()).toLocalDate();
        final double netWorth = this.mathService.getDouble(accounts.stream().mapToDouble(Account::getBalance).sum());

        if (CollectionUtils.isEmpty(equityPoints) || CollectionUtils.isEmpty(equityPoints.get(0).accounts())) {
            isNew = true;
            equityPoints = List.of(PortfolioEquityPoint.builder().date(limit).portfolio(netWorth).accounts(Collections.emptyList()).build(), PortfolioEquityPoint.builder().date(limit).portfolio(netWorth).accounts(Collections.emptyList()).build());
        }

        return PortfolioRecord
                .builder()
                .newPortfolio(isNew)
                .netWorth(this.mathService.getDouble(accounts.stream().mapToDouble(Account::getBalance).sum()))
                .trades(accounts.stream().map(Account::getTrades).mapToLong(List::size).sum())
                .deposits(accounts.stream().map(Account::getTransactions).filter(CollectionUtils::isNotEmpty).flatMap(List::stream).filter(tr -> tr.getTransactionType() == TransactionType.DEPOSIT).count())
                .withdrawals(accounts.stream().map(Account::getTransactions).filter(CollectionUtils::isNotEmpty).flatMap(List::stream).filter(tr -> tr.getTransactionType() == TransactionType.WITHDRAWAL).count())
                .statistics(computePortfolioStatistics(accounts))
                .equity(equityPoints)
                .build();
    }

    /**
     * Computes the {@link PortfolioStatistics}
     *
     * @param accounts {@link List} of {@link Account}s
     * @return {@link PortfolioStatistics}
     */
    private PortfolioStatistics computePortfolioStatistics(final List<Account> accounts) {

        final LocalDateTime limit = accounts.stream().filter(Account::isActive).map(Account::getLastTraded).max(LocalDateTime::compareTo).orElse(LocalDateTime.now()).with(TemporalAdjusters.firstDayOfNextMonth()).minusMonths(1);
        final List<Trade> allTrades =
                accounts
                        .stream()
                        .map(Account::getTrades)
                        .flatMap(List::stream)
                        .toList();

        final List<Transaction> allTransactions =
                accounts
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

        final double netAccount = accounts.stream().mapToDouble(Account::getBalance).sum();
        final double netProfit = differenceTrades.stream().mapToDouble(Trade::getNetProfit).sum();

        final List<Transaction> allDeposits = allTransactions.stream().filter(tr -> tr.getTransactionType().equals(TransactionType.DEPOSIT)).toList();
        final List<Transaction> allWithdrawals = allTransactions.stream().filter(tr -> tr.getTransactionType().equals(TransactionType.WITHDRAWAL)).toList();
        final List<Transaction> differenceDeposits = differenceTransactions.stream().filter(tr -> tr.getTransactionType().equals(TransactionType.DEPOSIT)).toList();
        final List<Transaction> differenceWithdrawals = differenceTransactions.stream().filter(tr -> tr.getTransactionType().equals(TransactionType.WITHDRAWAL)).toList();

        return PortfolioStatistics
                .builder()
                .deltaNetWorth(safeDivide(netProfit, BigDecimal.valueOf(netAccount).subtract(BigDecimal.valueOf(netProfit)).doubleValue()))
                .deltaTrades(safeDivide(differenceTrades.size(), BigDecimal.valueOf(allTrades.size()).subtract(BigDecimal.valueOf(differenceTrades.size())).doubleValue()))
                .deltaDeposits(safeDivide(differenceDeposits.size(), allDeposits.size()))
                .deltaWithdrawals(safeDivide(differenceWithdrawals.size(), allWithdrawals.size()))
                .build();
    }

    /**
     * Computes a {@link List} of {@link PortfolioEquityPoint}s
     *
     * @param accounts {@link List} of {@link Account}s
     * @return {@link List} of {@link PortfolioEquityPoint}s
     */
    private List<PortfolioEquityPoint> computeEquityPoints(final List<Account> accounts) {

        final List<PortfolioEquityPoint> points = new ArrayList<>();
        final LocalDateTime limit = accounts.stream().map(Account::getLastTraded).max(LocalDateTime::compareTo).orElse(LocalDateTime.now()).with(TemporalAdjusters.firstDayOfNextMonth());

        //  only look at accounts that were last traded within the timespan to reduce number of trades considered
        final List<Account> relevantAccounts = accounts.stream().filter(acc -> isWithinTimespan(limit.minusMonths(6), limit, acc.getLastTraded())).toList();
        LocalDateTime compare = limit;
        double starterBalance = this.mathService.getDouble(accounts.stream().mapToDouble(Account::getBalance).sum());

        while (compare.isAfter(limit.minusMonths(6))) {
            LocalDateTime start = compare;
            LocalDateTime end = compare.plusMonths(1);

            final List<PortfolioAccountEquityPoint> accountEquityPoints = new ArrayList<>();
            for (final Account account : relevantAccounts) {
                final List<Trade> relevant = account.getTrades().stream().filter(tr -> isWithinTimespan(start, end, tr.getTradeCloseTime())).toList();
                accountEquityPoints.add(PortfolioAccountEquityPoint.builder().name(account.getName()).value(this.mathService.getDouble(relevant.stream().mapToDouble(Trade::getNetProfit).sum())).build());
            }

            starterBalance -= this.mathService.getDouble(accountEquityPoints.stream().mapToDouble(PortfolioAccountEquityPoint::value).sum());
            points.add(PortfolioEquityPoint.builder().date(compare.toLocalDate()).portfolio(starterBalance).accounts(accountEquityPoints).build());

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
