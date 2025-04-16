package com.bluebell.platform.models.api.dto.security;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.api.dto.portfolio.PortfolioDTO;
import com.bluebell.platform.models.api.dto.system.PhoneNumberDTO;
import com.bluebell.platform.models.core.entities.security.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * A DTO representation of a {@link User}
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Setter
@Getter
@Builder
@Schema(title = "UserDTO", name = "UserDTO", description = "Data representation of a user")
public class UserDTO implements GenericDTO {

    @Schema(description = "User uid")
    private @Builder.Default String uid = StringUtils.EMPTY;

    @Schema(description = "Unique user number")
    private long userIdentifier;

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

    @Schema(description = "User's portfolios")
    private List<PortfolioDTO> portfolios;

    @Schema(description = "User's privileges")
    private List<String> roles;


    //  METHODS

    /**
     * Returns a list of portfolios that are marked as active
     *
     * @return {@link List} of {@link PortfolioDTO}
     */
    public List<PortfolioDTO> getActivePortfolios() {
        if (CollectionUtils.isEmpty(this.portfolios)) {
            return Collections.emptyList();
        }

        return this.portfolios.stream().filter(PortfolioDTO::isActive).toList();
    }
}
