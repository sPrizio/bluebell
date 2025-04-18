package com.bluebell.radicle.services.transaction;

import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.radicle.repositories.transaction.TransactionRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service-layer for {@link Transaction}
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Slf4j
@Service
public class TransactionService {

    @Resource(name = "transactionRepository")
    private TransactionRepository transactionRepository;


    //  METHODS

    /**
     * Saves all {@link Transaction}s within the given list to the database
     *
     * @param transactions {@link List} of {@link Transaction}s
     * @param account {@link Account}
     * @return count of insertions/updates
     */
    public int saveAll(final List<Transaction> transactions, final Account account) {

        if (CollectionUtils.isEmpty(transactions) || account == null) {
            return -1;
        }

        int count = 0;
        for (final Transaction transaction : transactions) {
            count += this.transactionRepository.upsertTransaction(
                    transaction.getTransactionType(),
                    transaction.getTransactionDate(),
                    transaction.getName(),
                    transaction.getTransactionStatus(),
                    transaction.getAmount(),
                    account.getId()
            );
        }

        return count;
    }
}
