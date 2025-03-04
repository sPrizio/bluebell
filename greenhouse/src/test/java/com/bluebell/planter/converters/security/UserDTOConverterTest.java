package com.bluebell.planter.converters.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.converters.account.AccountDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.security.UserDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Testing class for {@link UserDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserDTOConverterTest extends AbstractPlanterTest {

    @MockBean
    private AccountDTOConverter accountDTOConverter;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Autowired
    private UserDTOConverter userDTOConverter;

    @Before
    public void setUp() {
        Mockito.when(this.accountDTOConverter.convert(any())).thenReturn(generateTestAccountDTO());
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.userDTOConverter.convert(null))
                .isNotNull()
                .satisfies(UserDTO::isEmpty);

    }

    /*@Test
    public void test_convert_success() {
        assertThat(this.userDTOConverter.convert(generateTestUser()))
                .isNotNull()
                .extracting("email", "username")
                .containsExactly("test@email.com", "s.prizio");

    }


    //  ----------------- convertAll -----------------

    @Test
    public void test_convertAll_success() {
        assertThat(this.userDTOConverter.convertAll(List.of(generateTestUser())))
                .isNotEmpty()
                .first()
                .extracting("email", "username")
                .containsExactly("test@email.com", "s.prizio");
    }*/
}
