package com.bluebell.radicle.security.services;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.radicle.security.exceptions.InvalidApiTokenException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;


/**
 * Service that handles generating and validating api tokens for incoming API requests
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Service
public class ApiTokenService {

    private static final String TOKEN_PREFIX = "flowerpot_api_token";


    //  METHODS

    /**
     * Generates a unique API token for the given {@link User}
     *
     * @param user {@link User}
     * @return unique API token
     */
    public String generateApiToken(final User user) {
        validateParameterIsNotNull(user, CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);
        validateParameterIsNotNull(user.getDateRegistered(), CorePlatformConstants.Validation.Security.User.USER_DATE_REGISTERED_CANNOT_BE_NULL);
        return Base64.getEncoder().encodeToString(String.format("%s&%s&%s", TOKEN_PREFIX, user.getEmail(), user.getDateRegistered().format(DateTimeFormatter.ofPattern(CorePlatformConstants.DATE_TIME_FORMAT))).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Reads an API token into it's decrypted form
     *
     * @param apiToken api token
     * @return plain api token
     */
    public String readApiToken(final String apiToken) {
        validateParameterIsNotNull(apiToken, CorePlatformConstants.Validation.Security.API_TOKEN_CANNOT_BE_NULL);

        try {
            return new String(Base64.getDecoder().decode(apiToken));
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * Using the given API token, obtains the associated {@link User}
     *
     * @param apiToken API token
     * @return {@link User}
     * @throws InvalidApiTokenException throws an exception when the api token does not conform to expectations
     */
    public String getEmailForApiToken(final String apiToken) {
        validateParameterIsNotNull(apiToken, CorePlatformConstants.Validation.Security.API_TOKEN_CANNOT_BE_NULL);

        final String token = readApiToken(apiToken);
        if (token.startsWith(TOKEN_PREFIX)) {
            final String[] decrypted = readApiToken(apiToken).split("&");
            if (decrypted.length < 2) {
                throw new InvalidApiTokenException("The given api token did not map to anything");
            }

            return decrypted[1];
        }

        throw new InvalidApiTokenException("The given api token was not of the expected format. It is not genuine");
    }
}
