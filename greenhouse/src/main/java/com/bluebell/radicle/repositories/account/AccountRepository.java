package com.bluebell.radicle.repositories.account;


import com.bluebell.platform.models.core.entities.account.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data-access layer for {@link Account} entities
 *
 * @author Stephen Prizio
 * @version 0.1.8
 */
@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long>, CrudRepository<Account, Long> {

    /**
     * Obtains an {@link Account} for the given account number
     *
     * @param accountNumber account number
     * @return {@link Account}
     */
    Account findAccountByAccountNumber(final long accountNumber);

    /**
     * Returns a {@link List} of {@link Account}s who's last traded is greater than the given time span
     *
     * @param dateTime {@link LocalDateTime}
     * @return {@link List} of {@link Account}
     */
    @Query("SELECT a FROM Account a WHERE a.lastTraded IS NOT NULL AND a.lastTraded < ?1")
    List<Account> findAccountsWhereLastTimeTradedIsBeyondLookback(final LocalDateTime dateTime);
}
