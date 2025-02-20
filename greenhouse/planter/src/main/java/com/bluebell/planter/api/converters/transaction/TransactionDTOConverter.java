package com.bluebell.planter.api.converters.transaction;

import com.bluebell.planter.api.converters.GenericDTOConverter;
import com.bluebell.planter.core.services.platform.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.transaction.TransactionDTO;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Converts {@link Transaction}s into {@link TransactionDTO}s
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Component("transactionDTOConverter")
public class TransactionDTOConverter implements GenericDTOConverter<Transaction, TransactionDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public TransactionDTO convert(final Transaction entity) {

        if (entity == null) {
            return new TransactionDTO();
        }

        TransactionDTO transactionDTO = new TransactionDTO();

        transactionDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        transactionDTO.setTransactionType(new EnumDisplay(entity.getTransactionType().getCode(), entity.getTransactionType().getLabel()));
        transactionDTO.setTransactionDate(entity.getTransactionDate());
        transactionDTO.setAmount(entity.getAmount());
        transactionDTO.setTransactionStatus(new EnumDisplay(entity.getTransactionStatus().getCode(), entity.getTransactionStatus().getLabel()));
        transactionDTO.setName(entity.getName());
        transactionDTO.setAccountNumber(entity.getAccount().getAccountNumber());
        transactionDTO.setAccountName(entity.getAccount().getName());

        return transactionDTO;
    }
}
