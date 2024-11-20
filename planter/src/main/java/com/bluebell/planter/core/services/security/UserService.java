package com.bluebell.planter.core.services.security;

import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.security.UserRole;
import com.bluebell.planter.core.exceptions.security.DuplicateUserEmailException;
import com.bluebell.planter.core.exceptions.security.DuplicateUserUsernameException;
import com.bluebell.planter.core.exceptions.system.EntityCreationException;
import com.bluebell.planter.core.exceptions.system.EntityModificationException;
import com.bluebell.planter.core.exceptions.validation.MissingRequiredDataException;
import com.bluebell.planter.core.models.entities.security.User;
import com.bluebell.planter.core.models.entities.system.PhoneNumber;
import com.bluebell.planter.core.repositories.security.UserRepository;
import com.bluebell.planter.core.services.system.PhoneNumberService;
import com.bluebell.planter.security.services.ApiTokenService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.bluebell.planter.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link User} entities
 *
 * @author Stephen Prizio
 * @version 0.0.2
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
        validateParameterIsNotNull(username, CoreConstants.Validation.Security.User.USERNAME_CANNOT_BE_NULL);
        return Optional.ofNullable(this.userRepository.findUserByUsername(username));
    }

    /**
     * Finds a {@link User} by their email
     *
     * @param email email
     * @return {@link Optional} {@link User}
     */
    public Optional<User> findUserByEmail(final String email) {
        validateParameterIsNotNull(email, CoreConstants.Validation.Security.User.EMAIL_CANNOT_BE_NULL);
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

        final String email = ((Map<String, Object>) data.get("user")).get("email").toString();
        final String username = ((Map<String, Object>) data.get("user")).get("username").toString();

        if (this.userRepository.findUserByEmail(email) != null) {
            throw new DuplicateUserEmailException(String.format("A user with the email %s already exists. Please try another email.", email));
        }

        if (this.userRepository.findUserByUsername(username) != null) {
            throw new DuplicateUserUsernameException(String.format("A user with the username %s already exists. Please try another username.", username));
        }

        try {
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

        validateParameterIsNotNull(user, CoreConstants.Validation.Security.User.USER_CANNOT_BE_NULL);

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

        user.setEmail(ud.get("email").toString());
        user.setPassword(ud.get("password").toString());
        user.setLastName(ud.get("lastName").toString());
        user.setFirstName(ud.get("firstName").toString());
        user.setUsername(ud.get("username").toString());
        user.setRoles(new ArrayList<>(List.of(UserRole.TRADER)));
        user.setAccounts(new ArrayList<>());
        user.setPhones(new ArrayList<>());

        /*user = this.userRepository.save(user);

        if (isNew) {
            user.setDateRegistered(LocalDateTime.now());
            user.setApiToken(this.apiTokenService.generateApiToken(user));
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
        return this.userRepository.save(user);*/
        return this.userRepository.findUserByUsername("s.prizio");
    }
}
