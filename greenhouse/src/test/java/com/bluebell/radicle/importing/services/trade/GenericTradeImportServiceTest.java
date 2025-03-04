package com.bluebell.radicle.importing.services.trade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.AbstractGenericTest;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.importing.exceptions.TradeImportFailureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Testing class for {@link GenericTradeImportService}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class GenericTradeImportServiceTest extends AbstractGenericTest {

    private final MockMultipartFile TEST_FILE = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

    private final Account BAD_ACCOUNT = generateTestAccount();

    @MockitoBean
    private CMCMarketsTradeImportService cmcMarketsTradeImportService;

    @MockitoBean
    private MetaTrader4TradeImportService metaTrader4TradeImportService;

    private GenericTradeImportService genericTradeImportService;


    @BeforeEach
    void setUp() throws Exception {
        BAD_ACCOUNT.setAccountNumber(-1);
        this.genericTradeImportService = new GenericTradeImportService(this.cmcMarketsTradeImportService, metaTrader4TradeImportService);
        Mockito.doNothing().when(this.cmcMarketsTradeImportService).importTrades(TEST_FILE.getInputStream(), ',', generateTestAccount());
        Mockito.doThrow(new TradeImportFailureException("Test Exception")).when(this.cmcMarketsTradeImportService).importTrades(TEST_FILE.getInputStream(), '|', generateTestAccount());
        Mockito.doThrow(new TradeImportFailureException("Test Exception")).when(this.cmcMarketsTradeImportService).importTrades(TEST_FILE.getInputStream(), ',', BAD_ACCOUNT);
    }

    @Test
    void test_importTrades_missingParamFile() {
        final Account account = generateTestAccount();
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.genericTradeImportService.importTrades(null, account))
                .withMessage("import stream cannot be null");
    }

    @Test
    void test_importTrades_success_cmc() throws Exception {
        assertThat(this.genericTradeImportService.importTrades(TEST_FILE.getInputStream(), generateTestAccount()))
                .isEmpty();
    }

    @Test
    void test_importTrades_success_mt4() throws Exception {
        final Account account = generateTestAccount();
        account.setTradePlatform(TradePlatform.METATRADER4);
        assertThat(this.genericTradeImportService.importTrades(TEST_FILE.getInputStream(), account))
                .isEmpty();
    }

    @Test
    void test_importTrades_success_unknown() throws Exception {
        final Account account = generateTestAccount();
        account.setTradePlatform(TradePlatform.UNDEFINED);
        assertThat(this.genericTradeImportService.importTrades(TEST_FILE.getInputStream(), account))
                .isEqualTo("Trading platform UNDEFINED is not currently supported");
    }
}
