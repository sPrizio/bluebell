package com.bluebell.planter.importing.services.strategy;

import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.entities.trade.Trade;
import com.bluebell.planter.core.repositories.account.AccountRepository;
import com.bluebell.planter.core.repositories.trade.TradeRepository;
import com.bluebell.planter.importing.ImportService;
import com.bluebell.planter.importing.enums.MetaTrader4TradeType;
import com.bluebell.planter.importing.exceptions.StrategyImportFailureException;
import com.bluebell.planter.importing.exceptions.TradeImportFailureException;
import com.bluebell.planter.importing.records.MetaTrader4TradeWrapper;
import jakarta.annotation.Resource;
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
 * @version 0.0.7
 */
@Service("metaTrader4StrategyImportService")
public class MetaTrader4StrategyImportService implements ImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetaTrader4StrategyImportService.class);
    private static final String DELIMITER = "%bb%";
    private static final List<String> BUY_SIGNALS = List.of("buy");
    private static final List<String> SELL_SIGNALS = List.of("sell");

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "tradeRepository")
    private TradeRepository tradeRepository;


    //  METHODS

    /**
     * Imports trades from a CSV file from the Metatrader 4 platform
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
     * Imports trades from a CSV file from the Metatrader 4 platform
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

            final Map<String, Trade> tradeMap = new HashMap<>();
            final Map<String, Trade> existingTrades = new HashMap<>();
        } catch (Exception e) {
            LOGGER.error("The import process failed with reason : {}", e.getMessage(), e);
            throw new StrategyImportFailureException(String.format("The import process failed with reason : %s", e.getMessage()), e);
        }


        //  TODO: code cleanup for duplicates
        //  TODO: finish import code
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

        final List<String> reportContent = Arrays.stream(string.substring(startIndex).split(DELIMITER)).toList();
        final Pattern pattern = Pattern.compile("<tr.*?>(.*?)<\\/tr>");

        final List<String> entries = new ArrayList<>();
        reportContent.forEach(rc -> {
            final Matcher matcher = pattern.matcher(rc);
            while (matcher.find()) {
                entries.add(
                        matcher.group()
                                .replaceAll("<tr.*?>", StringUtils.EMPTY)
                                .replace("</tr>", StringUtils.EMPTY)
                                .trim()
                );
            }
        });

        return entries;
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

        final Pattern pattern = Pattern.compile("<td.*?>(.*?)<\\/td>");
        final Matcher matcher = pattern.matcher(string);
        final List<String> data = new ArrayList<>();

        while (matcher.find()) {
            data.add(
                    matcher.group()
                            .replaceAll("<td.*?>", StringUtils.EMPTY)
                            .replace("</td>", StringUtils.EMPTY)
                            .trim()
            );
        }

        if (safeParseInt(data.get(0)) == -1 || data.size() < 8 || data.size() > 10) {
            return null;
        }

        final String type = data.get(2);
        if (data.size() == 9 && MetaTrader4TradeType.isEntry(type)) {
            return new MetaTrader4TradeWrapper(
                    data.get(3),
                    LocalDateTime.parse(data.get(1), DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")),
                    null,
                    type,
                    Double.parseDouble(data.get(4)),
                    "",
                    Double.parseDouble(data.get(5)),
                    Double.parseDouble(data.get(6)),
                    Double.parseDouble(data.get(7)),
                    0.0,
                    0.0
            );
        } else if (data.size() == 10 && MetaTrader4TradeType.isExit(type)) {
            return new MetaTrader4TradeWrapper(
                    data.get(3),
                    null,
                    LocalDateTime.parse(data.get(1), DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")),
                    type,
                    Double.parseDouble(data.get(4)),
                    "",
                    0.0,
                    Double.parseDouble(data.get(6)),
                    Double.parseDouble(data.get(7)),
                    Double.parseDouble(data.get(5)),
                    Double.parseDouble(data.get(8))
            );
        }

        return null;
    }

    /**
     * Safely parses an integer
     *
     * @param s test string
     * @return integer
     */
    private int safeParseInt(final String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
