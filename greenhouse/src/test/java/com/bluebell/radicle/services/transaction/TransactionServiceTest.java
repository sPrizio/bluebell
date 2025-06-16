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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

/**
 * Testing class for {@link TransactionService}
 *
 * @author Stephen Prizio
 * @version 0.2.5
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
        Mockito.when(this.transactionRepository.findTransactionByTransactionNumberAndAccount(anyLong(), any())).thenReturn(generateTestTransactionDeposit(generateTestAccount()));
        Mockito.when(this.transactionRepository.findAllTransactionsWithinDate(LocalDateTime.MIN.plusMonths(1), LocalDateTime.MAX, generateTestAccount())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount()), generateTestTransactionWithdrawal(generateTestAccount())));
        Mockito.when(this.transactionRepository.findAllTransactionsWithinDate(
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class),
                Mockito.any(Account.class)
        )).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount()), generateTestTransactionWithdrawal(generateTestAccount())));

        Mockito.when(this.transactionRepository.findAllByTransactionStatusAndAccount(any(), any())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount())));
        Mockito.when(this.transactionRepository.findAllByTransactionTypeAndAccount(any(), any())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount())));
        Mockito.when(this.transactionRepository.findAllByAccount(any())).thenReturn(List.of(generateTestTransactionDeposit(generateTestAccount())));
        Mockito.when(this.transactionRepository.findAllTransactionsForStatusWithinDatePaged(any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(generateTestTransactionDeposit(generateTestAccount()), generateTestTransactionWithdrawal(generateTestAccount()))));
        Mockito.when(this.transactionRepository.findAllTransactionsForTypeWithinDatePaged(any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(generateTestTransactionDeposit(generateTestAccount()), generateTestTransactionWithdrawal(generateTestAccount()))));
        Mockito.when(this.transactionRepository.findAllTransactionsForTypeAndStatusWithinTimespanPaged(any(), any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(generateTestTransactionDeposit(generateTestAccount()), generateTestTransactionWithdrawal(generateTestAccount()))));
        Mockito.when(this.transactionRepository.findAllTransactionsWithinDatePaged(any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(generateTestTransactionDeposit(generateTestAccount()), generateTestTransactionWithdrawal(generateTestAccount()))));
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


    //  ----------------- findTransactionForNumber -----------------

    @Test
    void test_findTransactionForNumber_success() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findTransactionForNumber(1234L, null))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        assertThat(this.transactionService.findTransactionForNumber(1234L, generateTestAccount()))
                .isNotEmpty();
    }


    //  ----------------- findAllTransactionsWithinDatePaged (paged) -----------------

    @Test
    void test_findAllTransactionsWithinDatePaged_paged_missingParamStart() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsWithinDatePaged(null, LocalDateTime.MAX, generateTestAccount(), 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_findAllTransactionsWithinDatePaged_paged_missingParamEnd() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsWithinDatePaged(LocalDateTime.MAX, null, generateTestAccount(), 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    void test_findAllTransactionsWithinDatePaged_paged_missingParamAccount() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsWithinDatePaged(LocalDateTime.MIN, LocalDateTime.MAX, null, 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    void test_findAllTransactionsWithinDatePaged_paged_invalidInterval() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsWithinDatePaged(LocalDateTime.MAX, LocalDateTime.MIN, generateTestAccount(), 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    void test_findAllTransactionsWithinDatePaged_paged_success() {
        assertThat(this.transactionService.findAllTransactionsWithinDatePaged(LocalDateTime.MIN.plusMonths(1), LocalDateTime.MAX, generateTestAccount(), 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .hasSize(2);
    }


    //  ----------------- findAllTransactionsForStatusWithinDatePaged (paged) -----------------

    @Test
    void test_findAllTransactionsForStatusWithinDatePaged_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsForStatusWithinDatePaged(null, LocalDateTime.MAX, generateTestAccount(), TransactionStatus.COMPLETED, 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsForStatusWithinDatePaged(LocalDateTime.MIN, null, generateTestAccount(), TransactionStatus.COMPLETED, 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsForStatusWithinDatePaged(LocalDateTime.MAX, LocalDateTime.MIN, generateTestAccount(), TransactionStatus.COMPLETED, 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsForStatusWithinDatePaged(LocalDateTime.MIN, LocalDateTime.MAX, generateTestAccount(), null, 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.Transaction.TRANSACTION_STATUS_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsForStatusWithinDatePaged(LocalDateTime.MIN, LocalDateTime.MAX, null, TransactionStatus.COMPLETED, 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    void test_findAllTransactionsForStatusWithinDatePaged_success() {
        assertThat(this.transactionService.findAllTransactionsForStatusWithinDatePaged(LocalDateTime.MIN.plusMonths(1), LocalDateTime.MAX, generateTestAccount(), TransactionStatus.COMPLETED, 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .hasSize(2);
    }


    //  ----------------- findAllTransactionsForTypeWithinDatePaged (paged) -----------------

    @Test
    void test_findAllTransactionsForTypeWithinDatePaged_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsForTypeWithinDatePaged(null, LocalDateTime.MAX, generateTestAccount(), TransactionType.DEPOSIT, 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsForTypeWithinDatePaged(LocalDateTime.MIN, null, generateTestAccount(), TransactionType.DEPOSIT, 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsForTypeWithinDatePaged(LocalDateTime.MAX, LocalDateTime.MIN, generateTestAccount(), TransactionType.DEPOSIT, 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsForTypeWithinDatePaged(LocalDateTime.MIN, LocalDateTime.MAX, generateTestAccount(), null, 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.Transaction.TRANSACTION_TYPE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsForTypeWithinDatePaged(LocalDateTime.MIN, LocalDateTime.MAX, null, TransactionType.DEPOSIT, 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    void test_findAllTransactionsForTypeWithinDatePaged_success() {
        assertThat(this.transactionService.findAllTransactionsForTypeWithinDatePaged(LocalDateTime.MIN.plusMonths(1), LocalDateTime.MAX, generateTestAccount(), TransactionType.DEPOSIT, 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .hasSize(2);
    }


    //  ----------------- findAllTransactionsForTypeAndStatusWithinTimespan (paged) -----------------

    @Test
    void test_findAllTransactionsForTypeAndStatusWithinTimespan_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsForTypeAndStatusWithinTimespan(null, LocalDateTime.MAX, TransactionType.DEPOSIT, TransactionStatus.COMPLETED, generateTestAccount(), 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsForTypeAndStatusWithinTimespan(LocalDateTime.MIN, null, TransactionType.DEPOSIT, TransactionStatus.COMPLETED, generateTestAccount(), 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsForTypeAndStatusWithinTimespan(LocalDateTime.MAX, LocalDateTime.MIN, TransactionType.DEPOSIT, TransactionStatus.COMPLETED, generateTestAccount(), 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsForTypeAndStatusWithinTimespan(LocalDateTime.MIN, LocalDateTime.MAX, null, TransactionStatus.COMPLETED, generateTestAccount(), 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.Transaction.TRANSACTION_TYPE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsForTypeAndStatusWithinTimespan(LocalDateTime.MIN, LocalDateTime.MAX, TransactionType.DEPOSIT, null, generateTestAccount(), 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.Transaction.TRANSACTION_STATUS_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.findAllTransactionsForTypeAndStatusWithinTimespan(LocalDateTime.MIN, LocalDateTime.MAX, TransactionType.DEPOSIT, TransactionStatus.COMPLETED, null, 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    void test_findAllTransactionsForTypeAndStatusWithinTimespan_success() {
        assertThat(this.transactionService.findAllTransactionsForTypeAndStatusWithinTimespan(LocalDateTime.MIN.plusMonths(1), LocalDateTime.MAX, TransactionType.DEPOSIT, TransactionStatus.COMPLETED, generateTestAccount(), 0, 10, Sort.by(Sort.Direction.ASC, "transactionDate")))
                .hasSize(2);
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
                .transactionNumber(1234L)
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
                .transactionNumber(1234L)
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
