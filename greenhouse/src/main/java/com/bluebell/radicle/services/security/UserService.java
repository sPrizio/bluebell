package com.bluebell.radicle.services.security;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.security.UserRole;
import com.bluebell.platform.models.api.dto.security.CreateUpdateUserDTO;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.radicle.exceptions.security.DuplicateUserEmailException;
import com.bluebell.radicle.exceptions.security.DuplicateUserUsernameException;
import com.bluebell.radicle.exceptions.system.EntityCreationException;
import com.bluebell.radicle.exceptions.system.EntityModificationException;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.repositories.security.UserRepository;
import com.bluebell.radicle.security.services.ApiTokenService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;


/**
 * Service-layer for {@link User} entities
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Service
public class UserService {

    @Resource(name = "apiTokenService")
    private ApiTokenService apiTokenService;

    @Resource(name = "userRepository")
    private UserRepository userRepository;


    //  METHODS

    /**
     * Finds a {@link User} by their username
     *
     * @param username username
     * @return {@link Optional} {@link User}
     */
    public Optional<User> findUserByUsername(final String username) {
        validateParameterIsNotNull(username, CorePlatformConstants.Validation.Security.User.USERNAME_CANNOT_BE_NULL);
        return Optional.ofNullable(this.userRepository.findUserByUsername(username));
    }

    /**
     * Finds a {@link User} by their email
     *
     * @param email email
     * @return {@link Optional} {@link User}
     */
    public Optional<User> findUserByEmail(final String email) {
        validateParameterIsNotNull(email, CorePlatformConstants.Validation.Security.User.EMAIL_CANNOT_BE_NULL);
        return Optional.ofNullable(this.userRepository.findUserByEmail(email));
    }

    /**
     * Creates a new {@link User} from the given {@link Map} of data
     *
     * @param data {@link CreateUpdateUserDTO}
     * @return newly created {@link User}
     */
    public User createUser(final CreateUpdateUserDTO data) {

        String email = "";
        String username;

        if (data == null || StringUtils.isEmpty(data.email())) {
            throw new MissingRequiredDataException("The required data for creating a User was null or empty");
        }

        try {
            email = data.email();
            username = data.username();

            if (this.userRepository.findUserByEmail(email) != null) {
                throw new DuplicateUserEmailException(String.format("A user with the email %s already exists. Please try another email.", email));
            }

            if (this.userRepository.findUserByUsername(username) != null) {
                throw new DuplicateUserUsernameException(String.format("A user with the username %s already exists. Please try another username.", username));
            }

            return applyChanges(User.builder().build(), data, true);
        } catch (Exception e) {
            this.userRepository.deleteUserByEmail(email);
            throw new EntityCreationException(String.format("A User could not be created : %s", e.getMessage()), e);
        }
    }

    /**
     * Updates an existing {@link User} with the given {@link Map} of data. Update methods are designed to be idempotent.
     *
     * @param user {@link User}
     * @param data {@link CreateUpdateUserDTO}
     * @return modified {@link User}
     */
    public User updateUser(final User user, final CreateUpdateUserDTO data) {

        validateParameterIsNotNull(user, CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);

        if (data == null || StringUtils.isEmpty(data.email())) {
            throw new MissingRequiredDataException("The required data for updating a User was null or empty");
        }

        try {
            return applyChanges(user, data, false);
        } catch (Exception e) {
            throw new EntityModificationException(String.format("An error occurred while modifying the User : %s", e.getMessage()), e);
        }
    }


    //  HELPERS

    /**
     * Applies changes to the given {@link User} with the given data
     *
     * @param user {@link User}
     * @param data {@link CreateUpdateUserDTO}
     * @return updated {@link User}
     */
    private User applyChanges(User user, final CreateUpdateUserDTO data, final boolean isNew) {

        if (isNew) {
            user.setPassword(data.password());
        }

        user.setEmail(data.email());
        user.setLastName(data.lastName());
        user.setFirstName(data.firstName());
        user.setUsername(data.username());
        user.setRoles(new ArrayList<>(List.of(UserRole.TRADER)));
        user.setPortfolios(new ArrayList<>());

        user = this.userRepository.save(user);

        if (isNew) {
            user.setDateRegistered(LocalDateTime.now());
            user.setApiToken(this.apiTokenService.generateApiToken(user));
        }

        if (!isNew) {
            user = this.userRepository.save(user);
        }

        return this.userRepository.save(user);
    }
}
