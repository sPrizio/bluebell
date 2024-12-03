package com.bluebell.planter.api.models.dto.transaction;

import com.bluebell.planter.api.models.dto.GenericDTO;
import com.bluebell.planter.api.models.records.EnumDisplay;
import com.bluebell.planter.core.models.entities.transaction.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * A DTO representation for {@link Transaction}
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@Getter
@Setter
public class TransactionDTO implements GenericDTO {

    private String uid;

    private EnumDisplay transactionType;

    private LocalDateTime transactionDate;

    private String name;

    private EnumDisplay transactionStatus;

    private double amount;

    private long accountNumber;

    private String accountName;
}
