package com.bluebell.radicle.repositories.account;


import com.bluebell.platform.models.core.entities.account.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link Account} entities
 *
 * @author Stephen Prizio
 * @version 0.0.9
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
}
