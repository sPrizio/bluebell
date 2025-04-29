package com.bluebell.radicle.importing.services.trade;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.radicle.importing.ImportService;
import com.bluebell.radicle.importing.exceptions.TradeImportFailureException;
import com.bluebell.radicle.importing.models.wrapper.trade.MetaTrader4TradeWrapper;
import com.bluebell.radicle.importing.models.wrapper.transaction.MetaTrader4TransactionWrapper;
import com.bluebell.radicle.importing.services.AbstractImportService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Service-layer for importing trades into the system from the MetaTrader4 platform
 *
 * @author Stephen Prizio
 * @version 0.1.8
 */
@Service("metaTrader4TradeImportService")
public class MetaTrader4TradeImportService extends AbstractImportService implements ImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetaTrader4TradeImportService.class);


    //  METHODS

    /**
     * Imports trades from a CSV file from the MT4 platform
     *
     * @param filePath  file path
     * @param delimiter unit delimiter
     */
    @Override
    public void importTrades(final String filePath, final Character delimiter, final Account account) {
        try {
            importFile(new BufferedReader(new FileReader(ResourceUtils.getFile(filePath))), account);
        } catch (Exception e) {
            LOGGER.error("The import process failed with reason : {}", e.getMessage(), e);
            throw new TradeImportFailureException(String.format("The import process failed with reason : %s", e.getMessage()), e);
        }
    }

    /**
     * Imports trades from a CSV file from the MT4 platform
     *
     * @param inputStream {@link InputStream}
     * @param delimiter   unit delimiter
     */
    @Override
    public void importTrades(final InputStream inputStream, final Character delimiter, final Account account) {
        importFile(new BufferedReader(new InputStreamReader(inputStream)), account);
    }


    //  HELPERS

    /**
     * Imports a file using the given {@link BufferedReader} and delimiter
     *
     * @param bufferedReader {@link BufferedReader}
     */
    private void importFile(final BufferedReader bufferedReader, final Account account) {

        try (bufferedReader) {
            final StringBuilder stringBuilder = new StringBuilder();
            bufferedReader.lines().forEach(stringBuilder::append);

            final List<String> content = getContent(stringBuilder.toString());
            List<MetaTrader4TradeWrapper> trades =
                    content
                            .stream()
                            .map(this::generateWrapper)
                            .filter(Objects::nonNull)
                            .sorted(Comparator.comparing(MetaTrader4TradeWrapper::getOpenTime))
                            .toList();

            List<MetaTrader4TransactionWrapper> transactions =
                    content
                            .stream()
                            .map(this::generateTransactionWrapper)
                            .filter(Objects::nonNull)
                            .sorted(Comparator.comparing(MetaTrader4TransactionWrapper::getDateTime))
                            .toList();

            mt4TradeCleanup(account, trades);
            mt4TransactionCleanup(account, transactions);
        } catch (Exception e) {
            LOGGER.error("The import process failed with reason : {}", e.getMessage(), e);
            throw new TradeImportFailureException(String.format("The import process failed with reason : %s", e.getMessage()), e);
        }
    }

    /**
     * Obtains trade content from the file content
     *
     * @param string file content
     * @return {@link List} of trade content strings
     */
    private List<String> getContent(final String string) {

        final int ticketIndex = string.indexOf("Ticket");
        if (ticketIndex == -1) {
            throw new TradeImportFailureException("No valid trades were given to import");
        }

        final int startIndex = string.indexOf("<tr", ticketIndex);
        if (startIndex == -1) {
            throw new TradeImportFailureException("The import file is not properly formatted");
        }

        final String tradeContent = string.substring(startIndex, string.indexOf("Closed P/L:"));
        return parseMetaTrader4TradeList(tradeContent);
    }

    /**
     * Generates a {@link MetaTrader4TradeWrapper} from the given string
     *
     * @param string input value
     * @return {@link MetaTrader4TradeWrapper}
     */
    private MetaTrader4TradeWrapper generateWrapper(final String string) {

        if (StringUtils.isEmpty(string)) {
            return null;
        }

        final List<String> data = parseMetaTrader4RowEntry(string);
        if (data.size() != 14) {
            return null;
        }

        return MetaTrader4TradeWrapper
                .builder()
                .ticketNumber(data.get(0))
                .openTime(LocalDateTime.parse(data.get(1), DateTimeFormatter.ofPattern(CorePlatformConstants.MT4_DATE_TIME_FORMAT)))
                .closeTime(LocalDateTime.parse(data.get(8), DateTimeFormatter.ofPattern(CorePlatformConstants.MT4_DATE_TIME_FORMAT)))
                .type(data.get(2))
                .size(safeParseDouble(data.get(3)))
                .item(data.get(4))
                .openPrice(safeParseDouble(data.get(5)))
                .stopLoss(safeParseDouble(data.get(6)))
                .takeProfit(safeParseDouble(data.get(7)))
                .closePrice(safeParseDouble(data.get(9)))
                .profit(safeParseDouble(data.get(13).replace(" ", StringUtils.EMPTY).replace(",", StringUtils.EMPTY).trim()))
                .build();
    }

    /**
     * Generates a {@link MetaTrader4TransactionWrapper} from the given string
     *
     * @param string input value
     * @return {@link MetaTrader4TransactionWrapper}
     */
    private MetaTrader4TransactionWrapper generateTransactionWrapper(final String string) {

        if (StringUtils.isEmpty(string)) {
            return null;
        }

        final List<String> data = parseMetaTrader4RowEntry(string);
        if (data.size() != 5) {
            return null;
        }

        try {
            final double amount = safeParseDouble(data.get(4));
            return MetaTrader4TransactionWrapper
                    .builder()
                    .amount(amount)
                    .name(parseMetaTrader4TransactionName(data.get(3), amount))
                    .dateTime(LocalDateTime.parse(data.get(1), DateTimeFormatter.ofPattern(CorePlatformConstants.MT4_DATE_TIME_FORMAT)))
                    .type(amount > 0.0 ? TransactionType.DEPOSIT : TransactionType.WITHDRAWAL)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Failed to import row with reason {}", e.getMessage(), e);
            return null;
        }
    }
}
