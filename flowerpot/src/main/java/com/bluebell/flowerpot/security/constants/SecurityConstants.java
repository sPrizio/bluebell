package com.bluebell.flowerpot.security.constants;

import com.bluebell.flowerpot.core.constants.CoreConstants;
import com.bluebell.flowerpot.core.models.entities.security.User;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Constants used with regard to system security
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
public class SecurityConstants {

    private SecurityConstants() {
        throw new UnsupportedOperationException(String.format(CoreConstants.NO_INSTANTIATION, getClass().getName()));
    }

    /**
     * Expected header for incoming requests to contain the api token
     */
    public static final String API_KEY_TOKEN = "fp-api_token";

    /**
     * The key on {@link HttpServletRequest}s containing the current {@link User} context
     */
    public static final String USER_REQUEST_KEY = "user";
}
