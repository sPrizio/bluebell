package com.bluebell.radicle.importing.services.strategy;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.radicle.importing.ImportService;
import com.bluebell.radicle.importing.enums.MetaTrader4TradeType;
import com.bluebell.radicle.importing.exceptions.StrategyImportFailureException;
import com.bluebell.radicle.importing.exceptions.TradeImportFailureException;
import com.bluebell.radicle.importing.models.wrapper.trade.MetaTrader4TradeWrapper;
import com.bluebell.radicle.importing.services.AbstractImportService;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service-layer for importing trades into the system from the MetaTrader4 platform
 *
 * @author Stephen Prizio
 * @version 0.1.8
 */
@Service("metaTrader4StrategyImportService")
public class MetaTrader4StrategyImportService extends AbstractImportService implements ImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetaTrader4StrategyImportService.class);
    private static final String DELIMITER = "%bb%";
    private static final String PRODUCT_TARGET = "Symbol</td>";
    private String product = "";


    //  METHODS

    /**
     * Imports trades from a CSV file from the MetaTrader 4 platform
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
     * Imports trades from a CSV file from the MetaTrader 4 platform
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
            bufferedReader.lines().forEach(line -> stringBuilder.append(line).append("%bb%"));

            final List<String> content = getContent(stringBuilder.toString());
            List<MetaTrader4TradeWrapper> trades =
                    resolveWrappers(
                            content
                                    .stream()
                                    .map(this::generateWrapper)
                                    .filter(Objects::nonNull)
                                    .toList()
                    )
                            .stream()
                            .sorted(Comparator.comparing(MetaTrader4TradeWrapper::getOpenTime))
                            .toList();

            mt4TradeCleanup(account, trades);
        } catch (Exception e) {
            LOGGER.error("The import process failed with reason : {}", e.getMessage(), e);
            throw new StrategyImportFailureException(String.format("The import process failed with reason : %s", e.getMessage()), e);
        }
    }

    /**
     * Obtains trade content from the file content
     *
     * @param string file content
     * @return {@link List} of trade content strings
     */
    private List<String> getContent(final String string) {

        final int ticketIndex = string.indexOf("Strategy Tester Report");
        if (ticketIndex == -1) {
            throw new StrategyImportFailureException("File was not of the strategy report format from MetaTrader 4.");
        }

        final int startIndex = string.indexOf("<img", ticketIndex);
        if (startIndex == -1) {
            throw new TradeImportFailureException("The import file is not properly formatted.");
        }

        final String productId = string.substring(string.indexOf(PRODUCT_TARGET), string.indexOf(CorePlatformConstants.Regex.Import.MT4_HTML_TABLE_ROW_END)).replace(PRODUCT_TARGET, StringUtils.EMPTY);
        this.product = getProductName(productId);

        final List<String> reportContent = Arrays.stream(string.substring(startIndex).split(DELIMITER)).toList();
        final Pattern pattern = Pattern.compile(CorePlatformConstants.Regex.Import.MT4_HTML_TABLE_ROW);

        final List<String> entries = new ArrayList<>();
        reportContent.forEach(rc -> {
            final Matcher matcher = pattern.matcher(rc);
            while (matcher.find()) {
                entries.add(
                        matcher.group()
                                .replaceAll(CorePlatformConstants.Regex.Import.MT4_HTML_TABLE_ROW_START, StringUtils.EMPTY)
                                .replace(CorePlatformConstants.Regex.Import.MT4_HTML_TABLE_ROW_END, StringUtils.EMPTY)
                                .trim()
                );
            }
        });

        return entries;
    }

    /**
     * Obtains the product/symbol name for the strategy import process
     *
     * @param string filtered name string
     * @return product name / symbol
     */
    private String getProductName(final String string) {

        final Pattern test = Pattern.compile(CorePlatformConstants.Regex.Import.MT4_HTML_TABLE_CELL);
        final List<String> entries = new ArrayList<>();
        final Matcher matcher = test.matcher(string);
        while (matcher.find()) {
            entries.add(
                    matcher.group()
                            .replaceAll(CorePlatformConstants.Regex.Import.MT4_HTML_TABLE_CELL_START, StringUtils.EMPTY)
                            .replace(CorePlatformConstants.Regex.Import.MT4_HTML_TABLE_CELL_END, StringUtils.EMPTY)
                            .trim()
            );
        }

        if (CollectionUtils.isNotEmpty(entries)) {
            return entries.get(0);
        }

        return StringUtils.EMPTY;
    }

    /**
     * Resolves a list of incomplete mt4 wrappers in a list of complete wrappers
     *
     * @param wrappers {@link List} of incomplete {@link MetaTrader4TradeWrapper}
     * @return {@link List} of complete {@link MetaTrader4TradeWrapper}
     */
    private List<MetaTrader4TradeWrapper> resolveWrappers(final List<MetaTrader4TradeWrapper> wrappers) {

        final HashMap<String, MetaTrader4TradeWrapper> map = new HashMap<>();
        wrappers.forEach(wrapper -> {
            if (map.containsKey(wrapper.ticketNumber())) {
                map.replace(wrapper.ticketNumber(), wrapper.merge(map.get(wrapper.ticketNumber())));
            } else {
                map.put(wrapper.ticketNumber(), wrapper);
            }
        });

        final List<MetaTrader4TradeWrapper> resolved = new ArrayList<>(map.values());
        map.clear();

        return resolved;
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
        if (CollectionUtils.isEmpty(data) || safeParseInt(data.get(0)) == -1 || data.size() < 8 || data.size() > 10) {
            return null;
        }

        final String type = data.get(2);
        if (data.size() == 9 && MetaTrader4TradeType.isEntry(type)) {
            return MetaTrader4TradeWrapper
                    .builder()
                    .ticketNumber(data.get(3))
                    .openTime(LocalDateTime.parse(data.get(1), DateTimeFormatter.ofPattern(CorePlatformConstants.MT4_DATE_SHORT_TIME_FORMAT)).minusHours(7))
                    .closeTime(null)
                    .type(type)
                    .size(Double.parseDouble(data.get(4)))
                    .item(this.product)
                    .openPrice(Double.parseDouble(data.get(5)))
                    .stopLoss(Double.parseDouble(data.get(6)))
                    .takeProfit(Double.parseDouble(data.get(7)))
                    .closePrice(0.0)
                    .profit(0.0)
                    .build();
        } else if (data.size() == 10 && MetaTrader4TradeType.isExit(type)) {
            return MetaTrader4TradeWrapper
                    .builder()
                    .ticketNumber(data.get(3))
                    .openTime(null)
                    .closeTime(LocalDateTime.parse(data.get(1), DateTimeFormatter.ofPattern(CorePlatformConstants.MT4_DATE_SHORT_TIME_FORMAT)).minusHours(7))
                    .type(type)
                    .size(Double.parseDouble(data.get(4)))
                    .item(this.product)
                    .openPrice(0.0)
                    .stopLoss(Double.parseDouble(data.get(6)))
                    .takeProfit(Double.parseDouble(data.get(7)))
                    .closePrice(Double.parseDouble(data.get(5)))
                    .profit(Double.parseDouble(data.get(8)))
                    .build();
        }

        return null;
    }
}
