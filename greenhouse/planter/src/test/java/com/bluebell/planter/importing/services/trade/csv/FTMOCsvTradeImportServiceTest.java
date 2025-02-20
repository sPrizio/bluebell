package com.bluebell.planter.importing.services.trade.csv;

import com.bluebell.planter.AbstractGenericTest;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.entities.security.User;
import com.bluebell.planter.core.repositories.account.AccountRepository;
import com.bluebell.planter.core.repositories.security.UserRepository;
import com.bluebell.planter.core.repositories.trade.TradeRepository;
import com.bluebell.planter.importing.exceptions.TradeImportFailureException;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.assertj.core.groups.Tuple;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
 * Testing for {@link FTMOCsvTradeImportService}
 *
 * @author Stephen Prizio
 * @version 0.0.8
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FTMOCsvTradeImportServiceTest extends AbstractGenericTest {

    private User user;

    private Account account;

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "ftmoCSVTradeImportService")
    private FTMOCsvTradeImportService ftmoCsvTradeImportService;

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
                .isThrownBy(() -> this.ftmoCsvTradeImportService.importTrades("src/main/resources/testing/NotFound.htm", ';', this.account))
                .withMessageContaining("The import process failed with reason");
    }

    @Test
    @Order(2)
    @Transactional
    @WithMockUser(username = "test")
    public void test_importTrades_success() {

        this.ftmoCsvTradeImportService.importTrades("classpath:testing/csv_test.csv", ';', this.account);

        assertThat(this.tradeRepository.findAll())
                .hasSize(24)
                .extracting("tradeId", "lotSize", "tradeOpenTime", "tradeCloseTime", "openPrice", "closePrice", "netProfit")
                .contains(
                        Tuple.tuple("48202146", 3.0, LocalDateTime.of(2024, 8, 13, 18, 4, 14), LocalDateTime.of(2024, 8, 13, 19, 46, 48), 18854.15, 18939.05, 349.50),
                        Tuple.tuple("47899572", 3.0, LocalDateTime.of(2024, 8, 9, 18, 30, 44), LocalDateTime.of(2024, 8, 9, 20, 13, 13), 18408.45, 18473.75, -268.96),
                        Tuple.tuple("47456378", 0.5, LocalDateTime.of(2024, 8, 6, 17, 2, 51), LocalDateTime.of(2024, 8, 6, 17, 44, 45), 18065.55, 18149.35, 57.79)
                );
    }

    @Test
    @Order(3)
    @Transactional
    @WithMockUser(username = "test")
    public void testImportTrades_success_unchanged() {

        this.ftmoCsvTradeImportService.importTrades("classpath:testing/csv_test.csv", ';', this.account);
        this.ftmoCsvTradeImportService.importTrades("classpath:testing/csv_test.csv", ';', this.account);

        assertThat(this.tradeRepository.findAll())
                .hasSize(24);
    }

    @Test
    @Order(4)
    @Transactional
    @WithMockUser(username = "test")
    public void test_importTrades_success_inputStream() throws Exception {

        this.ftmoCsvTradeImportService.importTrades(new FileInputStream(ResourceUtils.getFile("classpath:testing/csv_test.csv")), ';', this.account);

        assertThat(this.tradeRepository.findAll())
                .hasSize(24)
                .extracting("tradeId", "lotSize", "tradeOpenTime", "tradeCloseTime", "openPrice", "closePrice", "netProfit")
                .contains(
                        Tuple.tuple("48202146", 3.0, LocalDateTime.of(2024, 8, 13, 18, 4, 14), LocalDateTime.of(2024, 8, 13, 19, 46, 48), 18854.15, 18939.05, 349.50),
                        Tuple.tuple("47899572", 3.0, LocalDateTime.of(2024, 8, 9, 18, 30, 44), LocalDateTime.of(2024, 8, 9, 20, 13, 13), 18408.45, 18473.75, -268.96),
                        Tuple.tuple("47456378", 0.5, LocalDateTime.of(2024, 8, 6, 17, 2, 51), LocalDateTime.of(2024, 8, 6, 17, 44, 45), 18065.55, 18149.35, 57.79)
                );
    }
}
