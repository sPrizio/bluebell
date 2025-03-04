package com.bluebell.platform.models.api.dto.transaction;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * A DTO representation for {@link Transaction}
 *
 * @author Stephen Prizio
 * @version 0.0.9
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
