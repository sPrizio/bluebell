package com.bluebell.planter.converters.security;

import com.bluebell.planter.converters.GenericDTOConverter;
import com.bluebell.planter.converters.account.AccountDTOConverter;
import com.bluebell.planter.converters.system.PhoneNumberDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.enums.security.UserRole;
import com.bluebell.platform.models.api.dto.security.UserDTO;
import com.bluebell.platform.models.core.entities.security.User;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;


/**
 * Converts {@link User}s into {@link UserDTO}s
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Component("userDTOConverter")
public class UserDTOConverter implements GenericDTOConverter<User, UserDTO> {

    @Resource(name = "accountDTOConverter")
    private AccountDTOConverter accountDTOConverter;

    @Resource(name = "phoneNumberDTOConverter")
    private PhoneNumberDTOConverter phoneNumberDTOConverter;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public UserDTO convert(final User entity) {

        if (entity == null) {
            return UserDTO.builder().build();
        }

        return UserDTO
                .builder()
                .uid(this.uniqueIdentifierService.generateUid(entity))
                .apiToken(entity.getApiToken())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .dateRegistered(entity.getDateRegistered())
                .phones(this.phoneNumberDTOConverter.convertAll(entity.getPhones()))
                .accounts(this.accountDTOConverter.convertAll(entity.getAccounts()))
                .roles(entity.getRoles().stream().map(UserRole::getLabel).toList())
                .build();
    }
}
