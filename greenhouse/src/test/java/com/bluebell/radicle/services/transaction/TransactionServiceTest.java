package com.bluebell.radicle.services.transaction;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.transaction.TransactionStatus;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.api.dto.transaction.CreateUpdateTransactionDTO;
import com.bluebell.platform.models.core.entities.account.Account;
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
import java.util.List;

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
        Mockito.when(this.transactionRepository.findTransactionByNameAndAccount(any(), any())).thenReturn(generateTestTransactionDeposit(generateTestAccount()));
        Mockito.when(this.transactionRepository.findAllTransactionsWithinDate(LocalDateTime.MIN.plusMonths(1), LocalDateTime.MAX, generateTestAccount())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount()), generateTestTransactionWithdrawal(generateTestAccount())));
        Mockito.when(this.transactionRepository.findAllTransactionsWithinDate(
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class),
                Mockito.any(Account.class)
        )).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount()), generateTestTransactionWithdrawal(generateTestAccount())));

        Mockito.when(this.transactionRepository.findAllByTransactionStatusAndAccount(any(), any())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount())));
        Mockito.when(this.transactionRepository.findAllByTransactionTypeAndAccount(any(), any())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount())));
        Mockito.when(this.transactionRepository.findAllByAccount(any())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount())));
    }


    //  ----------------- findRecentTransactions -----------------

    @Test
    void test_findRecentTransactions_badData() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findRecentTransactions(null))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    void test_findRecentTransactions_success() {
        assertThat(this.transactionService.findRecentTransactions(generateTestAccount()))
                .hasSize(2);
    }


    //  ----------------- findAllTransactionsForAccount -----------------

    @Test
    void test_findAllTransactionsForAccount_badData() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsForAccount(null))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    void test_findAllTransactionsForAccount_success() {
        assertThat(this.transactionService.findAllTransactionsForAccount(generateTestAccount()))
                .hasSize(1);
    }


    //  ----------------- findAllTransactionsByTypeForAccount -----------------

    @Test
    void test_findAllTransactionsByTypeForAccount_badData() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsByTypeForAccount(null, generateTestAccount()))
                .withMessage(CorePlatformConstants.Validation.Transaction.TRANSACTION_TYPE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsByTypeForAccount(TransactionType.DEPOSIT, null))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    void test_findAllTransactionsByTypeForAccount_success() {
        assertThat(this.transactionService.findAllTransactionsByTypeForAccount(TransactionType.DEPOSIT, generateTestAccount()))
                .hasSize(1);
    }


    //  ----------------- findAllTransactionsByStatusForAccount -----------------

    @Test
    void test_findAllTransactionsByStatusForAccount_badData() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsByStatusForAccount(null, generateTestAccount()))
                .withMessage(CorePlatformConstants.Validation.Transaction.TRANSACTION_STATUS_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsByStatusForAccount(TransactionStatus.COMPLETED, null))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    void test_findAllTransactionsByStatusForAccount_success() {
        assertThat(this.transactionService.findAllTransactionsByStatusForAccount(TransactionStatus.COMPLETED, generateTestAccount()))
                .hasSize(1);
    }


    //  ----------------- findTransactionsWithinTimespanForAccount -----------------

    @Test
    void test_findTransactionsWithinTimespanForAccount_missingParamStart() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findTransactionsWithinTimespanForAccount(null, LocalDateTime.MAX, generateTestAccount()))
                .withMessage(CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_findTransactionsWithinTimespanForAccount_missingParamEnd() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findTransactionsWithinTimespanForAccount(LocalDateTime.MAX, null, generateTestAccount()))
                .withMessage(CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_findTransactionsWithinTimespanForAccount_invalidInterval() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.transactionService.findTransactionsWithinTimespanForAccount(LocalDateTime.MAX, LocalDateTime.MIN, generateTestAccount()))
                .withMessage(CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    void test_findTransactionsWithinTimespanForAccount_missingParamAccount() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findTransactionsWithinTimespanForAccount(LocalDateTime.MIN, LocalDateTime.MAX, null))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    void test_findTransactionsWithinTimespanForAccount_success() {
        assertThat(this.transactionService.findTransactionsWithinTimespanForAccount(LocalDateTime.MIN.plusMonths(1), LocalDateTime.MAX, generateTestAccount()))
                .hasSize(2);
    }


    //  ----------------- findTransactionForNameAndAccount -----------------

    @Test
    void test_findTransactionForNameAndAccount_success() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findTransactionForNameAndAccount(null, generateTestAccount()))
                .withMessage(CorePlatformConstants.Validation.Transaction.TRANSACTION_NAME_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findTransactionForNameAndAccount("Test", null))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        assertThat(this.transactionService.findTransactionForNameAndAccount("test", generateTestAccount()))
                .isNotEmpty();
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
