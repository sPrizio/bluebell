package com.bluebell.radicle.services.security;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.models.api.dto.security.CreateUpdateUserDTO;
import com.bluebell.platform.models.api.dto.system.CreateUpdatePhoneNumberDTO;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.repositories.security.UserRepository;
import com.bluebell.radicle.repositories.system.PhoneNumberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link UserService}
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceTest extends AbstractGenericTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PhoneNumberRepository phoneNumberRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.userRepository.findUserByEmail("test@email.com")).thenReturn(generateTestUser());
        Mockito.when(this.userRepository.findUserByUsername("test")).thenReturn(generateTestUser());
        Mockito.when(this.userRepository.save(any())).thenReturn(generateTestUser());
        Mockito.when(this.phoneNumberRepository.save(any())).thenReturn(generateTestPhoneNumber());
    }


    //  ----------------- findUserByUsername -----------------

    @Test
    void test_findUserByUsername_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.userService.findUserByUsername(null))
                .withMessage("username cannot be null");
    }

    @Test
    void test_findUserByUsername_success() {
        assertThat(this.userService.findUserByUsername("test"))
                .isNotEmpty();
    }


    //  ----------------- findUserByEmail -----------------

    @Test
    void test_findUserByEmail_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.userService.findUserByEmail(null))
                .withMessage("email cannot be null");
    }

    @Test
    void test_findUserByEmail_success() {
        assertThat(this.userService.findUserByEmail("test@email.com"))
                .isNotEmpty();
    }


    //  ----------------- createUser -----------------

    @Test
    void test_createUser_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.userService.createUser(null))
                .withMessage("The required data for creating a User was null or empty");
    }

    @Test
    void test_createUser_success() {

        final CreateUpdateUserDTO data = CreateUpdateUserDTO
                .builder()
                .email("test@123email.com")
                .password("2022-09-05")
                .lastName("Prizio")
                .firstName("Stephen")
                .username("s.prizio")
                .phoneNumbers(
                        List.of(
                                CreateUpdatePhoneNumberDTO
                                        .builder()
                                        .phoneType("MOBILE")
                                        .countryCode((short) 1)
                                        .telephoneNumber(5149411025L)
                                        .build()
                        )
                )
                .build();

        assertThat(this.userService.createUser(data))
                .isNotNull()
                .extracting("email", "firstName", "lastName")
                .containsExactly("test@email.com", "Stephen", "Test");
    }


    //  ----------------- updateUser -----------------

    @Test
    void test_updateUser_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.userService.updateUser(generateTestUser(), null))
                .withMessage("The required data for updating a User was null or empty");
    }

    @Test
    void test_updateUser_success() {

        final CreateUpdateUserDTO data = CreateUpdateUserDTO
                .builder()
                .email("test@email.com")
                .password("2022-09-05")
                .lastName("Prizio")
                .firstName("Stephen")
                .username("s.prizio")
                .phoneNumbers(
                        List.of(
                                CreateUpdatePhoneNumberDTO
                                        .builder()
                                        .phoneType("MOBILE")
                                        .countryCode((short) 1)
                                        .telephoneNumber(5149411025L)
                                        .build()
                        )
                )
                .build();

        assertThat(this.userService.updateUser(generateTestUser(), data))
                .isNotNull()
                .extracting("email", "firstName", "lastName")
                .containsExactly("test@email.com", "Stephen", "Test");
    }
}
