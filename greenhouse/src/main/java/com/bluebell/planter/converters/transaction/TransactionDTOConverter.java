package com.bluebell.planter.converters.transaction;

import com.bluebell.planter.converters.GenericDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.transaction.TransactionDTO;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Converts {@link Transaction}s into {@link TransactionDTO}s
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Component("transactionDTOConverter")
public class TransactionDTOConverter implements GenericDTOConverter<Transaction, TransactionDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public TransactionDTO convert(final Transaction entity) {

        if (entity == null) {
            return TransactionDTO.builder().build();
        }

        return TransactionDTO
                .builder()
                .uid(this.uniqueIdentifierService.generateUid(entity))
                .transactionType(new EnumDisplay(entity.getTransactionType().getCode(), entity.getTransactionType().getLabel()))
                .transactionDate(entity.getTransactionDate())
                .amount(entity.getAmount())
                .transactionStatus(new EnumDisplay(entity.getTransactionStatus().getCode(), entity.getTransactionStatus().getLabel()))
                .name(entity.getName())
                .accountNumber(entity.getAccount().getAccountNumber())
                .accountName(entity.getAccount().getName())
                .build();
    }
}
