package com.bluebell.radicle.services.account;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.enums.account.AccountType;
import com.bluebell.platform.enums.account.Broker;
import com.bluebell.platform.enums.account.Currency;
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
import com.bluebell.platform.models.core.nonentities.records.account.AccountDetails;
import com.bluebell.platform.services.MathService;
import com.bluebell.radicle.exceptions.system.EntityCreationException;
import com.bluebell.radicle.exceptions.system.EntityModificationException;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.repositories.account.AccountRepository;
import com.bluebell.radicle.services.trade.TradeService;
import com.bluebell.radicle.services.transaction.TransactionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;


/**
 * Service-layer for {@link Account} entities
 *
 * @author Stephen Prizio
 * @version 0.1.7
 */
@Slf4j
@Service
public class AccountService {

    private final MathService mathService = new MathService();

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "accountDetailsService")
    private AccountDetailsService accountDetailsService;

    @Resource(name = "tradeService")
    private TradeService tradeService;

    @Resource(name = "transactionService")
    private TransactionService transactionService;


    //  METHODS

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
     * Attempts to delete the given {@link Account}
     *
     * @param account {@link Account}
     * @return true if successfully deleted
     */
    public boolean deleteAccount(final Account account) {
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        try {
            this.accountRepository.delete(account);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
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

        if (!Objects.isNull(data.isDefault())) {
            portfolio.getAccounts().forEach(a -> {
                a.setDefaultAccount(false);
                this.accountRepository.save(a);
            });

            account.setDefaultAccount(true);
        } else {
            account.setDefaultAccount(false);
        }

        return this.accountRepository.save(account);
    }
}
