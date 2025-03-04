package com.bluebell.platform.models.api.dto.trade;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

/**
 * Class representation of a collection of {@link TradeDTO}s with associated page information
 *
 * @param page          current page
 * @param pageSize      page size
 * @param trades        {@link List} of {@link TradeDTO}
 * @param totalElements total trades count
 * @param totalPages    number of pages
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Schema(title = "PaginatedTradesDTO", name = "PaginatedTradesDTO", description = "Data representation of a collection of trades organized by page")
public record PaginatedTradesDTO(
        @Getter @Schema(description = "Current page") int page,
        @Getter @Schema(description = "Page size") int pageSize,
        @Getter @Schema(description = "List of trades in the page") List<TradeDTO> trades,
        @Getter @Schema(description = "Total elements across all pages") int totalElements,
        @Getter @Schema(description = "Total pages") int totalPages
) { }
