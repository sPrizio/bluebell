package com.bluebell.platform.models.api.dto.account;

import com.bluebell.platform.models.core.entities.account.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Request object for creating and updating {@link Account}s
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Builder
@Schema(title = "CreateUpdateAccountDTO", name = "CreateUpdateAccountDTO", description = "Payload for creating and updating accounts")
public record CreateUpdateAccountDTO(
        @Schema(description = "Is the account active") Boolean active,
        @Schema(description = "Account balance") double balance,
        @Schema(description = "Account name") String name,
        @Schema(description = "Account number") Long number,
        @Schema(description = "Account currency") String currency,
        @Schema(description = "Account type") String type,
        @Schema(description = "Account broker") String broker,
        @Schema(description = "Account trade platform") String tradePlatform,
        @Schema(description = "Is this the user's default account") Boolean isDefault
) { }
