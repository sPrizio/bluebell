package com.bluebell.radicle.security.services;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.radicle.services.security.UserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for security operations
 *
 * @author Stephen Prizio
 * @version 0.2.6
 */
@Service
public class SecurityService {

    @Resource(name = "userService")
    private UserService userService;


    //  METHODS

    /**
     * Checks for a user with the given username or email. Returns a pair of booleans, corresponding to username and email respectively.
     * Ex: if this method return Pair<true, false>, this means that a user with the given username was identified but not an email
     *
     * @param usernameOrEmail test string
     * @return {@link Triplet}
     */
    public Triplet<Boolean, Boolean, User> isUserTaken(final String usernameOrEmail) {
        validateParameterIsNotNull(usernameOrEmail, CorePlatformConstants.Validation.Security.USERNAME_EMAIL_CANNOT_BE_NULL);

        final Optional<User> test1 = this.userService.findUserByUsername(usernameOrEmail);
        final Optional<User> test2 = this.userService.findUserByEmail(usernameOrEmail);

        if (test1.isPresent()) {
            return Triplet.with(true, test2.isPresent(),  test1.get());
        } else if (test2.isPresent()) {
            return Triplet.with(false, true,  test2.get());
        }

        return Triplet.with(false, false, null);
    }

    /**
     * Attempts to log a user in for the given email/username string and password
     *
     * @param usernameOrEmail username email comparison string
     * @param password password
     * @return {@link Pair}
     */
    public Pair<String, User> login(final String usernameOrEmail, final String password) {
        validateParameterIsNotNull(usernameOrEmail, CorePlatformConstants.Validation.Security.USERNAME_EMAIL_CANNOT_BE_NULL);
        validateParameterIsNotNull(password, CorePlatformConstants.Validation.Security.PASSWORD_CANNOT_BE_NULL);

        final Triplet<Boolean, Boolean, User> test = isUserTaken(usernameOrEmail);
        if (Boolean.TRUE.equals(test.getValue0()) || Boolean.TRUE.equals(test.getValue1())) {
            final User user = test.getValue2();
            if (!user.getPassword().equals(password)) {
                return Pair.with("Incorrect password. Try again.", null);
            } else {
                return Pair.with(StringUtils.EMPTY, user);
            }
        }

        if (Boolean.FALSE.equals(test.getValue0())) {
            return Pair.with(String.format("No user found for username %s", usernameOrEmail), null);
        } else {
            return Pair.with(String.format("No user found for email %s", usernameOrEmail), null);
        }
    }
}
