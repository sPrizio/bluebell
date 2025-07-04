package com.bluebell.planter.converters.security;

import com.bluebell.planter.converters.GenericDTOConverter;
import com.bluebell.planter.converters.portfolio.PortfolioDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.enums.security.UserRole;
import com.bluebell.platform.models.api.dto.security.UserDTO;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;


/**
 * Converts {@link User}s into {@link UserDTO}s
 *
 * @author Stephen Prizio
 * @version 0.2.6
 */
@Component("userDTOConverter")
public class UserDTOConverter implements GenericDTOConverter<User, UserDTO> {

    @Resource(name = "portfolioDTOConverter")
    private PortfolioDTOConverter portfolioDTOConverter;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public UserDTO convert(final User entity) {

        if (entity == null) {
            return UserDTO.builder().build();
        }

        final String uid = this.uniqueIdentifierService.generateUid(entity);
        return UserDTO
                .builder()
                .uid(uid)
                .userIdentifier(this.uniqueIdentifierService.generateUniqueIdentifierAsLong(uid))
                .apiToken(entity.getApiToken())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .dateRegistered(entity.getDateRegistered())
                .portfolios(this.portfolioDTOConverter.convertAll(entity.getPortfolios()))
                .roles(entity.getRoles().stream().map(role -> EnumDisplay.builder().code(role.getCode()).label(role.getLabel()).build()).toList())
                .build();
    }
}
