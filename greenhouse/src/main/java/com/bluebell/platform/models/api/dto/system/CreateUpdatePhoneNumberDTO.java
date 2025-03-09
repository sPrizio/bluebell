package com.bluebell.platform.models.api.dto.system;

import com.bluebell.platform.models.core.entities.system.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Request object for creating and updating {@link PhoneNumber}s
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Builder
@Schema(title = "CreateUpdatePhoneNumberDTO", name = "CreateUpdatePhoneNumberDTO", description = "Payload for creating and updating phone numbers")
public record CreateUpdatePhoneNumberDTO(
        @Schema(description = "The type of phone") String phoneType,
        @Schema(description = "The country code of the phone number") Short countryCode,
        @Schema(description = "The telephone number") Long telephoneNumber
) { }
