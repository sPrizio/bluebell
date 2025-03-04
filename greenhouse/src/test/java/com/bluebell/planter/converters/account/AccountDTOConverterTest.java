package com.bluebell.planter.converters.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.account.AccountDTO;
import com.bluebell.platform.services.MathService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Testing class for {@link AccountDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class AccountDTOConverterTest extends AbstractPlanterTest {

    @Autowired
    private AccountDTOConverter accountDTOConverter;

    @MockitoBean
    private MathService mathService;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.mathService.getDouble(1000.0)).thenReturn(1000.0);
    }


    //  ----------------- convert -----------------

    @Test
    void test_convert_success_emptyResult() {
        assertThat(this.accountDTOConverter.convert(null))
                .isNotNull()
                .satisfies(AccountDTO::isEmpty);

    }

    @Test
    void test_convert_success() {
        assertThat(this.accountDTOConverter.convert(generateTestAccount()))
                .isNotNull()
                .extracting("balance", "active")
                .containsExactly(1000.0, true);

    }


    //  ----------------- convertAll -----------------

    @Test
    void test_convertAll_success() {
        assertThat(this.accountDTOConverter.convertAll(List.of(generateTestAccount())))
                .isNotEmpty()
                .first()
                .extracting("balance", "active")
                .containsExactly(1000.0, true);
    }
}
