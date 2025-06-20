package com.bluebell.platform.services;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link PasswordService}
 *
 * @author Stephen Prizio
 * @version 0.2.6
 */
class PasswordServiceTest {

    private static final String TEST_PASSWORD = "I am the one who knocks";
    private final PasswordService passwordService = new PasswordService();


    //  ----------------- encryptPassword -----------------

    @Test
    void test_encryptPassword_missingParamPassword() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.passwordService.encryptPassword(null))
                .withMessage(CorePlatformConstants.Validation.Security.PASSWORD_CANNOT_BE_NULL);
    }

    @Test
    void test_encryptPassword_success() {
        final String encrypted = new String(Base64.getEncoder().encode(TEST_PASSWORD.getBytes(StandardCharsets.UTF_8)));
        assertThat(this.passwordService.encryptPassword(TEST_PASSWORD)).isEqualTo(encrypted);
    }


    //  ----------------- readPassword -----------------

    @Test
    void test_readPassword_missingParamPassword() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.passwordService.readPassword(null))
                .withMessage(CorePlatformConstants.Validation.Security.PASSWORD_CANNOT_BE_NULL);
    }

    @Test
    void test_readPassword_success() {
        final String encrypted = new String(Base64.getEncoder().encode(TEST_PASSWORD.getBytes(StandardCharsets.UTF_8)));
        assertThat(this.passwordService.readPassword(encrypted)).isEqualTo(TEST_PASSWORD);
    }
}
