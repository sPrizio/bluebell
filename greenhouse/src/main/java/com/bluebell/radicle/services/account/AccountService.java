package com.bluebell.radicle.services.account;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.enums.account.AccountType;
import com.bluebell.platform.enums.account.Broker;
import com.bluebell.platform.enums.account.Currency;
import com.bluebell.platform.enums.system.TradeRecordTimeInterval;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.enums.transaction.TransactionStatus;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.api.dto.account.CreateUpdateAccountDTO;
import com.bluebell.platform.models.api.dto.account.CreateUpdateAccountTradingDataDTO;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.platform.models.core.nonentities.account.AccountBalanceHistory;
import com.bluebell.platform.models.core.nonentities.records.account.AccountDetails;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecordReport;
import com.bluebell.platform.services.MathService;
import com.bluebell.radicle.exceptions.system.EntityCreationException;
import com.bluebell.radicle.exceptions.system.EntityModificationException;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.repositories.account.AccountRepository;
import com.bluebell.radicle.services.portfolio.PortfolioService;
import com.bluebell.radicle.services.trade.TradeRecordService;
import com.bluebell.radicle.services.trade.TradeService;
import com.bluebell.radicle.services.transaction.TransactionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;


/**
 * Service-layer for {@link Account} entities
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Slf4j
@Service
public class AccountService {

    private final MathService mathService = new MathService();

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "accountDetailsService")
    private AccountDetailsService accountDetailsService;

    @Resource(name = "portfolioService")
    private PortfolioService portfolioService;

    @Resource(name = "tradeRecordService")
    private TradeRecordService tradeRecordService;

    @Resource(name = "tradeService")
    private TradeService tradeService;

    @Resource(name = "transactionService")
    private TransactionService transactionService;

    @Value("${bluebell.stale.account.lookback}")
    private long lookbackPeriod;


    //  METHODS

    /**
     * Refreshes the given {@link Account}
     *
     * @param account {@link Account}
     * @return refreshed {@link Account}
     */
    @Transactional
    public Account refreshAccount(final Account account) {
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        final double start = account.getInitialBalance();
        double sum = 0.0;
        final List<Trade> allTrades = this.tradeService.findAllTradesWithinTimespan(account.getAccountOpenTime(), LocalDateTime.now().plusDays(1), account);

        if (CollectionUtils.isNotEmpty(allTrades)) {
            sum = this.mathService.getDouble(allTrades.stream().mapToDouble(Trade::getNetProfit).sum());
        }

        account.setBalance(this.mathService.add(start, sum));
        return this.accountRepository.save(account);
    }

    /**
     * Returns an {@link AccountDetails} for the given {@link Account}
     *
     * @param account {@link Account}
     * @return {@link AccountDetails}
     */
    public AccountDetails getAccountDetails(final Account account) {
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        return AccountDetails
                .builder()
                .account(account)
                .consistency(this.accountDetailsService.calculateConsistencyScore(account))
                .equity(this.accountDetailsService.calculateEquityPoints(account))
                .insights(this.accountDetailsService.obtainInsights(account))
                .statistics(this.accountDetailsService.obtainStatistics(account))
                .riskFreeRate(CorePlatformConstants.RISK_FREE_RATE_CANADA)
                .build();
    }

    /**
     * Obtains an {@link Account} for the given account number
     *
     * @param accountNumber account number
     * @return {@link Optional} of {@link Account}
     */
    @Transactional
    public Optional<Account> findAccountByAccountNumber(final long accountNumber) {
        return Optional.ofNullable(this.accountRepository.findAccountByAccountNumber(accountNumber));
    }

    /**
     * Creates a new {@link Account} with the given data
     *
     * @param data      {@link CreateUpdateAccountDTO}
     * @param portfolio {@link Portfolio}
     * @return new {@link Account}
     */
    public Account createNewAccount(final CreateUpdateAccountDTO data, final Portfolio portfolio) {
        validateParameterIsNotNull(portfolio, CorePlatformConstants.Validation.Portfolio.PORTFOLIO_CANNOT_BE_NULL);

        if (data == null || data.number() == null) {
            throw new MissingRequiredDataException("The required data for creating an Account entity was null or empty");
        }

        try {
            return applyChanges(Account.builder().build(), data, portfolio, true);
        } catch (Exception e) {
            throw new EntityCreationException(String.format("An Account could not be created : %s", e.getMessage()), e);
        }
    }

    /**
     * Updates an existing {@link Account}
     *
     * @param account   {@link Account} to update
     * @param data      {@link CreateUpdateAccountDTO}
     * @param portfolio {@link Portfolio}
     * @return updated {@link Account}
     */
    public Account updateAccount(final Account account, final CreateUpdateAccountDTO data, final Portfolio portfolio) {
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
        validateParameterIsNotNull(portfolio, CorePlatformConstants.Validation.Portfolio.PORTFOLIO_CANNOT_BE_NULL);

        if (data == null || data.number() == null) {
            throw new MissingRequiredDataException("The required data for updating an Account was null or empty");
        }

        try {
            return applyChanges(account, data, portfolio, false);
        } catch (Exception e) {
            throw new EntityModificationException(String.format("An error occurred while modifying the Account : %s", e.getMessage()), e);
        }
    }

    /**
     * Deletes the given {@link Account}
     *
     * @param account {@link Account}
     * @return true if the account was deleted
     */
    @Transactional
    public boolean deleteAccount(final Account account) {
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        try {
            this.accountRepository.deleteById(account.getId());
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }


    /**
     * Reassigns default accounts
     *
     * @param portfolioNumber portfolio id of accounts to reassign
     */
    @Transactional
    public void reassignAccounts(final long portfolioNumber) {

        final Optional<Portfolio> portfolio = this.portfolioService.findPortfolioForPortfolioNumber(portfolioNumber);
        if (portfolio.isEmpty()) {
            LOGGER.warn("No accounts to reassign for portfolio {}", portfolioNumber);
            return;
        }

        final List<Account> accounts = portfolio.get().getActiveAccounts();
        if (CollectionUtils.isNotEmpty(accounts)) {
            if (accounts.stream().anyMatch(Account::isDefaultAccount)) {
                LOGGER.warn("{} already has a default account. Nothing to re-assign", portfolio);
                return;
            }

            final Account first = accounts.stream().min(Comparator.comparing(Account::getAccountNumber)).orElse(null);
            if (first == null) {
                LOGGER.warn("IMPOSSIBLE ERROR");
                return;
            }

            first.setDefaultAccount(true);
            this.accountRepository.save(first);
        }
    }

    /**
     * Updates account trading data for the given payload
     *
     * @param data {@link CreateUpdateAccountTradingDataDTO}
     * @return true if information is updated
     */
    public boolean updateAccountTradingData(final CreateUpdateAccountTradingDataDTO data) {
        validateParameterIsNotNull(data, "trades dto cannot be null");
        validateParameterIsNotNull(data.userIdentifier(), CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);

        final Optional<Account> account = findAccountByAccountNumber(data.accountNumber());
        if (account.isEmpty()) {
            LOGGER.error("Account not found for number {}", data.accountNumber());
            return false;
        }

        try {
            if (CollectionUtils.isNotEmpty(data.trades())) {
                final List<Trade> trades = data.trades().stream().map(td ->
                        Trade
                                .builder()
                                .tradeId(td.tradeId())
                                .product(td.product())
                                .tradePlatform(GenericEnum.getByCode(TradePlatform.class, td.tradePlatform()))
                                .tradeType(GenericEnum.getByCode(TradeType.class, td.tradeType()))
                                .tradeOpenTime(LocalDateTime.parse(td.tradeOpenTime(), DateTimeFormatter.ofPattern(CorePlatformConstants.DATE_TIME_NO_TIMEZONE)))
                                .tradeCloseTime(LocalDateTime.parse(td.tradeCloseTime(), DateTimeFormatter.ofPattern(CorePlatformConstants.DATE_TIME_NO_TIMEZONE)))
                                .lotSize(this.mathService.getDouble(td.lotSize()))
                                .openPrice(this.mathService.getDouble(td.openPrice()))
                                .closePrice(this.mathService.getDouble(td.closePrice()))
                                .netProfit(this.mathService.getDouble(td.netProfit()))
                                .stopLoss(this.mathService.getDouble(td.stopLoss()))
                                .takeProfit(this.mathService.getDouble(td.takeProfit()))
                                .account(account.get())
                                .build()
                ).toList();

                this.tradeService.saveAll(trades, account.get());
            }

            if (CollectionUtils.isNotEmpty(data.transactions())) {
                final List<Transaction> transactions = data.transactions().stream().map(tr ->
                        Transaction
                                .builder()
                                .transactionType(GenericEnum.getByCode(TransactionType.class, tr.transactionType()))
                                .transactionDate(LocalDateTime.parse(tr.transactionDate(), DateTimeFormatter.ofPattern(CorePlatformConstants.DATE_TIME_NO_TIMEZONE)))
                                .name(tr.name())
                                .transactionStatus(GenericEnum.getByCode(TransactionStatus.class, tr.transactionStatus()))
                                .amount(tr.amount())
                                .account(account.get())
                                .build()
                ).toList();

                this.transactionService.saveAll(transactions, account.get());
            }

            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }


    /**
     * Marks stale {@link Account}s as inactive. A stale account is one that has not been traded on within the lookback period.
     * The default period is 1 year.
     *
     * @return count of accounts changed
     */
    @Transactional
    public int invalidateStaleAccounts() {

        int count = 0;
        final LocalDateTime lookbackTime = LocalDateTime.now().minusSeconds(this.lookbackPeriod);
        final List<Account> accounts = this.accountRepository.findAccountsWhereLastTimeTradedIsBeyondLookback(lookbackTime);

        if (CollectionUtils.isNotEmpty(accounts)) {
            for (final Account account : accounts) {
                account.setActive(false);
                account.setDefaultAccount(false);
                this.accountRepository.save(account);
                count += 1;
            }
        }

        return count;
    }

    /**
     * Generates a {@link List} of {@link AccountBalanceHistory} for the given {@link Account} and {@link TradeRecordTimeInterval}
     *
     * @param account      {@link Account}
     * @param timeInterval {@link TradeRecordTimeInterval}
     * @return {@link List} of {@link AccountBalanceHistory}
     */
    public List<AccountBalanceHistory> generateAccountBalanceHistory(final Account account, final TradeRecordTimeInterval timeInterval) {
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
        validateParameterIsNotNull(timeInterval, CorePlatformConstants.Validation.System.TIME_INTERVAL_CANNOT_BE_NULL);

        if (!account.isActive() || account.getAccountCloseTime() != null) {
            return Collections.emptyList();
        }

        double runningBalance = account.getInitialBalance();
        final LocalDate startDate = account.getAccountOpenTime().toLocalDate();
        LocalDate compareStart = timeInterval == TradeRecordTimeInterval.MONTHLY ? startDate.with(TemporalAdjusters.firstDayOfMonth()) : startDate;
        LocalDate compareEnd = compareStart.plus(timeInterval.getAmount(), timeInterval.getUnit());
        final LocalDate endDate = LocalDate.now().plusDays(1);


        final List<AccountBalanceHistory> values = new ArrayList<>();
        while (compareStart.isBefore(endDate)) {
            final TradeRecordReport records = this.tradeRecordService.getTradeRecords(compareStart, compareEnd, account, timeInterval, CorePlatformConstants.MAX_RESULT_SIZE);
            final double percentage = this.mathService.delta(records.tradeRecordTotals().netProfit(), runningBalance);
            runningBalance = this.mathService.add(runningBalance, records.tradeRecordTotals().netProfit());
            values.add(AccountBalanceHistory.builder().start(compareStart).end(compareEnd).balance(runningBalance).normalized(percentage).delta(records.tradeRecordTotals().netProfit()).build());

            compareStart = compareStart.plus(timeInterval.getAmount(), timeInterval.getUnit());
            compareEnd = compareEnd.plus(timeInterval.getAmount(), timeInterval.getUnit());
        }

        return values.stream().sorted(Comparator.comparing(AccountBalanceHistory::getStart)).toList();
    }


    //  HELPERS

    /**
     * Applies the changes to the given {@link Account}
     *
     * @param account   {@link Account}
     * @param data      {@link CreateUpdateAccountDTO}
     * @param portfolio {@link Portfolio}
     * @param isNew     is this a new account
     * @return updated {@link Account}
     */
    private Account applyChanges(Account account, final CreateUpdateAccountDTO data, final Portfolio portfolio, final boolean isNew) {

        if (isNew) {
            account.setAccountOpenTime(LocalDateTime.now());

            if (!Objects.isNull(data.active())) {
                account.setActive(data.active());
            } else {
                account.setActive(true);
            }

            account.setPortfolio(portfolio);
            account.setBalance(data.balance());
            account.setInitialBalance(data.balance());
        } else {
            account.setActive(data.active());
            account.setBalance(data.balance());
        }

        account.setName(data.name());
        account.setAccountNumber(data.number());
        account.setCurrency(Currency.getByIsoCode(data.currency()));
        account.setAccountType(AccountType.valueOf(data.type()));
        account.setBroker(Broker.valueOf(data.broker()));
        account.setTradePlatform(TradePlatform.getByCode(data.tradePlatform()));

        if (isNew || account.getLastTraded() == null) {
            account.setLastTraded(LocalDateTime.now());
        }

        if (!Objects.isNull(data.isDefault()) && data.isDefault()) {
            portfolio.getAccounts().forEach(a -> {
                a.setDefaultAccount(false);
                this.accountRepository.save(a);
            });

            account.setDefaultAccount(true);
        } else {
            account.setDefaultAccount(false);
        }

        if (Boolean.TRUE.equals(data.isLegacy())) {
            account.setAccountOpenTime(LocalDateTime.parse(data.accountOpenTime(), DateTimeFormatter.ISO_DATE_TIME));

            if (StringUtils.isNotEmpty(data.accountCloseTime())) {
                account.setAccountCloseTime(LocalDateTime.parse(data.accountCloseTime(), DateTimeFormatter.ISO_DATE_TIME));
                account.setLastTraded(LocalDateTime.parse(data.accountCloseTime(), DateTimeFormatter.ISO_DATE_TIME));
            }

            account.setLastTraded(null);
        }

        return this.accountRepository.save(account);
    }
}
