package com.bluebell.radicle.services.symbol;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link SymbolService}
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class SymbolServiceTest extends AbstractGenericTest {

    @Autowired
    private SymbolService symbolService;


    //  ----------------- getTradedSymbolsForAccount -----------------

    @Test
    void test_getTradedSymbolsForAccount_badData() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.symbolService.getTradedSymbolsForAccount(null))
                .withMessage(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    void test_getTradedSymbolsForAccount_success() {
        final Account account = generateTestAccount();
        final Trade trade1 = generateTestBuyTrade();
        final Trade trade2 = generateTestSellTrade();
        final Trade trade3 = generateTestBuyTrade();
        trade3.setProduct("Another one");

        account.setTrades(List.of(trade1, trade2, trade3));
        assertThat(this.symbolService.getTradedSymbolsForAccount(account)).hasSize(2);
    }
}
