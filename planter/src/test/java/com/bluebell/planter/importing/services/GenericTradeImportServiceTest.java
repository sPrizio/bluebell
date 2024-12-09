package com.bluebell.planter.importing.services;

import com.bluebell.planter.AbstractGenericTest;
import com.bluebell.planter.core.enums.trade.platform.TradePlatform;
import com.bluebell.planter.core.exceptions.validation.IllegalParameterException;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.importing.exceptions.TradeImportFailureException;
import com.bluebell.planter.importing.services.trade.CMCMarketsTradeImportService;
import com.bluebell.planter.importing.services.trade.GenericTradeImportService;
import com.bluebell.planter.importing.services.trade.MetaTrader4TradeImportService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link GenericTradeImportService}
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GenericTradeImportServiceTest extends AbstractGenericTest {

    private final MockMultipartFile TEST_FILE = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

    private final Account BAD_ACCOUNT = generateTestAccount();

    @MockBean
    private CMCMarketsTradeImportService cmcMarketsTradeImportService;

    @MockBean
    private MetaTrader4TradeImportService metaTrader4TradeImportService;

    private GenericTradeImportService genericTradeImportService;


    @Before
    public void setUp() throws Exception {
        BAD_ACCOUNT.setAccountNumber(-1);
        this.genericTradeImportService = new GenericTradeImportService(this.cmcMarketsTradeImportService, metaTrader4TradeImportService);
        Mockito.doNothing().when(this.cmcMarketsTradeImportService).importTrades(TEST_FILE.getInputStream(), ',', generateTestAccount());
        Mockito.doThrow(new TradeImportFailureException("Test Exception")).when(this.cmcMarketsTradeImportService).importTrades(TEST_FILE.getInputStream(), '|', generateTestAccount());
        Mockito.doThrow(new TradeImportFailureException("Test Exception")).when(this.cmcMarketsTradeImportService).importTrades(TEST_FILE.getInputStream(), ',', BAD_ACCOUNT);
    }

    @Test
    public void test_importTrades_missingParamFile() {
        final Account account = generateTestAccount();
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.genericTradeImportService.importTrades(null, account))
                .withMessage("import stream cannot be null");
    }

    @Test
    public void test_importTrades_success_cmc() throws Exception {
        assertThat(this.genericTradeImportService.importTrades(TEST_FILE.getInputStream(), generateTestAccount()))
                .isEmpty();
    }

    @Test
    public void test_importTrades_success_mt4() throws Exception {
        final Account account = generateTestAccount();
        account.setTradePlatform(TradePlatform.METATRADER4);
        assertThat(this.genericTradeImportService.importTrades(TEST_FILE.getInputStream(), account))
                .isEmpty();
    }

    @Test
    public void test_importTrades_success_unknown() throws Exception {
        final Account account = generateTestAccount();
        account.setTradePlatform(TradePlatform.UNDEFINED);
        assertThat(this.genericTradeImportService.importTrades(TEST_FILE.getInputStream(), account))
                .isEqualTo("Trading platform UNDEFINED is not currently supported");
    }
}
