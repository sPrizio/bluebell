package com.bluebell.flowerpot.importing.services;

import com.bluebell.flowerpot.AbstractGenericTest;
import com.bluebell.flowerpot.core.models.entities.account.Account;
import com.bluebell.flowerpot.core.models.entities.security.User;
import com.bluebell.flowerpot.core.repositories.account.AccountRepository;
import com.bluebell.flowerpot.core.repositories.security.UserRepository;
import com.bluebell.flowerpot.core.repositories.trade.TradeRepository;
import com.bluebell.flowerpot.importing.exceptions.TradeImportFailureException;
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
 * Testing class for {@link MetaTrader4TradesImportService}
 *
 * @author Stephen Prizio
 * @version 0.0.3
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MetaTrader4TradesImportServiceTest extends AbstractGenericTest {

    private User user;

    private Account account;

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "metaTrader4TradesImportService")
    private MetaTrader4TradesImportService metaTrader4TradesImportService;

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
                .isThrownBy(() -> this.metaTrader4TradesImportService.importTrades("src/main/resources/testing/NotFound.htm", ';', this.account))
                .withMessageContaining("The import process failed with reason");
    }

    @Test
    @Order(2)
    @Transactional
    @WithMockUser(username = "test")
    public void test_importTrades_success() {

        this.metaTrader4TradesImportService.importTrades("classpath:testing/Statement.htm", ';', this.account);

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
    public void testImportTrades_success_unchanged() {

        this.metaTrader4TradesImportService.importTrades("classpath:testing/Statement.htm", ';', this.account);
        this.metaTrader4TradesImportService.importTrades("classpath:testing/Statement.htm", ';', this.account);

        assertThat(this.tradeRepository.findAll())
                .hasSize(41);
    }

    @Test
    @Order(4)
    @Transactional
    @WithMockUser(username = "test")
    public void test_importTrades_success_inputStream() throws Exception {

        this.metaTrader4TradesImportService.importTrades(new FileInputStream(ResourceUtils.getFile("classpath:testing/Statement.htm")), ';', this.account);

        assertThat(this.tradeRepository.findAll())
                .hasSize(41)
                .extracting("tradeId", "lotSize", "tradeOpenTime", "tradeCloseTime", "openPrice", "closePrice", "netProfit")
                .contains(
                        Tuple.tuple("62952792", 3.5, LocalDateTime.of(2023, 6, 8, 16, 49, 22), LocalDateTime.of(2023, 6, 8, 16, 51, 52), 14347.90, 14351.60, 17.31),
                        Tuple.tuple("62973223", 3.5, LocalDateTime.of(2023, 6, 8, 20, 2, 20), LocalDateTime.of(2023, 6, 8, 20, 6, 48), 14464.10, 14472.80, -40.67),
                        Tuple.tuple("62861228", 3.5, LocalDateTime.of(2023, 6, 7, 16, 47, 26), LocalDateTime.of(2023, 6, 7, 16, 49, 3), 14640.20, 14628.30, -55.79)
                );
    }
}
