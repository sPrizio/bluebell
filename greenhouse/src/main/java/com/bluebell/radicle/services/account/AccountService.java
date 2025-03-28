package com.bluebell.radicle.services.account;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.account.AccountType;
import com.bluebell.platform.enums.account.Broker;
import com.bluebell.platform.enums.account.Currency;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.models.api.dto.account.CreateUpdateAccountDTO;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.nonentities.records.account.AccountDetails;
import com.bluebell.radicle.exceptions.system.EntityCreationException;
import com.bluebell.radicle.exceptions.system.EntityModificationException;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.repositories.account.AccountRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;


/**
 * Service-layer for {@link Account} entities
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Service
public class AccountService {

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "accountDetailsService")
    private AccountDetailsService accountDetailsService;


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
