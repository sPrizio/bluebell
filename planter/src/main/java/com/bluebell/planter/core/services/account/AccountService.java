package com.bluebell.planter.core.services.account;

import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.account.AccountType;
import com.bluebell.planter.core.enums.account.Broker;
import com.bluebell.planter.core.enums.account.Currency;
import com.bluebell.planter.core.enums.trade.platform.TradePlatform;
import com.bluebell.planter.core.exceptions.system.EntityCreationException;
import com.bluebell.planter.core.exceptions.system.EntityModificationException;
import com.bluebell.planter.core.exceptions.validation.MissingRequiredDataException;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.entities.security.User;
import com.bluebell.planter.core.models.nonentities.records.account.AccountDetails;
import com.bluebell.planter.core.repositories.account.AccountRepository;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.bluebell.planter.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link Account} entities
 *
 * @author Stephen Prizio
 * @version 0.0.8
 */
@Service
public class AccountService {

    private static final String ACTIVE_KEY = "active";
    private static final String BALANCE_KEY = "balance";

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

        validateParameterIsNotNull(account, CoreConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        return new AccountDetails(
                account,
                this.accountDetailsService.calculateConsistencyScore(account),
                this.accountDetailsService.calculateEquityPoints(account),
                this.accountDetailsService.obtainInsights(account),
                this.accountDetailsService.obtainStatistics(account),
                CoreConstants.RISK_FREE_RATE_CANADA
        );
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
     * @param data {@link Map}
     * @param user {@link User}
     * @return new {@link Account}
     */
    public Account createNewAccount(final Map<String, Object> data, final User user) {

        validateParameterIsNotNull(user, CoreConstants.Validation.Security.User.USER_CANNOT_BE_NULL);

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for creating an Account entity was null or empty");
        }

        try {
            return applyChanges(new Account(), data, user, true);
        } catch (Exception e) {
            throw new EntityCreationException(String.format("An Account could not be created : %s", e.getMessage()), e);
        }
    }

    /**
     * Updates an existing {@link Account}
     *
     * @param account {@link Account} to update
     * @param data {@link Map}
     * @param user {@link User}
     * @return updated {@link Account}
     */
    public Account updateAccount(final Account account, final Map<String, Object> data, final User user) {

        validateParameterIsNotNull(account, CoreConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
        validateParameterIsNotNull(user, CoreConstants.Validation.Security.User.USER_CANNOT_BE_NULL);

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for updating an Account was null or empty");
        }

        try {
            return applyChanges(account, data, user, false);
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

        validateParameterIsNotNull(account, CoreConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        try {
            this.accountRepository.delete(account);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    //  HELPERS

    /**
     * Applies the changes contained within the {@link Map} to the given {@link Account}
     *
     * @param account {@link Account}
     * @param data    {@link Map}
     * @param user    {@link User}
     * @return updated {@link Account}
     */
    private Account applyChanges(Account account, final Map<String, Object> data, final User user, final boolean isNew) {

        final Map<String, Object> acc = (Map<String, Object>) data.get("account");

        if (isNew) {
            account.setAccountOpenTime(LocalDateTime.now());

            if (!Objects.isNull(acc.get(ACTIVE_KEY))) {
                account.setActive(Boolean.parseBoolean(acc.get(ACTIVE_KEY).toString()));
            } else {
                account.setActive(true);
            }

            account.setUser(user);
            account.setBalance(Double.parseDouble(acc.get(BALANCE_KEY).toString()));
            account.setInitialBalance(Double.parseDouble(acc.get(BALANCE_KEY).toString()));
        } else {
            account.setActive(Boolean.parseBoolean(acc.get(ACTIVE_KEY).toString()));
            account.setBalance(Double.parseDouble(acc.get(BALANCE_KEY).toString()));
        }
        
        account.setName(acc.get("name").toString());
        account.setAccountNumber(Long.parseLong(acc.get("number").toString()));
        account.setCurrency(Currency.get(acc.get("currency").toString()));
        account.setAccountType(AccountType.valueOf(acc.get("type").toString()));
        account.setBroker(Broker.valueOf(acc.get("broker").toString()));
        account.setTradePlatform(TradePlatform.getByCode(acc.get("tradePlatform").toString()));

        if (Boolean.parseBoolean(acc.get("isDefault").toString())) {
            user.getAccounts().forEach(a -> {
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
