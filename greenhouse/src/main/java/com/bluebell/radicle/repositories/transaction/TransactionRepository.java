package com.bluebell.radicle.repositories.transaction;

import com.bluebell.platform.enums.transaction.TransactionStatus;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data-access later for {@link Transaction} entities
 *
 * @author Stephen Prizio
 * @version 0.1.7
 */
@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long>, CrudRepository<Transaction, Long> {

    /**
     * Returns a {@link List} of {@link Transaction}s for the given {@link Account}
     *
     * @param account {@link Account}
     * @return {@link List} of {@link Transaction}
     */
    List<Transaction> findAllByAccount(final Account account);

    /**
     * Returns a {@link List} of {@link Transaction}s for the given {@link Account} and {@link TransactionType}
     *
     * @param transactionType {@link TransactionType}
     * @param account         {@link Account}
     * @return {@link List} of {@link Transaction}
     */
    List<Transaction> findAllByTransactionTypeAndAccount(final TransactionType transactionType, final Account account);

    /**
     * Returns a {@link List} of {@link Transaction}s for the given {@link Account} and {@link TransactionStatus}
     *
     * @param transactionStatus {@link TransactionStatus}
     * @param account           {@link Account}
     * @return {@link List} of {@link Transaction}
     */
    List<Transaction> findAllByTransactionStatusAndAccount(final TransactionStatus transactionStatus, final Account account);

    /**
     * Returns a {@link List} of {@link Transaction}s that are within the given timespan for the given {@link Account}
     *
     * @param start   {@link LocalDateTime} start of interval (inclusive)
     * @param end     {@link LocalDateTime} end of interval (exclusive)
     * @param account {@link Account}
     * @return {@link List} of {@link Transaction}
     */
    @Query("SELECT tr from Transaction tr WHERE tr.transactionDate >= ?1 AND tr.transactionDate < ?2 AND tr.account = ?3  ORDER BY tr.transactionDate ASC")
    List<Transaction> findAllTransactionsWithinDate(final LocalDateTime start, final LocalDateTime end, final Account account);

    /**
     * Looks up a {@link Transaction} for the given {@link Account} and transaction name
     *
     * @param name    transaction name
     * @param account {@link Account}
     * @return {@link Transaction}
     */
    Transaction findTransactionByNameAndAccount(final String name, final Account account);

    /**
     * Inserts or updates an existing {@link Transaction}
     *
     * @param transactionType   {@link TransactionType}
     * @param transactionDate   date of the transaction
     * @param name              transaction name
     * @param transactionStatus {@link TransactionStatus}
     * @param amount            transaction amount
     * @param accountId         associated {@link Account} ID
     * @return number of entries inserted/updated
     */
    @Modifying
    @Transactional
    @Query(value = """
                INSERT INTO transactions (
                    transaction_type,
                    transaction_date,
                    transaction_name,
                    transaction_status,
                    transaction_amount,
                    account_id
                ) VALUES (
                    :transactionType,
                    :transactionDate,
                    :transactionName,
                    :transactionStatus,
                    :transactionAmount,
                    :accountId
                )
                ON DUPLICATE KEY UPDATE
                    transaction_type = :transactionType,
                    transaction_date = :transactionDate,
                    transaction_name = :transactionName,
                    transaction_status = :transactionStatus,
                    transaction_amount = :transactionAmount,
                    account_id = :accountId
            """, nativeQuery = true)
    int upsertTransaction(
            @Param("transactionType") TransactionType transactionType,
            @Param("transactionDate") LocalDateTime transactionDate,
            @Param("transactionName") String name,
            @Param("transactionStatus") TransactionStatus transactionStatus,
            @Param("transactionAmount") double amount,
            @Param("accountId") Long accountId
    );
}
