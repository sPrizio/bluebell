package com.bluebell.radicle.services.transaction;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.transaction.TransactionStatus;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.api.dto.transaction.CreateUpdateTransactionDTO;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.repositories.transaction.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link TransactionService}
 *
 * @author Stephen Prizio
 * @version 0.1.7
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class TransactionServiceTest extends AbstractGenericTest {

    @MockitoBean
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.transactionRepository.save(any())).thenReturn(generateTestTransactionDeposit(generateTestAccount()));
    }


    //  ----------------- createNewTransaction -----------------

    @Test
    void test_createNewTransaction_missingAccount() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.createNewTransaction(null, null))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    void test_createNewTransaction_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.transactionService.createNewTransaction(null, generateTestAccount()))
                .withMessage("The required data for creating a Transaction entity was null or empty");
    }

    @Test
    void test_createNewTransaction_success() {

        final CreateUpdateTransactionDTO data = CreateUpdateTransactionDTO
                .builder()
                .transactionDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(CorePlatformConstants.DATE_TIME_NO_TIMEZONE)))
                .transactionStatus(TransactionStatus.COMPLETED.getCode())
                .transactionType(TransactionType.DEPOSIT.getCode())
                .amount(145.89)
                .name("Test Deposit")
                .build();

        assertThat(this.transactionService.createNewTransaction(data, generateTestAccount()))
                .isNotNull()
                .extracting("name", "amount")
                .containsExactly("Test Transaction Deposit", 123.45);
    }


    //  ----------------- updateTransaction -----------------

    @Test
    void test_updateTransaction_missingTransaction() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.updateTransaction(null, null, null))
                .withMessage(CorePlatformConstants.Validation.Transaction.TRANSACTION_CANNOT_BE_NULL);
    }

    @Test
    void test_updateTransaction_missingAccount() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.updateTransaction(generateTestTransactionDeposit(generateTestAccount()), CreateUpdateTransactionDTO.builder().build(), null))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    void test_updateTransaction_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.transactionService.updateTransaction(generateTestTransactionDeposit(generateTestAccount()), CreateUpdateTransactionDTO.builder().build(), generateTestAccount()))
                .withMessage("The required data for updating a Transaction entity was null or empty");
    }

    @Test
    void test_updateTransaction_success() {

        final CreateUpdateTransactionDTO data = CreateUpdateTransactionDTO
                .builder()
                .transactionDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(CorePlatformConstants.DATE_TIME_NO_TIMEZONE)))
                .transactionStatus(TransactionStatus.COMPLETED.getCode())
                .transactionType(TransactionType.DEPOSIT.getCode())
                .amount(145.89)
                .name("Test Deposit")
                .build();

        assertThat(this.transactionService.updateTransaction(generateTestTransactionDeposit(generateTestAccount()), data, generateTestAccount()))
                .isNotNull()
                .extracting("name", "amount")
                .containsExactly("Test Transaction Deposit", 123.45);
    }


    //  ----------------- deleteTransaction -----------------

    @Test
    void test_deleteTransaction_success() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.deleteTransaction(null))
                .withMessage(CorePlatformConstants.Validation.Transaction.TRANSACTION_CANNOT_BE_NULL);

        assertThat(this.transactionService.deleteTransaction(generateTestTransactionDeposit(generateTestAccount()))).isTrue();
    }
}
