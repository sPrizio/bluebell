package com.bluebell.radicle.services.transaction;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.enums.transaction.TransactionStatus;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.api.dto.transaction.CreateUpdateTransactionDTO;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.radicle.exceptions.system.EntityCreationException;
import com.bluebell.radicle.exceptions.system.EntityModificationException;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.repositories.transaction.TransactionRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link Transaction}
 *
 * @author Stephen Prizio
 * @version 0.1.7
 */
@Slf4j
@Service
public class TransactionService {

    @Resource(name = "transactionRepository")
    private TransactionRepository transactionRepository;


    //  METHODS

    /**
     * Creates a new {@link Transaction} with the given data
     *
     * @param data    {@link CreateUpdateTransactionDTO}
     * @param account {@link Account}
     * @return new {@link Transaction}
     */
    public Transaction createNewTransaction(final CreateUpdateTransactionDTO data, final Account account) {
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        if (data == null || StringUtils.isEmpty(data.transactionDate())) {
            throw new MissingRequiredDataException("The required data for creating a Transaction entity was null or empty");
        }

        try {
            return applyChanges(Transaction.builder().build(), data, account);
        } catch (Exception e) {
            throw new EntityCreationException(String.format("A Transaction could not be created : %s", e.getMessage()), e);
        }
    }

    /**
     * Updates an existing {@link Transaction}
     *
     * @param transaction {@link Transaction} to update
     * @param data        {@link CreateUpdateTransactionDTO}
     * @param account     {@link Account}
     * @return updated {@link Transaction}
     */
    public Transaction updateTransaction(final Transaction transaction, final CreateUpdateTransactionDTO data, final Account account) {
        validateParameterIsNotNull(transaction, CorePlatformConstants.Validation.Transaction.TRANSACTION_CANNOT_BE_NULL);
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        if (data == null || StringUtils.isEmpty(data.transactionDate())) {
            throw new MissingRequiredDataException("The required data for updating a Transaction entity was null or empty");
        }

        try {
            return applyChanges(transaction, data, account);
        } catch (Exception e) {
            throw new EntityModificationException(String.format("An error occurred while modifying the Transaction : %s", e.getMessage()), e);
        }
    }

    /**
     * Attempts to delete the given {@link Transaction}
     *
     * @param transaction {@link Transaction}
     * @return true if successfully deleted
     */
    public boolean deleteTransaction(final Transaction transaction) {
        validateParameterIsNotNull(transaction, CorePlatformConstants.Validation.Transaction.TRANSACTION_CANNOT_BE_NULL);

        try {
            this.transactionRepository.delete(transaction);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * Saves all {@link Transaction}s within the given list to the database
     *
     * @param transactions {@link List} of {@link Transaction}s
     * @param account      {@link Account}
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


    //  HELPERS

    /**
     * Applies the changes to the given {@link Transaction}
     *
     * @param transaction {@link Transaction}
     * @param data        {@link CreateUpdateTransactionDTO}
     * @param account     {@link Account}
     * @return {@link Transaction}
     */
    private Transaction applyChanges(Transaction transaction, final CreateUpdateTransactionDTO data, final Account account) {

        transaction.setTransactionType(GenericEnum.getByCode(TransactionType.class, data.transactionType().toUpperCase()));
        transaction.setTransactionDate(LocalDateTime.parse(data.transactionDate(), DateTimeFormatter.ofPattern(CorePlatformConstants.DATE_TIME_NO_TIMEZONE)));
        transaction.setName(data.name());
        transaction.setTransactionStatus(GenericEnum.getByCode(TransactionStatus.class, data.transactionStatus().toUpperCase()));
        transaction.setAmount(data.amount());
        transaction.setAccount(account);


        return this.transactionRepository.save(transaction);
    }
}
