package com.bluebell.radicle.repositories.transaction;

import com.bluebell.platform.enums.transaction.TransactionStatus;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * @version 0.2.5
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
    @Query("SELECT tr FROM Transaction tr WHERE tr.transactionDate >= ?1 AND tr.transactionDate < ?2 AND tr.account = ?3  ORDER BY tr.transactionDate ASC")
    List<Transaction> findAllTransactionsWithinDate(final LocalDateTime start, final LocalDateTime end, final Account account);

    /**
     * Returns a {@link Transaction} for the given transaction number and {@link Account}
     *
     * @param transactionNumber transaction number
     * @param account           {@link Account}
     * @return {@link Transaction}
     */
    Transaction findTransactionByTransactionNumberAndAccount(long transactionNumber, Account account);

    /**
     * Returns a paginated {@link List} of {@link Transaction}s that are within the given time span
     *
     * @param start    {@link LocalDateTime} start of interval (inclusive)
     * @param end      {@link LocalDateTime} end of interval (exclusive)
     * @param account  {@link Account}
     * @param pageable {@link Pageable}
     * @return {@link Page} of {@link Transaction}s
     */
    @Query("SELECT tr FROM Transaction tr WHERE tr.transactionDate >= ?1 AND tr.transactionDate < ?2 AND tr.account = ?3")
    Page<Transaction> findAllTransactionsWithinDatePaged(final LocalDateTime start, final LocalDateTime end, final Account account, final Pageable pageable);

    /**
     * Returns a paginated {@link List} of {@link Transaction}s that are within the given time span for the given status
     *
     * @param start             {@link LocalDateTime} start of interval (inclusive)
     * @param end               {@link LocalDateTime} end of interval (exclusive)
     * @param account           {@link Account}
     * @param transactionStatus {@link TransactionStatus}
     * @param pageable          {@link Pageable}
     * @return {@link Page} of {@link Transaction}s
     */
    @Query("SELECT tr FROM Transaction tr WHERE tr.transactionDate >= ?1 AND tr.transactionDate < ?2 AND tr.account = ?3 AND tr.transactionStatus = ?4")
    Page<Transaction> findAllTransactionsForStatusWithinDatePaged(final LocalDateTime start, final LocalDateTime end, final Account account, final TransactionStatus transactionStatus, final Pageable pageable);

    /**
     * Returns a paginated {@link List} of {@link Transaction}s that are within the given time span for the given transaction type
     *
     * @param start           {@link LocalDateTime} start of interval (inclusive)
     * @param end             {@link LocalDateTime} end of interval (exclusive)
     * @param account         {@link Account}
     * @param transactionType {@link TransactionType}
     * @param pageable        {@link Pageable}
     * @return {@link Page} of {@link Transaction}s
     */
    @Query("SELECT tr FROM Transaction tr WHERE tr.transactionDate >= ?1 AND tr.transactionDate < ?2 AND tr.account = ?3 AND tr.transactionType = ?4")
    Page<Transaction> findAllTransactionsForTypeWithinDatePaged(final LocalDateTime start, final LocalDateTime end, final Account account, final TransactionType transactionType, final Pageable pageable);

    /**
     * Returns a paginated {@link List} of {@link Transaction}s filtered by their date, status, type and account and sorted
     *
     * @param start             {@link LocalDateTime} start of interval (inclusive)
     * @param end               {@link LocalDateTime} end of interval (exclusive)
     * @param transactionType   {@link TransactionType}
     * @param transactionStatus {@link TransactionStatus}
     * @param account           {@link Account}
     * @param pageable          {@link Pageable}
     * @return {@link Page} of {@link Transaction}s
     */
    @Query("SELECT tr FROM Transaction tr WHERE tr.transactionDate >= ?1 AND tr.transactionDate < ?2 AND tr.transactionType = ?3 AND tr.transactionStatus = ?4 AND tr.account = ?5")
    Page<Transaction> findAllTransactionsForTypeAndStatusWithinTimespanPaged(final LocalDateTime start, final LocalDateTime end, final TransactionType transactionType, final TransactionStatus transactionStatus, final Account account, final Pageable pageable);

    /**
     * Inserts or updates an existing {@link Transaction}
     *
     * @param transactionNumber transaction number
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
                    transaction_number,
                    transaction_type,
                    transaction_date,
                    transaction_name,
                    transaction_status,
                    transaction_amount,
                    account_id
                ) VALUES (
                    :transactionNumber,
                    :transactionType,
                    :transactionDate,
                    :transactionName,
                    :transactionStatus,
                    :transactionAmount,
                    :accountId
                )
                ON DUPLICATE KEY UPDATE
                    transaction_number = :transactionNumber,
                    transaction_type = :transactionType,
                    transaction_date = :transactionDate,
                    transaction_name = :transactionName,
                    transaction_status = :transactionStatus,
                    transaction_amount = :transactionAmount,
                    account_id = :accountId
            """, nativeQuery = true)
    int upsertTransaction(
            @Param("transactionNumber") long transactionNumber,
            @Param("transactionType") TransactionType transactionType,
            @Param("transactionDate") LocalDateTime transactionDate,
            @Param("transactionName") String name,
            @Param("transactionStatus") TransactionStatus transactionStatus,
            @Param("transactionAmount") double amount,
            @Param("accountId") Long accountId
    );
}
