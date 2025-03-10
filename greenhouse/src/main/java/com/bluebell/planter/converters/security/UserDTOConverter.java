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
 * @version 0.0.9
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
            return new UserDTO();
        }

        final UserDTO userDTO = new UserDTO();

        userDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        userDTO.setApiToken(entity.getApiToken());
        userDTO.setEmail(entity.getEmail());
        userDTO.setUsername(entity.getUsername());
        userDTO.setFirstName(entity.getFirstName());
        userDTO.setLastName(entity.getLastName());
        userDTO.setDateRegistered(entity.getDateRegistered());
        userDTO.setPhones(this.phoneNumberDTOConverter.convertAll(entity.getPhones()));
        userDTO.setAccounts(this.accountDTOConverter.convertAll(entity.getAccounts()));
        userDTO.setRoles(entity.getRoles().stream().map(UserRole::getLabel).toList());

        return userDTO;
    }
}
