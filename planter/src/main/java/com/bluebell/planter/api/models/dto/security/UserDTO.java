package com.bluebell.planter.api.models.dto.security;

import com.bluebell.planter.api.models.dto.GenericDTO;
import com.bluebell.planter.api.models.dto.account.AccountDTO;
import com.bluebell.planter.api.models.dto.system.PhoneNumberDTO;
import com.bluebell.planter.core.models.entities.security.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO representation of a {@link User}
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
@Setter
@Getter
public class UserDTO implements GenericDTO {

    private String uid;

    private String apiToken;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private LocalDateTime dateRegistered;

    private List<PhoneNumberDTO> phones;

    private List<AccountDTO> accounts;

    private List<String> roles;
}
