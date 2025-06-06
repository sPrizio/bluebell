package com.bluebell.radicle.importing.services.trade;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.radicle.importing.exceptions.TradeImportFailureException;
import com.bluebell.radicle.repositories.account.AccountRepository;
import com.bluebell.radicle.repositories.portfolio.PortfolioRepository;
import com.bluebell.radicle.repositories.security.UserRepository;
import com.bluebell.radicle.repositories.trade.TradeRepository;
import com.bluebell.radicle.repositories.transaction.TransactionRepository;
import jakarta.annotation.Resource;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link MetaTrader4TradeImportService}
 *
 * @author Stephen Prizio
 * @version 0.1.8
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MetaTrader4TradeImportServiceTest extends AbstractGenericTest {

    private User user;

    private Portfolio portfolio;

    private Account account;

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "metaTrader4TradeImportService")
    private MetaTrader4TradeImportService metaTrader4TradeImportService;

    @Resource(name = "portfolioRepository")
    private PortfolioRepository portfolioRepository;

    @Resource(name = "tradeRepository")
    private TradeRepository tradeRepository;

    @Resource(name = "transactionRepository")
    private TransactionRepository transactionRepository;

    @Resource(name = "userRepository")
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        final Account acc = generateTestAccount();
        acc.setId(null);
        account = this.accountRepository.save(acc);

        portfolio = generateTestPortfolio();
        portfolio.setId(null);
        portfolio.setAccounts(List.of(account));
        portfolio = this.portfolioRepository.save(portfolio);

        user = generateTestUser();
        user.setPortfolios(List.of(portfolio));
        user = this.userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        this.transactionRepository.deleteAll();
        this.tradeRepository.deleteAll();
        this.accountRepository.delete(account);
        account = null;

        this.portfolioRepository.delete(portfolio);
        portfolio = null;

        this.userRepository.delete(user);
        user = null;
    }


    //  METHODS

    @Test
    @Order(1)
    @Transactional
    void test_importTrades_failure() {
        assertThatExceptionOfType(TradeImportFailureException.class)
                .isThrownBy(() -> this.metaTrader4TradeImportService.importTrades("src/main/resources/testing/NotFound.htm", ';', this.account))
                .withMessageContaining("The import process failed with reason");
    }

    @Test
    @Order(2)
    @Transactional
    @WithMockUser(username = "test")
    void test_importTrades_success() {

        this.metaTrader4TradeImportService.importTrades("classpath:testing/Statement.htm", ';', this.account);

        assertThat(this.tradeRepository.findAll())
                .hasSize(41)
                .extracting("tradeId", "lotSize", "tradeOpenTime", "tradeCloseTime", "openPrice", "closePrice", "netProfit")
                .contains(
                        Tuple.tuple("62952792", 3.5, LocalDateTime.of(2023, 6, 8, 16, 49, 22), LocalDateTime.of(2023, 6, 8, 16, 51, 52), 14347.90, 14351.60, 17.31),
                        Tuple.tuple("62973223", 3.5, LocalDateTime.of(2023, 6, 8, 20, 2, 20), LocalDateTime.of(2023, 6, 8, 20, 6, 48), 14464.10, 14472.80, -40.67),
                        Tuple.tuple("62861228", 3.5, LocalDateTime.of(2023, 6, 7, 16, 47, 26), LocalDateTime.of(2023, 6, 7, 16, 49, 3), 14640.20, 14628.30, -55.79)
                );
    }

    @Test
    @Order(3)
    @Transactional
    @WithMockUser(username = "test")
    void testImportTrades_success_unchanged() {

        this.metaTrader4TradeImportService.importTrades("classpath:testing/Statement.htm", ';', this.account);
        this.metaTrader4TradeImportService.importTrades("classpath:testing/Statement.htm", ';', this.account);

        assertThat(this.tradeRepository.findAll())
                .hasSize(41);
    }

    @Test
    @Order(4)
    @Transactional
    @WithMockUser(username = "test")
    void test_importTrades_success_inputStream() throws Exception {

        this.metaTrader4TradeImportService.importTrades(new FileInputStream(ResourceUtils.getFile("classpath:testing/Statement.htm")), ';', this.account);

        assertThat(this.tradeRepository.findAll())
                .hasSize(41)
                .extracting("tradeId", "lotSize", "tradeOpenTime", "tradeCloseTime", "openPrice", "closePrice", "netProfit")
                .contains(
                        Tuple.tuple("62952792", 3.5, LocalDateTime.of(2023, 6, 8, 16, 49, 22), LocalDateTime.of(2023, 6, 8, 16, 51, 52), 14347.90, 14351.60, 17.31),
                        Tuple.tuple("62973223", 3.5, LocalDateTime.of(2023, 6, 8, 20, 2, 20), LocalDateTime.of(2023, 6, 8, 20, 6, 48), 14464.10, 14472.80, -40.67),
                        Tuple.tuple("62861228", 3.5, LocalDateTime.of(2023, 6, 7, 16, 47, 26), LocalDateTime.of(2023, 6, 7, 16, 49, 3), 14640.20, 14628.30, -55.79)
                );
    }

    @Test
    @Order(5)
    @Transactional
    @WithMockUser(username = "test")
    void test_importTrades_withTransactions_success() {

        this.metaTrader4TradeImportService.importTrades("classpath:testing/Statement_with_transactions.htm", ';', this.account);

        assertThat(this.transactionRepository.findAll())
                .hasSize(24)
                .extracting("transactionDate", "transactionType", "name", "amount")
                .contains(
                        Tuple.tuple(LocalDateTime.of(2024, 12, 9, 0, 55, 5), TransactionType.DEPOSIT, "Price Adjust", 7.46),
                        Tuple.tuple(LocalDateTime.of(2024, 12, 7, 0, 2, 23), TransactionType.WITHDRAWAL, "Withdrawal", -53.71),
                        Tuple.tuple(LocalDateTime.of(2024, 5, 3, 17, 7, 6), TransactionType.DEPOSIT, "Promo Payment", 1095.76)
                );
    }
}
