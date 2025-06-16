package com.bluebell.radicle.services.transaction;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.transaction.TransactionStatus;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.repositories.account.AccountRepository;
import com.bluebell.radicle.repositories.transaction.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for integrations within {@link TransactionService}
 *
 * @author Stephen Prizio
 * @version 0.2.5
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class TransactionServiceIntegrationTest extends AbstractGenericTest {

    private Account account;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        final Account acc = generateTestAccount();
        acc.setId(null);
        this.account = this.accountRepository.save(acc);
    }

    @AfterEach
    void tearDown() {
        this.accountRepository.deleteAll();
        this.transactionRepository.deleteAll();
    }


    //  ----------------- generateUniqueTransactionNumber -----------------

    @Test
    void test_generateUniqueTransactionNumber_missingParamAccount() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.transactionService.generateUniqueTransactionNumber(null))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    void test_generateUniqueTransactionNumber_success() {
        assertThat(this.transactionService.generateUniqueTransactionNumber(generateTestAccount())).isGreaterThan(0);
    }


    //  ----------------- saveAll -----------------

    @Test
    void test_saveAll_success() {

        final Transaction transaction1 = generateTestTransactionDeposit(this.account);
        final Transaction transaction2 = generateTestTransactionWithdrawal(this.account);

        assertThat(this.transactionService.saveAll(null, generateTestAccount())).isEqualTo(-1);
        assertThat(this.transactionService.saveAll(List.of(generateTestTransactionWithdrawal(this.account)), null)).isEqualTo(-1);

        assertThat(this.transactionService.saveAll(List.of(transaction1, transaction2), this.account)).isEqualTo(2);
        assertThat(this.transactionService.saveAll(List.of(transaction1, transaction2), this.account)).isZero();

        transaction1.setTransactionStatus(TransactionStatus.FAILED);
        transaction1.setAmount(23.45);

        assertThat(this.transactionService.saveAll(List.of(transaction1, transaction2), this.account)).isEqualTo(2);
        assertThat(this.transactionRepository.count()).isEqualTo(2);
    }
}
