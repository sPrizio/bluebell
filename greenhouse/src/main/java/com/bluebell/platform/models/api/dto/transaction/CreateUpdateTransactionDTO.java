package com.bluebell.platform.models.api.dto.transaction;

import com.bluebell.platform.models.core.entities.transaction.Transaction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Request object for creating and updating {@link Transaction}s
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Builder
@Schema(title = "CreateUpdateTransactionDTO", name = "CreateUpdateTransactionDTO", description = "Payload for creating and updating transactions")
public record CreateUpdateTransactionDTO(
        @Schema(description = "Transaction type") String transactionType,
        @Schema(description = "Transaction date") String transactionDate,
        @Schema(description = "Transaction name") String name,
        @Schema(description = "Transaction status") String transactionStatus,
        @Schema(description = "Transaction amount") double amount
) { }
