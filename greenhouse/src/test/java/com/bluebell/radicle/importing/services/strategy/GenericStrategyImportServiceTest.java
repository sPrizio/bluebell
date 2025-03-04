package com.bluebell.radicle.importing.services.strategy;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyChar;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.radicle.AbstractGenericTest;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Testing class for {@link GenericStrategyImportService}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenericStrategyImportServiceTest extends AbstractGenericTest {

    @Autowired
    private GenericStrategyImportService genericStrategyImportService;

    @MockBean
    private MetaTrader4StrategyImportService metaTrader4StrategyImportService;

    @BeforeEach
    public void setUp() {
        Mockito.doNothing().when(this.metaTrader4StrategyImportService).importTrades((InputStream) any(), anyChar(), any());
    }


    //  ----------------- importReport -----------------

    @Test
    void test_importTrades_success() {

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.genericStrategyImportService.importReport(null, null))
                .withMessageContaining(CorePlatformConstants.Validation.Trade.IMPORT_STREAM_CANNOT_BE_NULL);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.genericStrategyImportService.importReport(InputStream.nullInputStream(), null))
                .withMessageContaining(CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        final Account account = generateTestAccount();
        account.setTradePlatform(TradePlatform.BLUEBELL);
        assertThat(this.genericStrategyImportService.importReport(InputStream.nullInputStream(), account))
                .isEqualTo(StringUtils.EMPTY);

        account.setTradePlatform(TradePlatform.METATRADER4);
        assertThat(this.genericStrategyImportService.importReport(InputStream.nullInputStream(), account))
                .isEqualTo(StringUtils.EMPTY);

        account.setTradePlatform(TradePlatform.UNDEFINED);
        assertThat(this.genericStrategyImportService.importReport(InputStream.nullInputStream(), account))
                .isEqualTo("Strategy platform UNDEFINED is not currently supported");
    }
}
