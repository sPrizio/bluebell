package com.bluebell.radicle.importing.services.trade;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.radicle.importing.exceptions.TradeImportFailureException;
import com.bluebell.radicle.repositories.account.AccountRepository;
import com.bluebell.radicle.repositories.portfolio.PortfolioRepository;
import com.bluebell.radicle.repositories.security.UserRepository;
import com.bluebell.radicle.repositories.trade.TradeRepository;
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
 * Testing class for {@link CMCMarketsTradeImportService}
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CMCMarketsTradeImportServiceTest extends AbstractGenericTest {

    private User user;

    private Portfolio portfolio;

    private Account account;

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "cmcMarketsTradeImportService")
    private CMCMarketsTradeImportService cmcMarketsTradeImportService;

    @Resource(name = "portfolioRepository")
    private PortfolioRepository portfolioRepository;

    @Resource(name = "tradeRepository")
    private TradeRepository tradeRepository;

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
                .isThrownBy(() -> this.cmcMarketsTradeImportService.importTrades("src/main/resources/testing/NotFound.csv", ';', this.account))
                .withMessageContaining("The import process failed with reason");
    }

    @Test
    @Order(2)
    @Transactional
    @WithMockUser(username = "test")
    void test_importTrades_success() {

        this.cmcMarketsTradeImportService.importTrades("classpath:testing/History.csv", ';', this.account);

        assertThat(this.tradeRepository.findAll())
                .hasSize(3)
                .extracting("tradeId", "lotSize", "tradeOpenTime", "tradeCloseTime", "openPrice", "closePrice", "netProfit")
                .contains(
                        Tuple.tuple("O5-77-5H7P05", 0.80, LocalDateTime.of(2022, 8, 24, 11, 23), LocalDateTime.of(2022, 8, 24, 11, 27), 12960.00, 12972.38, 12.78),
                        Tuple.tuple("O5-77-5H7MXX", 0.75, LocalDateTime.of(2022, 8, 24, 11, 13), LocalDateTime.of(2022, 8, 24, 11, 14), 12935.17, 12943.36, -8.0),
                        Tuple.tuple("1109841303", 0.0, LocalDateTime.of(2022, 8, 24, 11, 14), LocalDateTime.of(2022, 8, 24, 11, 14), 0.0, 0.0, 8.0)
                );
    }

    @Test
    @Order(3)
    @Transactional
    @WithMockUser(username = "test")
    void testImportTrades_success_unchanged() {

        this.cmcMarketsTradeImportService.importTrades("classpath:testing/History.csv", ';', this.account);
        this.cmcMarketsTradeImportService.importTrades("classpath:testing/History.csv", ';', this.account);

        assertThat(this.tradeRepository.findAll())
                .hasSize(3);
    }

    @Test
    @Order(4)
    @Transactional
    @WithMockUser(username = "test")
    void test_importTrades_success_inputStream() throws Exception {

        this.cmcMarketsTradeImportService.importTrades(new FileInputStream(ResourceUtils.getFile("classpath:testing/History.csv")), ';', this.account);

        assertThat(this.tradeRepository.findAll())
                .hasSize(3)
                .extracting("tradeId", "lotSize", "tradeOpenTime", "tradeCloseTime", "openPrice", "closePrice", "netProfit")
                .contains(
                        Tuple.tuple("O5-77-5H7P05", 0.80, LocalDateTime.of(2022, 8, 24, 11, 23), LocalDateTime.of(2022, 8, 24, 11, 27), 12960.00, 12972.38, 12.78),
                        Tuple.tuple("O5-77-5H7MXX", 0.75, LocalDateTime.of(2022, 8, 24, 11, 13), LocalDateTime.of(2022, 8, 24, 11, 14), 12935.17, 12943.36, -8.0),
                        Tuple.tuple("1109841303", 0.0, LocalDateTime.of(2022, 8, 24, 11, 14), LocalDateTime.of(2022, 8, 24, 11, 14), 0.0, 0.0, 8.0)
                );
    }

    @Test
    @Order(5)
    @Transactional
    void test_importTrades_dateFailure() {
        assertThatExceptionOfType(TradeImportFailureException.class)
                .isThrownBy(() -> this.cmcMarketsTradeImportService.importTrades("classpath:testing/History_variable.csv", ';', this.account))
                .withMessageContaining("The import process failed with reason");
    }

    @Test
    @Order(6)
    @Transactional
    void test_importTrades_badData() {
        assertThatExceptionOfType(TradeImportFailureException.class)
                .isThrownBy(() -> this.cmcMarketsTradeImportService.importTrades("classpath:testing/History_bad.csv", ';', this.account))
                .withMessageContaining("The import process failed with reason");
    }
}
