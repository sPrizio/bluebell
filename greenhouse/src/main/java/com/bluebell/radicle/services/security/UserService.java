package com.bluebell.radicle.services.security;

import java.time.LocalDateTime;
import java.util.*;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.security.UserRole;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.entities.system.PhoneNumber;
import com.bluebell.radicle.exceptions.security.DuplicateUserEmailException;
import com.bluebell.radicle.exceptions.security.DuplicateUserUsernameException;
import com.bluebell.radicle.exceptions.system.EntityCreationException;
import com.bluebell.radicle.exceptions.system.EntityModificationException;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.repositories.security.UserRepository;
import com.bluebell.radicle.security.services.ApiTokenService;
import com.bluebell.radicle.services.system.PhoneNumberService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;


/**
 * Service-layer for {@link User} entities
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Service
public class UserService {

    @Resource(name = "apiTokenService")
    private ApiTokenService apiTokenService;

    @Resource(name = "phoneNumberService")
    private PhoneNumberService phoneNumberService;

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
     * @param data {@link Map}
     * @return newly created {@link User}
     */
    public User createUser(final Map<String, Object> data) {

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for creating a User was null or empty");
        }

        String email = "";
        String username;

        try {
            email = ((Map<String, Object>) data.get("user")).get("email").toString();
            username = ((Map<String, Object>) data.get("user")).get("username").toString();

            if (this.userRepository.findUserByEmail(email) != null) {
                throw new DuplicateUserEmailException(String.format("A user with the email %s already exists. Please try another email.", email));
            }

            if (this.userRepository.findUserByUsername(username) != null) {
                throw new DuplicateUserUsernameException(String.format("A user with the username %s already exists. Please try another username.", username));
            }

            return applyChanges(new User(), data, true);
        } catch (Exception e) {
            this.userRepository.deleteUserByEmail(email);
            throw new EntityCreationException(String.format("A User could not be created : %s", e.getMessage()), e);
        }
    }

    /**
     * Updates an existing {@link User} with the given {@link Map} of data. Update methods are designed to be idempotent.
     *
     * @param user {@link User}
     * @param data {@link Map}
     * @return modified {@link User}
     */
    public User updateUser(final User user, final Map<String, Object> data) {

        validateParameterIsNotNull(user, CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);

        if (MapUtils.isEmpty(data)) {
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
     * @param data {@link Map}
     * @return updated {@link User}
     */
    private User applyChanges(User user, final Map<String, Object> data, final boolean isNew) {

        Map<String, Object> ud = (Map<String, Object>) data.get("user");
        Set<PhoneNumber> phoneNumbers = (CollectionUtils.isEmpty(user.getPhones())) ? new HashSet<>() : new HashSet<>(user.getPhones());

        if (isNew) {
            user.setPassword(ud.get("password").toString());
        }

        user.setEmail(ud.get("email").toString());
        user.setLastName(ud.get("lastName").toString());
        user.setFirstName(ud.get("firstName").toString());
        user.setUsername(ud.get("username").toString());
        user.setRoles(new ArrayList<>(List.of(UserRole.TRADER)));
        user.setAccounts(new ArrayList<>());
        user.setPhones(new ArrayList<>());

        user = this.userRepository.save(user);

        if (isNew) {
            user.setDateRegistered(LocalDateTime.now());
            user.setApiToken(this.apiTokenService.generateApiToken(user));
        }

        if (!isNew) {
            user.getPhones().forEach(ph -> this.phoneNumberService.deletePhoneNumber(ph));
            user = this.userRepository.save(user);
        }

        List<Map<String, Object>> phoneData = (List<Map<String, Object>>) ud.get("phoneNumbers");
        for (Map<String, Object> d : phoneData) {
            final PhoneNumber phoneNumber = this.phoneNumberService.createPhoneNumber(d, user);
            if (!phoneNumbers.contains(phoneNumber)) {
                phoneNumbers.add(phoneNumber);
            } else {
                this.phoneNumberService.deletePhoneNumber(phoneNumber);
            }
        }

        user.setPhones(new ArrayList<>(phoneNumbers));
        return this.userRepository.save(user);
    }
}
