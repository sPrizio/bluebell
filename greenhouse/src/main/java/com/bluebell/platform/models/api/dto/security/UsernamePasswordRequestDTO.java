package com.bluebell.platform.models.api.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Class representation of a username/password request
 *
 * @param usernameEmail username/email string
 * @param password password
 * @author Stephen Prizio
 * @version 0.2.6
 */
@Builder
@Schema(title = "UsernamePasswordRequestDTO", name = "UsernamePasswordRequestDTO", description = "Payload for logging in to bluebell")
public record UsernamePasswordRequestDTO(
        @Schema(description = "Username/email string") String usernameEmail,
        @Schema(description = "Password") String password
) { }
