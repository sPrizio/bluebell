package com.bluebell.platform.models.api.dto.security;

import java.time.LocalDateTime;
import java.util.List;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.api.dto.account.AccountDTO;
import com.bluebell.platform.models.api.dto.system.PhoneNumberDTO;
import com.bluebell.platform.models.core.entities.security.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representation of a {@link User}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Setter
@Getter
@Schema(title = "UserDTO", name = "UserDTO", description = "Data representation of a user")
public class UserDTO implements GenericDTO {

    @Schema(description = "User uid")
    private String uid;

    @Schema(description = "User's api token")
    private String apiToken;

    @Schema(description = "User's first name")
    private String firstName;

    @Schema(description = "User's last name")
    private String lastName;

    @Schema(description = "User's username")
    private String username;

    @Schema(description = "User's email")
    private String email;

    @Schema(description = "Date user registered")
    private LocalDateTime dateRegistered;

    @Schema(description = "User's phone numbers")
    private List<PhoneNumberDTO> phones;

    @Schema(description = "User's accounts")
    private List<AccountDTO> accounts;

    @Schema(description = "User's privileges")
    private List<String> roles;
}
