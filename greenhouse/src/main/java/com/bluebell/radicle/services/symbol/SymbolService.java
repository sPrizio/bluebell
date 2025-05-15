package com.bluebell.radicle.services.symbol;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.trade.Trade;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for symbols and equities
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@Service
public class SymbolService {


    //  METHODS

    /**
     * Obtains a set of equities traded on an account
     *
     * @param account {@link Account}
     * @return {@link Set} of equities traded on the account
     */
    public SortedSet<String> getTradedSymbolsForAccount(final Account account) {
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        if (CollectionUtils.isEmpty(account.getTrades())) {
            return Collections.emptySortedSet();
        }

        return account.getTrades().stream().map(Trade::getProduct).collect(Collectors.toCollection(TreeSet::new));
    }
}
