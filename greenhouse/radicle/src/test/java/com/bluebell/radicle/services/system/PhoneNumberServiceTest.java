package com.bluebell.radicle.services.system;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.system.PhoneType;
import com.bluebell.platform.exceptions.calculator.UnexpectedNegativeValueException;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.radicle.AbstractGenericTest;
import com.bluebell.radicle.exceptions.system.EntityCreationException;
import com.bluebell.radicle.exceptions.system.EntityModificationException;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.repositories.system.PhoneNumberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;

/**
 * Testing class for {@link PhoneNumberService}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class PhoneNumberServiceTest extends AbstractGenericTest {

    @MockBean
    private PhoneNumberRepository phoneNumberRepository;

    @Autowired
    private PhoneNumberService phoneNumberService;

    @BeforeEach
    public void setUp() {

        final Account testAccount = generateTestAccount();
        final User testUser = generateTestUser();
        testUser.setAccounts(List.of(testAccount));

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
    void test_createPhoneNumber_erroneousCreation() {
        Map<String, Object> map = Map.of("bad", "input");
        assertThatExceptionOfType(EntityCreationException.class)
                .isThrownBy(() -> this.phoneNumberService.createPhoneNumber(map, generateTestUser()))
                .withMessage("A PhoneNumber could not be created : Cannot invoke \"java.util.Map.get(Object)\" because \"ud\" is null");
    }

    @Test
    void test_createPhoneNumber_success() {

        Map<String, Object> temp = new HashMap<>();
        temp.put("phoneType", "MOBILE");
        temp.put("countryCode", "1");
        temp.put("telephoneNumber", "5149411025");

        Map<String, Object> data = new HashMap<>(Map.of("phoneNumber", temp));

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
    void test_updatePhoneNumber_erroneousModification() {
        Map<String, Object> map = Map.of("bad", "input");
        assertThatExceptionOfType(EntityModificationException.class)
                .isThrownBy(() -> this.phoneNumberService.updatePhoneNumber(PhoneType.MOBILE, (short) 1, 5149411025L, map, generateTestUser()))
                .withMessage("An error occurred while modifying the PhoneNumber : Cannot invoke \"java.util.Map.get(Object)\" because \"ud\" is null");
    }

    @Test
    void test_updatePhoneNumber_success() {

        Map<String, Object> temp = new HashMap<>();
        temp.put("phoneType", "MOBILE");
        temp.put("countryCode", "1");
        temp.put("telephoneNumber", "5144639990");

        Map<String, Object> data = new HashMap<>(Map.of("phoneNumber", temp));

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
