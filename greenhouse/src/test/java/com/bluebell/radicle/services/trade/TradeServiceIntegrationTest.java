package com.bluebell.radicle.services.trade;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.api.dto.trade.CreateUpdateMultipleTradesDTO;
import com.bluebell.platform.models.api.dto.trade.CreateUpdateTradeDTO;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.repositories.account.AccountRepository;
import com.bluebell.radicle.repositories.trade.TradeRepository;
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
 * Testing class for integrations within {@link TradeService}
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class TradeServiceIntegrationTest extends AbstractGenericTest {

    private Account account;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private TradeService tradeService;

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


    //  ----------------- createTrades -----------------

    @Test
    void test_createTrades_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.createTrades(null))
                .withMessage("trades dto cannot be null");
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.createTrades(CreateUpdateMultipleTradesDTO.builder().accountNumber(1L).build()))
                .withMessage(CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);
    }

    @Test
    void test_createTrades_failed() {
        final CreateUpdateMultipleTradesDTO badAccount =
                CreateUpdateMultipleTradesDTO
                        .builder()
                        .userIdentifier("Test")
                        .accountNumber(-1L)
                        .trades(List.of(CreateUpdateTradeDTO.builder().build()))
                        .build();

        final CreateUpdateMultipleTradesDTO noTrades =
                CreateUpdateMultipleTradesDTO
                        .builder()
                        .userIdentifier("Test")
                        .accountNumber(this.account.getId())
                        .build();

        assertThat(this.tradeService.createTrades(badAccount)).isFalse();
        assertThat(this.tradeService.createTrades(noTrades)).isFalse();
    }

    @Test
    void test_createTrades_success() {
        final CreateUpdateMultipleTradesDTO trades =
                CreateUpdateMultipleTradesDTO
                        .builder()
                        .userIdentifier("Test")
                        .accountNumber(this.account.getAccountNumber())
                        .trades(List.of(
                                generateTestCreateUpdateTradeDTO(generateTestBuyTrade()),
                                generateTestCreateUpdateTradeDTO(generateTestSellTrade())
                        ))
                        .build();

        assertThat(this.tradeService.createTrades(trades)).isTrue();
        assertThat(this.tradeRepository.count()).isEqualTo(2);
    }


    //  ----------------- saveAll -----------------

    @Test
    void test_saveAll_success() {

        final Trade trade1 = generateTestBuyTrade();
        final Trade trade2 = generateTestSellTrade();

        assertThat(this.tradeService.saveAll(null, generateTestAccount())).isEqualTo(-1);
        assertThat(this.tradeService.saveAll(List.of(generateTestBuyTrade()), null)).isEqualTo(-1);

        assertThat(this.tradeService.saveAll(List.of(trade1, trade2), this.account)).isEqualTo(2);
        assertThat(this.tradeService.saveAll(List.of(trade1, trade2), this.account)).isZero();

        trade1.setNetProfit(-1.0);
        trade1.setOpenPrice(23.45);

        assertThat(this.tradeService.saveAll(List.of(trade1, trade2), this.account)).isEqualTo(2);
    }
}
