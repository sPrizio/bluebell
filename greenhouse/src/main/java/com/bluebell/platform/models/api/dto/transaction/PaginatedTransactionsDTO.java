package com.bluebell.platform.models.api.dto.transaction;

import com.bluebell.platform.models.core.entities.transaction.Transaction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Class representation of a collection of {@link Transaction}s with associated page information
 *
 * @param page          current page
 * @param pageSize      page size
 * @param transactions  {@link List} of {@link TransactionDTO}
 * @param totalElements total transactions count
 * @param totalPages    number of pages
 * @author Stephen Prizio
 * @version 0.2.5
 */
@Builder
@Schema(title = "PaginatedTransactionsDTO", name = "PaginatedTransactionsDTO", description = "Data representation of a collection of transactions organized into pages")
public record PaginatedTransactionsDTO(
        @Getter @Schema(description = "Current page") int page,
        @Getter @Schema(description = "Page size") int pageSize,
        @Getter @Schema(description = "List of transactions in the page") List<TransactionDTO> transactions,
        @Getter @Schema(description = "Total elements across all pages") int totalElements,
        @Getter @Schema(description = "Total pages") int totalPages
) {
}
