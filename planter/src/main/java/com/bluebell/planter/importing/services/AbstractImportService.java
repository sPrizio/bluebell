package com.bluebell.planter.importing.services;

import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.trade.info.TradeType;
import com.bluebell.planter.core.enums.trade.platform.TradePlatform;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.entities.trade.Trade;
import com.bluebell.planter.core.repositories.account.AccountRepository;
import com.bluebell.planter.core.repositories.trade.TradeRepository;
import com.bluebell.planter.importing.records.MetaTrader4TradeWrapper;
import com.bluebell.radicle.services.MathService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parent-level import service for re-usable functionality
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
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
     * Parse MT4 html to obtain data in the form of a list
     *
     * @param string html string
     * @return {@link List} of components
     */
    protected List<String> parseMetaTrader4Trade(final String string) {

        final Pattern pattern = Pattern.compile(CoreConstants.Regex.Import.MT4_HTML_TABLE_CELL);
        final Matcher matcher = pattern.matcher(string);
        final List<String> data = new ArrayList<>();

        while (matcher.find()) {
            data.add(
                    matcher.group()
                            .replaceAll(CoreConstants.Regex.Import.MT4_HTML_TABLE_CELL_START, StringUtils.EMPTY)
                            .replace(CoreConstants.Regex.Import.MT4_HTML_TABLE_CELL_END, StringUtils.EMPTY)
                            .trim()
            );
        }

        return data;
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

        Trade trade = new Trade();

        trade.setTradeId(wrapper.ticketNumber());
        trade.setTradePlatform(TradePlatform.METATRADER4);
        trade.setProduct(wrapper.item());
        trade.setTradeType(tradeType);
        trade.setClosePrice(wrapper.closePrice());
        trade.setTradeCloseTime(wrapper.closeTime());
        trade.setTradeOpenTime(wrapper.openTime());
        trade.setLotSize(wrapper.size());
        trade.setNetProfit(wrapper.profit());
        trade.setOpenPrice(wrapper.openPrice());
        trade.setStopLoss(wrapper.stopLoss());
        trade.setTakeProfit(wrapper.takeProfit());
        trade.setAccount(account);

        return trade;
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
