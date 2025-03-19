package com.bluebell.radicle.services.system;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.system.PhoneType;
import com.bluebell.platform.exceptions.calculator.UnexpectedNegativeValueException;
import com.bluebell.platform.models.api.dto.system.CreateUpdatePhoneNumberDTO;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
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
import static org.mockito.ArgumentMatchers.*;

/**
 * Testing class for {@link PhoneNumberService}
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class PhoneNumberServiceTest extends AbstractGenericTest {

    @MockitoBean
    private PhoneNumberRepository phoneNumberRepository;

    @Autowired
    private PhoneNumberService phoneNumberService;

    @BeforeEach
    void setUp() {
        final User testUser = generateTestUser();
        testUser.setPortfolios(List.of(generateTestPortfolio()));

        Mockito.when(this.phoneNumberRepository.findPhoneNumberByPhoneTypeAndCountryCodeAndTelephoneNumber(any(), anyShort(), anyLong())).thenReturn(generateTestPhoneNumber());
        Mockito.when(this.phoneNumberRepository.save(any())).thenReturn(generateTestPhoneNumber());
    }


    //  ----------------- findPhoneNumberForPhoneTypeAndCountryCodeAndTelephoneNumber -----------------

    @Test
    void test_findPhoneNumberForPhoneTypeAndCountryCodeAndTelephoneNumber_missingParamPhoneType() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.phoneNumberService.findPhoneNumberForPhoneTypeAndCountryCodeAndTelephoneNumber(null, (short) 1, 1112223333))
                .withMessage(CorePlatformConstants.Validation.System.PhoneNumber.PHONE_TYPE_CANNOT_BE_NULL);
    }

    @Test
    void test_findPhoneNumberForPhoneTypeAndCountryCodeAndTelephoneNumber_badCountryCode() {
        assertThatExceptionOfType(UnexpectedNegativeValueException.class)
                .isThrownBy(() -> this.phoneNumberService.findPhoneNumberForPhoneTypeAndCountryCodeAndTelephoneNumber(PhoneType.MOBILE, (short) -1, 1112223333))
                .withMessage(CorePlatformConstants.Validation.System.PhoneNumber.COUNTRY_CODE_CANNOT_BE_NEGATIVE);
    }

    @Test
    void test_findPhoneNumberForPhoneTypeAndCountryCodeAndTelephoneNumber_badTelephoneNumber() {
        assertThatExceptionOfType(UnexpectedNegativeValueException.class)
                .isThrownBy(() -> this.phoneNumberService.findPhoneNumberForPhoneTypeAndCountryCodeAndTelephoneNumber(PhoneType.MOBILE, (short) 1, -1112223333))
                .withMessage(CorePlatformConstants.Validation.System.PhoneNumber.TELEPHONE_NUMBER_CANNOT_BE_NEGATIVE);
    }

    @Test
    void test_findPhoneNumberForPhoneTypeAndCountryCodeAndTelephoneNumber_success() {
        assertThat(this.phoneNumberService.findPhoneNumberForPhoneTypeAndCountryCodeAndTelephoneNumber(PhoneType.MOBILE, (short) 1, 1112223333))
                .isPresent()
                .get()
                .extracting("phoneType", "countryCode", "telephoneNumber")
                .containsExactly(PhoneType.MOBILE, (short) 1, 1112223333L);
    }


    //  ----------------- createPhoneNumber -----------------

    @Test
    void test_createPhoneNumber_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.phoneNumberService.createPhoneNumber(null, generateTestUser()))
                .withMessage("The required data for creating a PhoneNumber was null or empty");
    }

    @Test
    void test_createPhoneNumber_success() {

        final CreateUpdatePhoneNumberDTO data = CreateUpdatePhoneNumberDTO
                .builder()
                .phoneType("MOBILE")
                .countryCode((short) 1)
                .telephoneNumber(5149411025L)
                .build();

        assertThat(this.phoneNumberService.createPhoneNumber(data, generateTestUser()))
                .isNotNull()
                .extracting("phoneType", "countryCode", "telephoneNumber")
                .containsExactly(PhoneType.MOBILE, (short) 1, 1112223333L);
    }


    //  ----------------- updatePhoneNumber -----------------

    @Test
    void test_updatePhoneNumber_missingParamPhoneType() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.phoneNumberService.updatePhoneNumber(null, (short) 1, 1112223333, null, generateTestUser()))
                .withMessage(CorePlatformConstants.Validation.System.PhoneNumber.PHONE_TYPE_CANNOT_BE_NULL);
    }

    @Test
    void test_updatePhoneNumber_badCountryCode() {
        assertThatExceptionOfType(UnexpectedNegativeValueException.class)
                .isThrownBy(() -> this.phoneNumberService.updatePhoneNumber(PhoneType.MOBILE, (short) -1, 1112223333, null, generateTestUser()))
                .withMessage(CorePlatformConstants.Validation.System.PhoneNumber.COUNTRY_CODE_CANNOT_BE_NEGATIVE);
    }

    @Test
    void test_updatePhoneNumber_badTelephoneNumber() {
        assertThatExceptionOfType(UnexpectedNegativeValueException.class)
                .isThrownBy(() -> this.phoneNumberService.updatePhoneNumber(PhoneType.MOBILE, (short) 1, -1112223333, null, generateTestUser()))
                .withMessage(CorePlatformConstants.Validation.System.PhoneNumber.TELEPHONE_NUMBER_CANNOT_BE_NEGATIVE);
    }

    @Test
    void test_updatePhoneNumber_missingParamUser() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.phoneNumberService.updatePhoneNumber(PhoneType.MOBILE, (short) 1, 1112223333, null, null))
                .withMessage(CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);
    }


    @Test
    void test_updatePhoneNumber_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.phoneNumberService.updatePhoneNumber(PhoneType.MOBILE, (short) 1, 5149411025L, null, generateTestUser()))
                .withMessage("The required data for updating a PhoneNumber was null or empty");
    }

    @Test
    void test_updatePhoneNumber_success() {

        final CreateUpdatePhoneNumberDTO data = CreateUpdatePhoneNumberDTO
                .builder()
                .phoneType("MOBILE")
                .countryCode((short) 1)
                .telephoneNumber(5149411025L)
                .build();

        assertThat(this.phoneNumberService.updatePhoneNumber(PhoneType.MOBILE, (short) 1, 5149411025L, data, generateTestUser()))
                .isNotNull()
                .extracting("phoneType", "countryCode", "telephoneNumber")
                .containsExactly(PhoneType.MOBILE, (short) 1, 1112223333L);
    }


    //  ----------------- deletePhoneNumber -----------------

    @Test
    void test_deletePhoneNumber_missingParamPhoneNumber() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.phoneNumberService.deletePhoneNumber(null))
                .withMessage(CorePlatformConstants.Validation.System.PhoneNumber.PHONE_NUMBER_CANNOT_BE_NULL);
    }

    @Test
    void test_deletePhoneNumber_success() {
        this.phoneNumberService.deletePhoneNumber(generateTestPhoneNumber());
    }
}
