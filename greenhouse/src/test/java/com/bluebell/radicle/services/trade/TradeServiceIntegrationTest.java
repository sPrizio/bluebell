package com.bluebell.radicle.services.trade;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.trade.Trade;
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
        assertThat(this.tradeRepository.count()).isEqualTo(2);
    }
}
