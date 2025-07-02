package com.bluebell.radicle.services.account;

import com.bluebell.AbstractGenericTest;
import com.bluebell.configuration.BluebellTestConfiguration;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.api.dto.account.CreateUpdateAccountTradingDataDTO;
import com.bluebell.platform.models.api.dto.trade.CreateUpdateTradeDTO;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.repositories.account.AccountRepository;
import com.bluebell.radicle.repositories.portfolio.PortfolioRepository;
import com.bluebell.radicle.repositories.trade.TradeRepository;
import com.bluebell.radicle.repositories.transaction.TransactionRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing integrations for {@link AccountService}
 *
 * @author Stephen Prizio
 * @version 1.0.0
 */
@Import(BluebellTestConfiguration.class)
@SpringBootTest
@RunWith(SpringRunner.class)
class AccountServiceIntegrationTest extends AbstractGenericTest {

    private Account account;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        this.jdbcTemplate.execute("TRUNCATE TABLE accounts RESTART IDENTITY CASCADE");
        final Account acc = generateTestAccount();
        acc.setId(null);
        this.account = this.accountRepository.save(acc);
    }

    @AfterEach
    void tearDown() {
        this.accountRepository.deleteAll();
        this.tradeRepository.deleteAll();
    }


    //  ----------------- refreshAccount -----------------

    @Test
    void test_refreshAccount_success_noChange() {
        Account acc = generateTestAccount();
        acc.setId(null);
        acc.setAccountNumber(1111L);
        acc = this.accountRepository.save(acc);

        assertThat(acc.getBalance()).isEqualTo(1000.0);

        acc = this.accountService.refreshAccount(acc);

        assertThat(acc.getBalance()).isEqualTo(1000.0);
    }

    @Test
    void test_refreshAccount_success() {
        Account acc = generateTestAccount();
        acc.setId(null);
        acc.setAccountNumber(1111L);

        assertThat(acc.getBalance()).isEqualTo(1000.0);

        acc = this.accountRepository.save(acc.refreshAccount());

        assertThat(CollectionUtils.isEmpty(acc.getTrades())).isTrue();

        Trade trade1 = generateTestBuyTrade();
        Trade trade2 = generateTestSellTrade();
        trade1.setAccount(acc);
        trade2.setAccount(acc);

        this.tradeRepository.saveAll(List.of(trade1, trade2));

        acc.setTrades(new ArrayList<>(List.of(trade1, trade2)));
        acc = this.accountService.refreshAccount(acc);

        assertThat(acc.getTrades()).hasSize(2);
        assertThat(acc.getBalance()).isEqualTo(1010.35);
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


    //  ----------------- reassignAccounts -----------------

    @Test
    void test_reassignAccounts_success() {

        this.accountRepository.deleteAll();
        this.portfolioRepository.deleteAll();

        Portfolio portfolio = generateTestPortfolio();
        portfolio.setId(null);
        portfolio.setAccounts(null);
        portfolio = this.portfolioRepository.save(portfolio);

        Account acc1 = generateTestAccount();
        acc1.setId(null);
        acc1.setAccountNumber(1111L);
        acc1.setLastTraded(LocalDateTime.now().minusMonths(1));
        acc1.setDefaultAccount(false);
        acc1.setPortfolio(portfolio);
        acc1 = this.accountRepository.save(acc1);

        Account acc2 = generateTestAccount();
        acc2.setId(null);
        acc2.setAccountNumber(2222L);
        acc2.setLastTraded(LocalDateTime.now().minusYears(4));
        acc2.setDefaultAccount(false);
        acc2.setPortfolio(portfolio);
        acc2 = this.accountRepository.save(acc2);

        Account acc3 = generateTestAccount();
        acc3.setId(null);
        acc3.setAccountNumber(3333L);
        acc3.setLastTraded(null);
        acc3.setDefaultAccount(false);
        acc3.setPortfolio(portfolio);
        acc3 = this.accountRepository.save(acc3);

        Account acc4 = generateTestAccount();
        acc4.setId(null);
        acc4.setAccountNumber(4444L);
        acc4.setLastTraded(LocalDateTime.now().minusYears(2));
        acc4.setDefaultAccount(false);
        acc4.setPortfolio(portfolio);
        acc4 = this.accountRepository.save(acc4);

        portfolio.setAccounts(List.of(acc1, acc2, acc3, acc4));
        portfolio = this.portfolioRepository.save(portfolio);

        this.accountService.reassignAccounts(portfolio.getPortfolioNumber());
        assertThat(this.accountRepository.findAccountByAccountNumber(acc1.getAccountNumber()).isDefaultAccount()).isTrue();

        this.accountService.deleteAccount(acc1);
        this.accountService.deleteAccount(acc3);

        this.accountService.reassignAccounts(portfolio.getPortfolioNumber());

        assertThat(this.accountRepository.findAccountByAccountNumber(acc2.getAccountNumber()).isDefaultAccount()).isTrue();
    }
}
