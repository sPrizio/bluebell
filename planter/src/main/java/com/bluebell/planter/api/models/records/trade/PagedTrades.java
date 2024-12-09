package com.bluebell.planter.api.models.records.trade;

import com.bluebell.planter.api.models.dto.trade.TradeDTO;

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
 * @version 0.0.7
 */
public record PagedTrades(int page, int pageSize, List<TradeDTO> trades, int totalElements, int totalPages) {
}
