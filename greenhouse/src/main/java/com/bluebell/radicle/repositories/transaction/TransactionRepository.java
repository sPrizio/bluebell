package com.bluebell.radicle.repositories.transaction;

import com.bluebell.platform.models.core.entities.transaction.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access later for {@link Transaction} entities
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long>, CrudRepository<Transaction, Long> {
}
