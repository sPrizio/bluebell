package com.bluebell.platform.models.api.dto.transaction;

import java.time.LocalDateTime;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representation for {@link Transaction}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
@Setter
@Schema(title = "TransactionDTO", name = "TransactionDTO", description = "Data representation of an account transaction")
public class TransactionDTO implements GenericDTO {

    @Schema(description = "Transaction uid")
    private String uid;

    @Schema(description = "Type of transaction")
    private EnumDisplay transactionType;

    @Schema(description = "Date & time of transaction")
    private LocalDateTime transactionDate;

    @Schema(description = "Transaction name")
    private String name;

    @Schema(description = "Status of the transaction")
    private EnumDisplay transactionStatus;

    @Schema(description = "Transaction amount")
    private double amount;

    @Schema(description = "Account number")
    private long accountNumber;

    @Schema(description = "Account name")
    private String accountName;
}
