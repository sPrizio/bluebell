package com.bluebell.radicle.importing.services.strategy;

import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.radicle.AbstractGenericTest;
import com.bluebell.radicle.importing.exceptions.TradeImportFailureException;
import com.bluebell.radicle.repositories.account.AccountRepository;
import com.bluebell.radicle.repositories.security.UserRepository;
import com.bluebell.radicle.repositories.trade.TradeRepository;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class {@link MetaTrader4StrategyImportService}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MetaTrader4StrategyImportServiceTest extends AbstractGenericTest {

    private User user;

    private Account account;

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "metaTrader4StrategyImportService")
    private MetaTrader4StrategyImportService metaTrader4StrategyImportService;

    @Resource(name = "tradeRepository")
    private TradeRepository tradeRepository;

    @Resource(name = "userRepository")
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        account = this.accountRepository.save(generateTestAccount());
        user = generateTestUser();
        user.setAccounts(List.of(account));
        user = this.userRepository.save(user);
    }

    @AfterEach
    public void tearDown() {
        this.accountRepository.deleteAll();
        this.userRepository.deleteAll();
        this.tradeRepository.deleteAll();
    }


    //  METHODS

    @Test
    @Order(1)
    @Transactional
    void test_importTrades_failure() {
        assertThatExceptionOfType(TradeImportFailureException.class)
                .isThrownBy(() -> this.metaTrader4StrategyImportService.importTrades("src/main/resources/testing/NotFound.htm", ';', this.account))
                .withMessageContaining("The import process failed with reason");
    }

    @Test
    @Order(2)
    @Transactional
    @WithMockUser(username = "test")
    void test_importTrades_success() {

        this.metaTrader4StrategyImportService.importTrades("classpath:testing/StrategyTester.htm", ';', this.account);

        assertThat(this.tradeRepository.findAll())
                .hasSize(58)
                .extracting("tradeId", "lotSize", "tradeOpenTime", "tradeCloseTime", "openPrice", "closePrice", "netProfit")
                .contains(
                        Tuple.tuple("10", 0.25, LocalDateTime.of(2025, 1, 8, 15, 54, 0), LocalDateTime.of(2025, 1, 8, 16, 0, 0), 21178.37, 21179.38, 5.05),
                        Tuple.tuple("54", 0.25, LocalDateTime.of(2025, 2, 19, 10, 2, 0), LocalDateTime.of(2025, 2, 19, 11, 11, 0), 22078.01, 22143.43, -327.10),
                        Tuple.tuple("14", 0.25, LocalDateTime.of(2025, 1, 14, 14, 49, 0), LocalDateTime.of(2025, 1, 14, 14, 59, 0), 20808.98, 20706.93, 510.25)
                );
    }

    @Test
    @Order(3)
    @Transactional
    @WithMockUser(username = "test")
    void testImportTrades_success_unchanged() {

        this.metaTrader4StrategyImportService.importTrades("classpath:testing/StrategyTester.htm", ';', this.account);
        this.metaTrader4StrategyImportService.importTrades("classpath:testing/StrategyTester.htm", ';', this.account);

        assertThat(this.tradeRepository.findAll())
                .hasSize(58);
    }

    @Test
    @Order(4)
    @Transactional
    @WithMockUser(username = "test")
    void test_importTrades_success_inputStream() throws Exception {

        this.metaTrader4StrategyImportService.importTrades(new FileInputStream(ResourceUtils.getFile("classpath:testing/StrategyTester.htm")), ';', this.account);

        assertThat(this.tradeRepository.findAll())
                .hasSize(58)
                .extracting("tradeId", "lotSize", "tradeOpenTime", "tradeCloseTime", "openPrice", "closePrice", "netProfit")
                .contains(
                        Tuple.tuple("10", 0.25, LocalDateTime.of(2025, 1, 8, 15, 54, 0), LocalDateTime.of(2025, 1, 8, 16, 0, 0), 21178.37, 21179.38, 5.05),
                        Tuple.tuple("54", 0.25, LocalDateTime.of(2025, 2, 19, 10, 2, 0), LocalDateTime.of(2025, 2, 19, 11, 11, 0), 22078.01, 22143.43, -327.10),
                        Tuple.tuple("14", 0.25, LocalDateTime.of(2025, 1, 14, 14, 49, 0), LocalDateTime.of(2025, 1, 14, 14, 59, 0), 20808.98, 20706.93, 510.25)
                );
    }
}
