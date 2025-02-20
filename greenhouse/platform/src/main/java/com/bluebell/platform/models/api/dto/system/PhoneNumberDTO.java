package com.bluebell.platform.models.api.dto.system;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.core.entities.system.PhoneNumber;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representation of a {@link PhoneNumber}
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Setter
@Getter
public class PhoneNumberDTO implements GenericDTO {

    private String uid;

    private String phoneType;

    private short countryCode;

    private long telephoneNumber;

    private String display;
}
