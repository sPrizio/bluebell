package com.bluebell.flowerpot.api.converters.account;

import com.bluebell.flowerpot.AbstractGenericTest;
import com.bluebell.flowerpot.api.models.dto.account.AccountDTO;
import com.bluebell.flowerpot.core.services.math.MathService;
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
 * Testing class for {@link AccountDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.0.3
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountDTOConverterTest extends AbstractGenericTest {

    @Autowired
    private AccountDTOConverter accountDTOConverter;

    @MockBean
    private MathService mathService;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.mathService.getDouble(1000.0)).thenReturn(1000.0);
    }


    //  ----------------- convert -----------------

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.accountDTOConverter.convert(null))
                .isNotNull()
                .satisfies(AccountDTO::isEmpty);

    }

    @Test
    public void test_convert_success() {
        assertThat(this.accountDTOConverter.convert(generateTestAccount()))
                .isNotNull()
                .extracting("balance", "active")
                .containsExactly(1000.0, true);

    }


    //  ----------------- convertAll -----------------

    @Test
    public void test_convertAll_success() {
        assertThat(this.accountDTOConverter.convertAll(List.of(generateTestAccount())))
                .isNotEmpty()
                .first()
                .extracting("balance", "active")
                .containsExactly(1000.0, true);
    }
}
