package com.bluebell.platform.models.api.dto.trade;


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
public record PaginatedTradesDTO(int page, int pageSize, List<TradeDTO> trades, int totalElements, int totalPages) {
}
