package com.bluebell.radicle.security.services;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.services.security.UserService;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link SecurityServiceTest}
 *
 * @author Stephen Prizio
 * @version 0.2.6
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class SecurityServiceTest extends AbstractGenericTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.userService.findUserByUsername("found1")).thenReturn(Optional.of(generateTestUser()));
        Mockito.when(this.userService.findUserByEmail("found2")).thenReturn(Optional.of(generateTestUser()));
        Mockito.when(this.userService.findUserByUsername("not_found1")).thenReturn(Optional.empty());
        Mockito.when(this.userService.findUserByEmail("not_found2")).thenReturn(Optional.empty());
    }


    //  ----------------- isUserTaken -----------------

    @Test
    void test_isUserTaken_missingParamString() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.securityService.isUserTaken(null))
                .withMessage(CorePlatformConstants.Validation.Security.USERNAME_EMAIL_CANNOT_BE_NULL);
    }

    @Test
    void test_isUserTaken_hasUsername() {
        final Triplet<Boolean, Boolean, User> result = this.securityService.isUserTaken("found1");
        assertThat(result.getValue0()).isTrue();
    }

    @Test
    void test_isUserTaken_hasEmail() {
        final Triplet<Boolean, Boolean, User> result = this.securityService.isUserTaken("found2");
        assertThat(result.getValue1()).isTrue();
    }

    @Test
    void test_isUserTaken_success() {
        final Triplet<Boolean, Boolean, User> result = this.securityService.isUserTaken("not_found1");
        assertThat(result.getValue0()).isFalse();
        assertThat(result.getValue1()).isFalse();
    }


    //  ----------------- login -----------------

    @Test
    void test_login_missingParamString() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.securityService.login(null, "hello there"))
                .withMessage(CorePlatformConstants.Validation.Security.USERNAME_EMAIL_CANNOT_BE_NULL);
    }
    @Test
    void test_login_missingParamPassword() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.securityService.login("null", null))
                .withMessage(CorePlatformConstants.Validation.Security.PASSWORD_CANNOT_BE_NULL);
    }

    @Test
    void test_login_noEmail() {
        final Pair<String, User> result = this.securityService.login("not_found2", "test");
        assertThat(result.getValue0()).isEqualTo("No user found for username not_found2");
    }

    @Test
    void test_login_noUsername() {
        final Pair<String, User> result = this.securityService.login("not_found1", "test");
        assertThat(result.getValue0()).isEqualTo("No user found for username not_found1");
    }

    @Test
    void test_login_badPassword() {
        final Pair<String, User> result = this.securityService.login("found1", "test");
        assertThat(result.getValue0()).isEqualTo("Incorrect password. Try again.");
    }

    @Test
    void test_login_success() {
        final Pair<String, User> result = this.securityService.login("found1", "1234");
        assertThat(result.getValue1()).isNotNull();
    }
}
