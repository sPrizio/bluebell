package com.bluebell.planter.converters.system;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.system.PhoneNumberDTO;
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
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link PhoneNumberDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class PhoneNumberDTOConverterTest extends AbstractPlanterTest {

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Autowired
    private PhoneNumberDTOConverter phoneNumberDTOConverter;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    void test_convert_success_emptyResult() {
        assertThat(this.phoneNumberDTOConverter.convert(null))
                .isNotNull()
                .satisfies(PhoneNumberDTO::isEmpty);

    }

    @Test
    void test_convert_success() {
        assertThat(this.phoneNumberDTOConverter.convert(generateTestPhoneNumber()))
                .isNotNull()
                .extracting("phoneType", "countryCode")
                .containsExactly("MOBILE", (short) 1);

    }


    //  ----------------- convertAll -----------------

    @Test
    void test_convertAll_success() {
        assertThat(this.phoneNumberDTOConverter.convertAll(List.of(generateTestPhoneNumber())))
                .isNotEmpty()
                .first()
                .extracting("phoneType", "countryCode")
                .containsExactly("MOBILE", (short) 1);
    }
}
