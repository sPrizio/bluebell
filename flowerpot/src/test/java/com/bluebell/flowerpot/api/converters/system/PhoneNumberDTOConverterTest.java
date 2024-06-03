package com.bluebell.flowerpot.api.converters.system;

import com.bluebell.flowerpot.AbstractGenericTest;
import com.bluebell.flowerpot.api.models.dto.system.PhoneNumberDTO;
import com.bluebell.flowerpot.core.services.platform.UniqueIdentifierService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link PhoneNumberDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.0.3
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PhoneNumberDTOConverterTest extends AbstractGenericTest {

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Autowired
    private PhoneNumberDTOConverter phoneNumberDTOConverter;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.phoneNumberDTOConverter.convert(null))
                .isNotNull()
                .satisfies(PhoneNumberDTO::isEmpty);

    }

    @Test
    public void test_convert_success() {
        assertThat(this.phoneNumberDTOConverter.convert(generateTestPhoneNumber()))
                .isNotNull()
                .extracting("phoneType", "countryCode")
                .containsExactly("MOBILE", (short) 1);

    }


    //  ----------------- convertAll -----------------

    @Test
    public void test_convertAll_success() {
        assertThat(this.phoneNumberDTOConverter.convertAll(List.of(generateTestPhoneNumber())))
                .isNotEmpty()
                .first()
                .extracting("phoneType", "countryCode")
                .containsExactly("MOBILE", (short) 1);
    }
}
