package com.bluebell.radicle.importing.services;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.services.MathService;
import com.bluebell.radicle.importing.records.FTMOTradeWrapper;
import com.bluebell.radicle.importing.records.MetaTrader4TradeWrapper;
import com.bluebell.radicle.repositories.account.AccountRepository;
import com.bluebell.radicle.repositories.trade.TradeRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Parent-level import service for re-usable functionality
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Slf4j
@Service("abstractImportService")
public abstract class AbstractImportService {

    private static final List<String> BUY_SIGNALS = List.of("buy");
    private static final List<String> SELL_SIGNALS = List.of("sell");
    private final MathService mathService = new MathService();

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "tradeRepository")
    private TradeRepository tradeRepository;


    //  METHODS

    /**
     * Refreshes an {@link Account} after importing {@link Trade}s
     *
     * @param tradeMap {@link  Map} of imported {@link Trade}s
     * @param existingTrades {@link  Map} of existing {@link Trade}s
     * @param account {@link Account}
     */
    protected void refreshAccount(final Map<String, Trade> tradeMap, final Map<String, Trade> existingTrades, final Account account) {
        final double existingSum = existingTrades.values().stream().mapToDouble(Trade::getNetProfit).sum();
        this.tradeRepository.saveAll(tradeMap.values());

        Account temp = this.accountRepository.save(account.refreshAccount());
        temp.setBalance(this.mathService.subtract(account.getBalance(), existingSum));
        this.accountRepository.save(temp);
    }

    /**
     * Cleans up MT4 trades during the import process
     *
     * @param account {@link Account}
     * @param trades {@link List} of {@link MetaTrader4TradeWrapper}s
     */
    protected void mt4TradeCleanup(final Account account, final List<MetaTrader4TradeWrapper> trades) {

        final Map<String, Trade> tradeMap = new HashMap<>();
        final Map<String, Trade> existingTrades = new HashMap<>();

        this.tradeRepository.findAllByAccount(account).forEach(trade -> existingTrades.put(trade.getTradeId(), trade));
        final List<MetaTrader4TradeWrapper> buyTrades = trades.stream().filter(trade -> !existingTrades.containsKey(trade.ticketNumber())).filter(trade -> matchTradeType(trade.type(), TradeType.BUY)).toList();
        final List<MetaTrader4TradeWrapper> sellTrades = trades.stream().filter(trade -> !existingTrades.containsKey(trade.ticketNumber())).filter(trade -> matchTradeType(trade.type(), TradeType.SELL)).toList();

        buyTrades.forEach(trade -> tradeMap.put(trade.ticketNumber(), createNewTrade(trade, TradeType.BUY, account)));
        sellTrades.forEach(trade -> tradeMap.put(trade.ticketNumber(), createNewTrade(trade, TradeType.SELL, account)));

        refreshAccount(tradeMap, existingTrades, account);
    }

    /**
     * Cleans up MT4 trades during the import process from FTMO
     *
     * @param account {@link Account}
     * @param trades {@link List} of {@link FTMOTradeWrapper}s
     */
    protected void ftmoTradeCleanup(final Account account, final List<FTMOTradeWrapper> trades) {

        final Map<String, Trade> tradeMap = new HashMap<>();
        final Map<String, Trade> existingTrades = new HashMap<>();

        this.tradeRepository.findAllByAccount(account).forEach(trade -> existingTrades.put(trade.getTradeId(), trade));
        final List<FTMOTradeWrapper> buyTrades = trades.stream().filter(trade -> !existingTrades.containsKey(trade.ticketNumber())).filter(trade -> matchTradeType(trade.type(), TradeType.BUY)).toList();
        final List<FTMOTradeWrapper> sellTrades = trades.stream().filter(trade -> !existingTrades.containsKey(trade.ticketNumber())).filter(trade -> matchTradeType(trade.type(), TradeType.SELL)).toList();

        buyTrades.forEach(trade -> tradeMap.put(trade.ticketNumber(), createNewTrade(trade, TradeType.BUY, account)));
        sellTrades.forEach(trade -> tradeMap.put(trade.ticketNumber(), createNewTrade(trade, TradeType.SELL, account)));

        refreshAccount(tradeMap, existingTrades, account);
    }

    /**
     * Parses a list of mt4 trade rows into a list trade cells
     *
     * @param string string
     * @return list of strings
     */
    protected List<String> parseMetaTrader4TradeList(final String string) {

        final Pattern pattern = Pattern.compile(CorePlatformConstants.Regex.Import.MT4_HTML_TABLE_ROW);
        final Matcher matcher = pattern.matcher(string);

        final List<String> entries = new ArrayList<>();
        while (matcher.find()) {
            entries.add(
                    matcher.group()
                            .replaceAll(CorePlatformConstants.Regex.Import.MT4_HTML_TABLE_ROW_START, StringUtils.EMPTY)
                            .replace(CorePlatformConstants.Regex.Import.MT4_HTML_TABLE_ROW_END, StringUtils.EMPTY)
                            .trim()
            );
        }

        return entries;
    }

    /**
     * Parse MT4 html to obtain data in the form of a list
     *
     * @param string html string
     * @return {@link List} of components
     */
    protected List<String> parseMetaTrader4Trade(final String string) {

        final Pattern pattern = Pattern.compile(CorePlatformConstants.Regex.Import.MT4_HTML_TABLE_CELL);
        final Matcher matcher = pattern.matcher(string);
        final List<String> data = new ArrayList<>();

        while (matcher.find()) {
            data.add(
                    matcher.group()
                            .replaceAll(CorePlatformConstants.Regex.Import.MT4_HTML_TABLE_CELL_START, StringUtils.EMPTY)
                            .replace(CorePlatformConstants.Regex.Import.MT4_HTML_TABLE_CELL_END, StringUtils.EMPTY)
                            .trim()
            );
        }

        return data;
    }

    /**
     * Parses a string into a list of strings separated by commas
     *
     * @param string string
     * @return {@link List} of {@link String}
     */
    protected List<String> parseCsvTrade(final String string) {
        try {
            return Arrays.stream(string.split(";")).toList();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Safely parses an integer
     *
     * @param s test string
     * @return integer
     */
    protected int safeParseInt(final String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Safely parses a {@link String} into a {@link Double}
     *
     * @param string {@link String}
     * @return {@link Double}
     */
    protected double safeParseDouble(final String string) {

        if (StringUtils.isEmpty(string) || string.equals("-")) {
            return 0.0;
        }

        return Double.parseDouble(string.replaceAll("[^\\d.-]", StringUtils.EMPTY).trim());
    }


    //  HELPERS

    /**
     * Creates a new {@link Trade} from a {@link MetaTrader4TradeWrapper}
     *
     * @param wrapper   {@link MetaTrader4TradeWrapper}
     * @param tradeType {@link TradeType}
     * @return {@link Trade}
     */
    private Trade createNewTrade(final MetaTrader4TradeWrapper wrapper, final TradeType tradeType, final Account account) {
        return Trade
                .builder()
                .tradeId(wrapper.ticketNumber())
                .tradePlatform(TradePlatform.METATRADER4)
                .product(wrapper.item())
                .tradeType(tradeType)
                .closePrice(wrapper.closePrice())
                .tradeCloseTime(wrapper.closeTime())
                .tradeOpenTime(wrapper.openTime())
                .lotSize(wrapper.size())
                .netProfit(wrapper.profit())
                .openPrice(wrapper.openPrice())
                .stopLoss(wrapper.stopLoss())
                .takeProfit(wrapper.takeProfit())
                .account(account)
                .build();
    }

    /**
     * Creates a new {@link Trade} from a {@link MetaTrader4TradeWrapper}
     *
     * @param ftmoWrapper   {@link MetaTrader4TradeWrapper}
     * @param tradeType {@link TradeType}
     * @return {@link Trade}
     */
    private Trade createNewTrade(final FTMOTradeWrapper ftmoWrapper, final TradeType tradeType, final Account account) {
        return Trade.builder()
                .tradeId(ftmoWrapper.ticketNumber())
                .tradePlatform(TradePlatform.METATRADER4)
                .product(ftmoWrapper.item())
                .lotSize(ftmoWrapper.size())
                .netProfit(ftmoWrapper.profit())
                .openPrice(ftmoWrapper.openPrice())
                .stopLoss(ftmoWrapper.stopLoss())
                .takeProfit(ftmoWrapper.takeProfit())
                .tradeType(tradeType)
                .closePrice(ftmoWrapper.closePrice())
                .tradeCloseTime(ftmoWrapper.closeTime())
                .tradeOpenTime(ftmoWrapper.openTime())
                .account(account)
                .build();
    }

    /**
     * Determines whether the given trade should be considered
     *
     * @param trade     trade name
     * @param tradeType {@link TradeType}
     * @return true if matches keywords
     */
    private boolean matchTradeType(final String trade, final TradeType tradeType) {
        final List<String> matchers = tradeType.equals(TradeType.BUY) ? BUY_SIGNALS : SELL_SIGNALS;
        return matchers.stream().anyMatch(trade::contains);
    }
}
