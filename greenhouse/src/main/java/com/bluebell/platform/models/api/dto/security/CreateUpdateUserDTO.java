package com.bluebell.platform.models.api.dto.security;

import com.bluebell.platform.models.core.entities.security.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Request object for creating and updating {@link User}s
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Builder
@Schema(title = "CreateUpdateUserDTO", name = "CreateUpdateUserDTO", description = "Payload for creating and updating users")
public record CreateUpdateUserDTO(
        @Schema(description = "User email") String email,
        @Schema(description = "User given name") String firstName,
        @Schema(description = "User family name") String lastName,
        @Schema(description = "User username") String username,
        @Schema(description = "User password") String password
) { }
