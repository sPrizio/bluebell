package com.bluebell.planter.converters.transaction;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.transaction.TransactionDTO;
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
 * Testing class for {@link TransactionDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class TransactionDTOConverterTest extends AbstractPlanterTest {

    @Autowired
    private TransactionDTOConverter transactionDTOConverter;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    void test_convert_success_emptyResult() {
        assertThat(this.transactionDTOConverter.convert(null))
                .isNotNull()
                .satisfies(TransactionDTO::isEmpty);

    }

    @Test
    void test_convert_success() {
        assertThat(this.transactionDTOConverter.convert(generateTestTransactionDeposit(generateTestAccount())))
                .isNotNull()
                .extracting("name", "amount")
                .containsExactly(
                        "Test Transaction Deposit",
                        123.45
                );

    }


    //  ----------------- convertAll -----------------

    @Test
    void test_convertAll_success() {
        assertThat(this.transactionDTOConverter.convertAll(List.of(generateTestTransactionDeposit(generateTestAccount()))))
                .isNotEmpty()
                .first()
                .extracting("name", "amount")
                .containsExactly(
                        "Test Transaction Deposit",
                        123.45
                );
    }
}
