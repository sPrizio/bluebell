package com.bluebell.planter.api.models.dto.system;

import com.bluebell.planter.api.models.dto.GenericDTO;
import com.bluebell.planter.core.models.entities.system.PhoneNumber;
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
