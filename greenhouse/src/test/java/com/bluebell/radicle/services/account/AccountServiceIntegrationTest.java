package com.bluebell.radicle.services.account;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.api.dto.account.CreateUpdateAccountTradingDataDTO;
import com.bluebell.platform.models.api.dto.trade.CreateUpdateTradeDTO;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.repositories.account.AccountRepository;
import com.bluebell.radicle.repositories.trade.TradeRepository;
import com.bluebell.radicle.repositories.transaction.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing integrations for {@link AccountService}
 *
 * @author Stephen Prizio
 * @version 0.1.8
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class AccountServiceIntegrationTest extends AbstractGenericTest {

    private Account account;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        final Account acc = generateTestAccount();
        acc.setId(null);
        this.account = this.accountRepository.save(acc);
    }

    @AfterEach
    void tearDown() {
        this.accountRepository.deleteAll();
        this.tradeRepository.deleteAll();
    }


    //  ----------------- updateAccountTradingData -----------------

    @Test
    void test_updateAccountTradingData_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.updateAccountTradingData(null))
                .withMessage("trades dto cannot be null");
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.updateAccountTradingData(CreateUpdateAccountTradingDataDTO.builder().accountNumber(1L).build()))
                .withMessage(CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);
    }

    @Test
    void test_updateAccountTradingData_failed() {
        final CreateUpdateAccountTradingDataDTO badAccount =
                CreateUpdateAccountTradingDataDTO
                        .builder()
                        .userIdentifier("Test")
                        .accountNumber(-1L)
                        .trades(List.of(CreateUpdateTradeDTO.builder().build()))
                        .build();

        assertThat(this.accountService.updateAccountTradingData(badAccount)).isFalse();
    }

    @Test
    void test_updateAccountTradingData_success() {
        final CreateUpdateAccountTradingDataDTO data =
                CreateUpdateAccountTradingDataDTO
                        .builder()
                        .userIdentifier("Test")
                        .accountNumber(this.account.getAccountNumber())
                        .trades(List.of(
                                generateTestCreateUpdateTradeDTO(generateTestBuyTrade()),
                                generateTestCreateUpdateTradeDTO(generateTestSellTrade())
                        ))
                        .transactions(List.of(
                                generateTestCreateUpdateTransactionDTO(generateTestTransactionDeposit(this.account)),
                                generateTestCreateUpdateTransactionDTO(generateTestTransactionWithdrawal(this.account))
                        ))
                        .build();

        assertThat(this.accountService.updateAccountTradingData(data)).isTrue();
        assertThat(this.tradeRepository.count()).isEqualTo(2);
        assertThat(this.transactionRepository.count()).isEqualTo(2);
    }


    //  ----------------- invalidateStaleAccounts -----------------

    @Test
    void test_invalidateStaleAccounts_success() {
        this.accountRepository.deleteAll();

        Account acc1 = generateTestAccount();
        acc1.setId(null);
        acc1.setAccountNumber(1111L);
        acc1.setLastTraded(LocalDateTime.now().minusMonths(1));
        acc1 = this.accountRepository.save(acc1);

        Account acc2 = generateTestAccount();
        acc2.setId(null);
        acc2.setAccountNumber(2222L);
        acc2.setLastTraded(LocalDateTime.now().minusYears(4));
        acc2 = this.accountRepository.save(acc2);

        Account acc3 = generateTestAccount();
        acc3.setId(null);
        acc3.setAccountNumber(3333L);
        acc3.setLastTraded(null);
        acc3 = this.accountRepository.save(acc3);

        Account acc4 = generateTestAccount();
        acc4.setId(null);
        acc4.setAccountNumber(4444L);
        acc4.setLastTraded(LocalDateTime.now().minusYears(2));
        acc4 = this.accountRepository.save(acc4);

        int count = this.accountService.invalidateStaleAccounts();
        assertThat(count).isEqualTo(2);

        assertThat(this.accountRepository.findById(acc1.getId()).get().isDefaultAccount()).isTrue();
        assertThat(this.accountRepository.findById(acc2.getId()).get().isDefaultAccount()).isFalse();
        assertThat(this.accountRepository.findById(acc3.getId()).get().isDefaultAccount()).isTrue();
        assertThat(this.accountRepository.findById(acc4.getId()).get().isDefaultAccount()).isFalse();

        assertThat(this.accountRepository.findById(acc1.getId()).get().isActive()).isTrue();
        assertThat(this.accountRepository.findById(acc2.getId()).get().isActive()).isFalse();
        assertThat(this.accountRepository.findById(acc3.getId()).get().isActive()).isTrue();
        assertThat(this.accountRepository.findById(acc4.getId()).get().isActive()).isFalse();
    }
}
