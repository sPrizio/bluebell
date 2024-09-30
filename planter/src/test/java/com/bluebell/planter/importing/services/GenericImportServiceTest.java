package com.bluebell.planter.importing.services;

import com.bluebell.planter.AbstractGenericTest;
import com.bluebell.planter.core.enums.trade.platform.TradePlatform;
import com.bluebell.planter.core.exceptions.validation.IllegalParameterException;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.importing.exceptions.TradeImportFailureException;
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
 * Testing class for {@link GenericImportService}
 *
 * @author Stephen Prizio
 * @version 0.0.3
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GenericImportServiceTest extends AbstractGenericTest {

    private final MockMultipartFile TEST_FILE = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

    private final Account BAD_ACCOUNT = generateTestAccount();

    @MockBean
    private CMCMarketsTradesImportService cmcMarketsTradesImportService;

    @MockBean
    private MetaTrader4TradesImportService metaTrader4TradesImportService;

    private GenericImportService genericImportService;


    @Before
    public void setUp() throws Exception {
        BAD_ACCOUNT.setAccountNumber(-1);
        this.genericImportService = new GenericImportService(this.cmcMarketsTradesImportService, metaTrader4TradesImportService);
        Mockito.doNothing().when(this.cmcMarketsTradesImportService).importTrades(TEST_FILE.getInputStream(), ',', generateTestAccount());
        Mockito.doThrow(new TradeImportFailureException("Test Exception")).when(this.cmcMarketsTradesImportService).importTrades(TEST_FILE.getInputStream(), '|', generateTestAccount());
        Mockito.doThrow(new TradeImportFailureException("Test Exception")).when(this.cmcMarketsTradesImportService).importTrades(TEST_FILE.getInputStream(), ',', BAD_ACCOUNT);
    }

    @Test
    public void test_importTrades_missingParamFile() {
        final Account account = generateTestAccount();
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.genericImportService.importTrades(null, account))
                .withMessage("import stream cannot be null");
    }

    @Test
    public void test_importTrades_success_cmc() throws Exception {
        assertThat(this.genericImportService.importTrades(TEST_FILE.getInputStream(), generateTestAccount()))
                .isEmpty();
    }

    @Test
    public void test_importTrades_success_mt4() throws Exception {
        final Account account = generateTestAccount();
        account.setTradePlatform(TradePlatform.METATRADER4);
        assertThat(this.genericImportService.importTrades(TEST_FILE.getInputStream(), account))
                .isEmpty();
    }

    @Test
    public void test_importTrades_success_unknown() throws Exception {
        final Account account = generateTestAccount();
        account.setTradePlatform(TradePlatform.UNDEFINED);
        assertThat(this.genericImportService.importTrades(TEST_FILE.getInputStream(), account))
                .isEqualTo("Trading platform UNDEFINED is not currently supported");
    }
}
