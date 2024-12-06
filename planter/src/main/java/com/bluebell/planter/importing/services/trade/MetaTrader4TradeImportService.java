package com.bluebell.planter.importing.services.trade;

import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.importing.ImportService;
import com.bluebell.planter.importing.exceptions.TradeImportFailureException;
import com.bluebell.planter.importing.records.MetaTrader4TradeWrapper;
import com.bluebell.planter.importing.services.AbstractImportService;
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
 * @version 0.0.7
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

            mt4TradeCleanup(account, trades);
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

        final List<String> data = parseMetaTrader4Trade(string);
        if (data.size() != 14) {
            return null;
        }

        return new MetaTrader4TradeWrapper(
                data.get(0),
                LocalDateTime.parse(data.get(1), DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")),
                LocalDateTime.parse(data.get(8), DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")),
                data.get(2),
                Double.parseDouble(data.get(3)),
                data.get(4),
                Double.parseDouble(data.get(5)),
                Double.parseDouble(data.get(6)),
                Double.parseDouble(data.get(7)),
                Double.parseDouble(data.get(9)),
                Double.parseDouble(data.get(13).replace(" ", StringUtils.EMPTY).replace(",", StringUtils.EMPTY).trim())
        );
    }
}
