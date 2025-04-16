package com.bluebell.platform.models.api.dto.trade;

import com.bluebell.platform.models.core.entities.trade.Trade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

/**
 * Request object for creating and updating a {@link List} of {@link Trade}s
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Builder
@Schema(title = "CreateUpdateMultipleTradesDTO", name = "CreateUpdateMultipleTradesDTO", description = "Payload for creating and updating trades")
public record CreateUpdateMultipleTradesDTO(
        String userIdentifier,
        long accountNumber,
        List<CreateUpdateTradeDTO> trades
) { }
