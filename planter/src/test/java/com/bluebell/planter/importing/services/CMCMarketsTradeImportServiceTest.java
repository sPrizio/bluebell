package com.bluebell.planter.importing.services;

import com.bluebell.planter.AbstractGenericTest;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.entities.security.User;
import com.bluebell.planter.core.repositories.account.AccountRepository;
import com.bluebell.planter.core.repositories.security.UserRepository;
import com.bluebell.planter.core.repositories.trade.TradeRepository;
import com.bluebell.planter.importing.exceptions.TradeImportFailureException;
import com.bluebell.planter.importing.services.trade.CMCMarketsTradeImportService;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.assertj.core.groups.Tuple;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
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
 * Testing class for {@link CMCMarketsTradeImportService}
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CMCMarketsTradeImportServiceTest extends AbstractGenericTest {

    private User user;
    private Account account;

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "cmcMarketsTradeImportService")
    private CMCMarketsTradeImportService cmcMarketsTradeImportService;

    @Resource(name = "tradeRepository")
    private TradeRepository tradeRepository;

    @Resource(name = "userRepository")
    private UserRepository userRepository;

    @Before
    public void setUp() {
        account = this.accountRepository.save(generateTestAccount());
        user = generateTestUser();
        user.setAccounts(List.of(account));
        user = this.userRepository.save(user);
    }

    @After
    public void tearDown() {
        this.accountRepository.delete(account);
        account = null;

        this.userRepository.delete(user);
        user = null;
    }


    //  METHODS

    @Test
    @Order(1)
    @Transactional
    public void test_importTrades_failure() {
        assertThatExceptionOfType(TradeImportFailureException.class)
                .isThrownBy(() -> this.cmcMarketsTradeImportService.importTrades("src/main/resources/testing/NotFound.csv", ';', this.account))
                .withMessageContaining("The import process failed with reason");
    }

    @Test
    @Order(2)
    @Transactional
    @WithMockUser(username = "test")
    public void test_importTrades_success() {

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
    public void testImportTrades_success_unchanged() {

        this.cmcMarketsTradeImportService.importTrades("classpath:testing/History.csv", ';', this.account);
        this.cmcMarketsTradeImportService.importTrades("classpath:testing/History.csv", ';', this.account);

        assertThat(this.tradeRepository.findAll())
                .hasSize(3);
    }

    @Test
    @Order(4)
    @Transactional
    @WithMockUser(username = "test")
    public void test_importTrades_success_inputStream() throws Exception {

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
    public void test_importTrades_dateFailure() {
        assertThatExceptionOfType(TradeImportFailureException.class)
                .isThrownBy(() -> this.cmcMarketsTradeImportService.importTrades("classpath:testing/History_variable.csv", ';', this.account))
                .withMessageContaining("The import process failed with reason");
    }

    @Test
    @Order(6)
    @Transactional
    public void test_importTrades_badData() {
        assertThatExceptionOfType(TradeImportFailureException.class)
                .isThrownBy(() -> this.cmcMarketsTradeImportService.importTrades("classpath:testing/History_bad.csv", ';', this.account))
                .withMessageContaining("The import process failed with reason");
    }
}
