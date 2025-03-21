package com.bluebell.planter.converters.security;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.converters.account.AccountDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.security.UserDTO;
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
 * Testing class for {@link UserDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class UserDTOConverterTest extends AbstractPlanterTest {

    @MockitoBean
    private AccountDTOConverter accountDTOConverter;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Autowired
    private UserDTOConverter userDTOConverter;

    @BeforeEach
    void setUp() {
        Mockito.when(this.accountDTOConverter.convert(any())).thenReturn(generateTestAccountDTO());
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    void test_convert_success_emptyResult() {
        assertThat(this.userDTOConverter.convert(null))
                .isNotNull()
                .satisfies(UserDTO::isEmpty);

    }

    @Test
    void test_convert_success() {
        assertThat(this.userDTOConverter.convert(generateTestUser()))
                .isNotNull()
                .extracting("email", "username")
                .containsExactly("test@email.com", "s.prizio");

    }


    //  ----------------- convertAll -----------------

    @Test
    void test_convertAll_success() {
        assertThat(this.userDTOConverter.convertAll(List.of(generateTestUser())))
                .isNotEmpty()
                .first()
                .extracting("email", "username")
                .containsExactly("test@email.com", "s.prizio");
    }
}
