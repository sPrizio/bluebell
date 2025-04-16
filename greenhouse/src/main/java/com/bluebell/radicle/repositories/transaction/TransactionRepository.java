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

/**
 * Data-access later for {@link Transaction} entities
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long>, CrudRepository<Transaction, Long> {

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
