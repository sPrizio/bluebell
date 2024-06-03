package com.bluebell.flowerpot.core.services.account;

import com.bluebell.flowerpot.core.constants.CoreConstants;
import com.bluebell.flowerpot.core.enums.account.AccountType;
import com.bluebell.flowerpot.core.enums.account.Broker;
import com.bluebell.flowerpot.core.enums.account.Currency;
import com.bluebell.flowerpot.core.enums.trade.platform.TradePlatform;
import com.bluebell.flowerpot.core.exceptions.system.EntityCreationException;
import com.bluebell.flowerpot.core.exceptions.validation.MissingRequiredDataException;
import com.bluebell.flowerpot.core.models.entities.account.Account;
import com.bluebell.flowerpot.core.models.entities.security.User;
import com.bluebell.flowerpot.core.repositories.account.AccountRepository;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static com.bluebell.flowerpot.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link Account} entities
 *
 * @author Stephen Prizio
 * @version 0.0.3
 */
@Service
public class AccountService {

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;


    //  METHODS

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
            return applyChanges(new Account(), data, user);
        } catch (Exception e) {
            throw new EntityCreationException(String.format("An Account could not be created : %s", e.getMessage()), e);
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
    private Account applyChanges(Account account, final Map<String, Object> data, final User user) {

        final Map<String, Object> acc = (Map<String, Object>) data.get("account");
        final boolean isDefault = user.getAccounts().isEmpty();

        account.setAccountOpenTime(LocalDateTime.now());
        account.setActive(true);
        account.setBalance(Double.parseDouble(acc.get("balance").toString()));
        account.setUser(user);
        account.setName(acc.get("name").toString());
        account.setAccountNumber(Long.parseLong(acc.get("number").toString()));
        account.setCurrency(Currency.get(acc.get("currency").toString()));
        account.setAccountType(AccountType.valueOf(acc.get("type").toString()));
        account.setBroker(Broker.valueOf(acc.get("broker").toString()));
        account.setDefaultAccount(isDefault);
        account.setTradePlatform(TradePlatform.valueOf(acc.get("tradePlatform").toString()));

        return this.accountRepository.save(account);
    }
}
