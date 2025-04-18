package com.bluebell.platform.models.api.dto.account;

import com.bluebell.platform.models.api.dto.trade.CreateUpdateTradeDTO;
import com.bluebell.platform.models.api.dto.transaction.CreateUpdateTransactionDTO;
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
@Schema(title = "CreateUpdateAccountTradingDataDTO", name = "CreateUpdateAccountTradingDataDTO", description = "Payload for creating and updating account trading data")
public record CreateUpdateAccountTradingDataDTO(
        @Schema(description = "User identifier") String userIdentifier,
        @Schema(description = "Account number") long accountNumber,
        @Schema(description = "List of trades to insert/update") List<CreateUpdateTradeDTO> trades,
        @Schema(description = "List of transactions to insert/update") List<CreateUpdateTransactionDTO> transactions
) { }
