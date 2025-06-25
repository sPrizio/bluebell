package com.bluebell.platform.services;

import com.bluebell.platform.constants.CorePlatformConstants;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service for reading and generating passwords
 *
 * @author Stephen Prizio
 * @version 0.2.6
 */
public class PasswordService {


    //  METHODS

    /**
     * Encrypts a plain-text password
     *
     * @param password password
     * @return encrypted password
     */
    public String encryptPassword(final String password) {
        validateParameterIsNotNull(password, CorePlatformConstants.Validation.Security.PASSWORD_CANNOT_BE_NULL);
        return new String(Base64.getEncoder().encode(password.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Decrypts a password into plain-text
     *
     * @param password encrypted password
     * @return plain-text password
     */
    public String readPassword(final String password) {
        validateParameterIsNotNull(password, CorePlatformConstants.Validation.Security.PASSWORD_CANNOT_BE_NULL);
        return new String(Base64.getDecoder().decode(password.getBytes(StandardCharsets.UTF_8)));
    }
}
