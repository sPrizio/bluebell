package com.bluebell.platform.models.api.dto.system;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.core.entities.system.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * A DTO representation of a {@link PhoneNumber}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Setter
@Getter
@Builder
@Schema(title = "PhoneNumberDTO", name = "PhoneNumberDTO", description = "Data representation of a user's phone number")
public class PhoneNumberDTO implements GenericDTO {

    @Schema(description = "Phone number uid")
    private @Builder.Default String uid = StringUtils.EMPTY;

    @Schema(description = "Type of phone for this phone number")
    private String phoneType;

    @Schema(description = "Country code (US : +1)")
    private short countryCode;

    @Schema(description = "Telephone number")
    private long telephoneNumber;

    @Schema(description = "String / Number to display")
    private String display;
}
