package com.bluebell.radicle.services.trade;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.api.dto.trade.CreateUpdateTradeDTO;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.models.core.nonentities.records.trade.TradeInsights;
import com.bluebell.platform.services.MathService;
import com.bluebell.radicle.exceptions.system.EntityCreationException;
import com.bluebell.radicle.exceptions.system.EntityModificationException;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.repositories.trade.TradeRepository;
import com.bluebell.radicle.services.AbstractEntityService;
import com.bluebell.radicle.services.market.MarketPriceService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

import static com.bluebell.radicle.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link Trade} entities
 *
 * @author Stephen Prizio
 * @version 0.2.5
 */
@Slf4j
@Service
public class TradeService extends AbstractEntityService {

    private static final Random RANDOM = new Random();
    private static final MathService MATH_SERVICE = new MathService();

    @Resource(name = "marketPriceService")
    private MarketPriceService marketPriceService;

    @Resource(name = "tradeRepository")
    private TradeRepository tradeRepository;


    //  METHODS

    /**
     * Returns a {@link List} of {@link Trade}s for the given {@link TradeType} and {@link Account}
     *
     * @param tradeType {@link TradeType}
     * @param account   {@link Account}
     * @return {@link List} of {@link Trade}s
     */
    public List<Trade> findAllByTradeType(final TradeType tradeType, final Account account) {

        validateParameterIsNotNull(tradeType, CorePlatformConstants.Validation.Trade.TRADE_TYPE_CANNOT_BE_NULL);
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        return this.tradeRepository.findAllByTradeTypeAndAccountOrderByTradeOpenTimeAsc(tradeType, account);
    }

    /**
     * Returns a {@link List} of {@link Trade}s that are within the given time span
     *
     * @param start   {@link LocalDateTime} start of interval (inclusive)
     * @param end     {@link LocalDateTime} end of interval (exclusive)
     * @param account {@link Account}
     * @return {@link List} of {@link Trade}s
     */
    public List<Trade> findAllTradesWithinTimespan(final LocalDateTime start, final LocalDateTime end, final Account account) {

        validateParameterIsNotNull(start, CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start, end, CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);

        return this.tradeRepository.findAllTradesWithinDate(start, end, account);
    }

    /**
     * Returns a {@link List} of {@link Trade}s that are within the given time span
     *
     * @param start    {@link LocalDateTime} start of interval (inclusive)
     * @param end      {@link LocalDateTime} end of interval (exclusive)
     * @param account  {@link Account}
     * @param page     page number
     * @param pageSize page size
     * @param sort sort order
     * @return {@link Page} of {@link Trade}s
     */
    public Page<Trade> findAllTradesWithinTimespan(final LocalDateTime start, final LocalDateTime end, final Account account, final int page, final int pageSize, final Sort sort) {
        validateStandardParameters(start, end, account, sort);
        return this.tradeRepository.findAllTradesWithinDatePaged(start.toLocalDate().atStartOfDay(), end.toLocalDate().atStartOfDay(), account, PageRequest.of(page, pageSize, sort));
    }

    /**
     * Returns a paginated {@link List} of {@link Trade}s that are within the given time span for the given symbol
     *
     * @param start    {@link LocalDateTime} start of interval (inclusive)
     * @param end      {@link LocalDateTime} end of interval (exclusive)
     * @param account  {@link Account}
     * @param symbol  symbol
     * @param page page number
     * @param pageSize page size
     * @param sort sort order
     * @return {@link Page} of {@link Trade}s
     */
    public Page<Trade> findAllTradesForSymbolWithinTimespan(final LocalDateTime start, final LocalDateTime end, final Account account, final String symbol, final int page, final int pageSize, final Sort sort) {
        validateStandardParameters(start, end, account, sort);
        validateParameterIsNotNull(symbol, CorePlatformConstants.Validation.MarketPrice.SYMBOL_CANNOT_BE_NULL);
        return this.tradeRepository.findAllTradesForSymbolWithinDatePaged(start.toLocalDate().atStartOfDay(), end.toLocalDate().atStartOfDay(), account, symbol, PageRequest.of(page, pageSize, sort));
    }

    /**
     * Returns a paginated {@link List} of {@link Trade}s that are within the given time span for the given trade type
     *
     * @param start    {@link LocalDateTime} start of interval (inclusive)
     * @param end      {@link LocalDateTime} end of interval (exclusive)
     * @param account  {@link Account}
     * @param tradeType {@link TradeType}
     * @param page page number
     * @param pageSize page size
     * @param sort sort order
     * @return {@link Page} of {@link Trade}s
     */
    public Page<Trade> findAllTradesForTradeTypeWithinTimespan(final LocalDateTime start, final LocalDateTime end, final Account account, final TradeType tradeType, final int page, final int pageSize, final Sort sort) {
        validateStandardParameters(start, end, account, sort);
        validateParameterIsNotNull(tradeType, CorePlatformConstants.Validation.Trade.TRADE_TYPE_CANNOT_BE_NULL);
        return this.tradeRepository.findAllTradesForTypeWithinDatePaged(start.toLocalDate().atStartOfDay(), end.toLocalDate().atStartOfDay(), account, tradeType, PageRequest.of(page, pageSize, sort));
    }

    /**
     * Returns a paginated {@link List} of {@link Trade}s that are within the given time span for the given symbol and trade type
     *
     * @param start    {@link LocalDateTime} start of interval (inclusive)
     * @param end      {@link LocalDateTime} end of interval (exclusive)
     * @param account  {@link Account}
     * @param symbol  symbol
     * @param tradeType {@link TradeType}
     * @param page page number
     * @param pageSize page size
     * @param sort sort order
     * @return {@link Page} of {@link Trade}s
     */
    public Page<Trade> findAllTradesForSymbolAndTradeTypeWithinTimespan(final LocalDateTime start, final LocalDateTime end, final Account account, final String symbol, final TradeType tradeType, final int page, final int pageSize, final Sort sort) {
        validateStandardParameters(start, end, account, sort);
        validateParameterIsNotNull(symbol, CorePlatformConstants.Validation.MarketPrice.SYMBOL_CANNOT_BE_NULL);
        validateParameterIsNotNull(tradeType, CorePlatformConstants.Validation.Trade.TRADE_TYPE_CANNOT_BE_NULL);
        return this.tradeRepository.findAllTradesForSymbolAndTypeWithinDatePaged(start.toLocalDate().atStartOfDay(), end.toLocalDate().atStartOfDay(), account, symbol, tradeType, PageRequest.of(page, pageSize, sort));
    }

    /**
     * Returns an {@link Optional} containing a {@link Trade}
     *
     * @param tradeId trade id
     * @param account {@link Account}
     * @return {@link Optional} {@link Trade}
     */
    public Optional<Trade> findTradeByTradeId(final String tradeId, final Account account) {
        validateParameterIsNotNull(tradeId, CorePlatformConstants.Validation.Trade.TRADE_ID_CANNOT_BE_NULL);
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        return Optional.ofNullable(this.tradeRepository.findTradeByTradeIdAndAccount(tradeId, account));
    }

    /**
     * Saves all {@link Trade}s within the given list to the database
     *
     * @param trades {@link List} of {@link Trade}s
     * @param account {@link Account}
     * @return count of insertions/updates
     */
    public int saveAll(final List<Trade> trades, final Account account) {

        if (CollectionUtils.isEmpty(trades) || account == null) {
            return -1;
        }

        int count = 0;
        for (final Trade trade : trades) {
            count += this.tradeRepository.upsertTrade(
                    trade.getTradeId(),
                    trade.getProduct(),
                    trade.getTradePlatform(),
                    trade.getTradeType(),
                    trade.getTradeOpenTime(),
                    trade.getTradeCloseTime(),
                    trade.getLotSize(),
                    trade.getOpenPrice(),
                    trade.getClosePrice(),
                    trade.getNetProfit(),
                    trade.getStopLoss(),
                    trade.getTakeProfit(),
                    account.getId()
            );
        }

        return count;
    }

    /**
     * Creates a new {@link Trade} with the given data
     *
     * @param data {@link CreateUpdateTradeDTO}
     * @param account {@link Account}
     * @return new {@link Trade}
     */
    public Trade createNewTrade(final CreateUpdateTradeDTO data, final Account account) {
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        if (data == null || StringUtils.isEmpty(data.tradeOpenTime())) {
            throw new MissingRequiredDataException("The required data for creating a Trade entity was null or empty");
        }

        try {
            return applyChanges(Trade.builder().build(), data, account);
        } catch (Exception e) {
            throw new EntityCreationException(String.format("A Trade could not be created : %s", e.getMessage()), e);
        }
    }

    /**
     * Updates an existing {@link Trade}
     *
     * @param trade {@link Trade}
     * @param data {@link CreateUpdateTradeDTO}
     * @param account {@link Account}
     * @return updated {@link Trade}
     */
    public Trade updateTrade(final Trade trade, final CreateUpdateTradeDTO data, final Account account) {
        validateParameterIsNotNull(trade, CorePlatformConstants.Validation.Trade.TRADE_CANNOT_BE_NULL);
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        if (data == null || StringUtils.isEmpty(data.tradeId())) {
            throw new MissingRequiredDataException("The required data for updating a Trade entity was null or empty");
        }

        try {
            return applyChanges(trade, data, account);
        } catch (Exception e) {
            throw new EntityModificationException(String.format("An error occurred while modifying the Trade : %s", e.getMessage()), e);
        }
    }

    /**
     * Deletes an existing {@link Trade}
     *
     * @param trade {@link Trade}
     * @return true if the trade was deleted, false otherwise
     */
    public boolean deleteTrade(final Trade trade) {
        validateParameterIsNotNull(trade, CorePlatformConstants.Validation.Trade.TRADE_CANNOT_BE_NULL);

        try {
            this.tradeRepository.deleteById(trade.getId());
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * Generates trade insights for the given trade
     *
     * @param trade {@link Trade
     * @return {@link TradeInsights}
     */
    public TradeInsights generateTradeInsights(final Trade trade) {
        validateParameterIsNotNull(trade, CorePlatformConstants.Validation.Trade.TRADE_CANNOT_BE_NULL);
        validateParameterIsNotNull(trade.getAccount(), CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        if (trade.isOpen()) {
            return TradeInsights.builder().build();
        }

        final double balance = trade.getAccount().getBalance();
        final double risk = trade.getStopLoss() == 0 ? 0.0 : Math.abs(MATH_SERVICE.subtract(trade.getOpenPrice(), trade.getStopLoss()));
        final double reward = trade.getTakeProfit() == 0 ? -1.0 : Math.abs(MATH_SERVICE.subtract(trade.getOpenPrice(), trade.getTakeProfit()));
        final Pair<Double, Double> drawdown = calculateDrawdown(trade, trade.getAccount());

        return TradeInsights
                .builder()
                .dayOfWeek(trade.getTradeOpenTime().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.CANADA))
                .rrr(MATH_SERVICE.divide(reward, risk))
                .risk(risk)
                .riskEquityPercentage(calculateEquityPercentage(balance, risk))
                .reward(reward)
                .rewardEquityPercentage(calculateEquityPercentage(balance, reward))
                .duration(Math.abs(ChronoUnit.SECONDS.between(trade.getTradeOpenTime(), trade.getTradeCloseTime())))
                .drawdown(drawdown.getValue0())
                .drawdownPercentage(drawdown.getValue1())
                .build();
    }


    //  HELPERS

    /**
     * Applies the changes to the given {@link Trade}}
     *
     * @param trade {@link Trade} to update
     * @param data {@link CreateUpdateTradeDTO}
     * @param account {@link Account}
     * @return updated {@link Trade}
     */
    private Trade applyChanges(Trade trade, final CreateUpdateTradeDTO data, final Account account) {

        if (StringUtils.isEmpty(data.tradeId())) {
            trade.setTradeId(generateUniqueTradeId(GenericEnum.getByCode(TradePlatform.class, data.tradePlatform()), account));
        } else {
            trade.setTradeId(data.tradeId());
        }

        trade.setTradePlatform(GenericEnum.getByCode(TradePlatform.class, data.tradePlatform()));
        trade.setProduct(data.product());
        trade.setTradeType(GenericEnum.getByCode(TradeType.class, data.tradeType()));
        trade.setClosePrice(data.closePrice());

        if (StringUtils.isNotEmpty(data.tradeCloseTime())) {
            trade.setTradeCloseTime((LocalDateTime.parse(data.tradeCloseTime(), DateTimeFormatter.ISO_DATE_TIME)));
        }

        trade.setTradeOpenTime((LocalDateTime.parse(data.tradeOpenTime(), DateTimeFormatter.ISO_DATE_TIME)));
        trade.setLotSize(data.lotSize());
        trade.setNetProfit(data.netProfit());
        trade.setOpenPrice(data.openPrice());
        trade.setStopLoss(data.stopLoss());
        trade.setTakeProfit(data.takeProfit());
        trade.setAccount(account);

        return this.tradeRepository.save(trade);
    }

    /**
     * Generates a unique trade id
     *
     * @param tradePlatform {@link TradePlatform}
     * @param account {@link Account}
     * @return trade id
     */
    private String generateUniqueTradeId(final TradePlatform tradePlatform, final Account account) {
        validateParameterIsNotNull(tradePlatform, CorePlatformConstants.Validation.Trade.TRADE_PLATFORM_CANNOT_BE_NULL);
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        String generated = String.valueOf(1_000_000 + RANDOM.nextInt(9_000_000));
        Optional<Trade> matched = findTradeByTradeId(generated, account);
        while (matched.isPresent()) {
            generated = String.valueOf(1_000_000 + RANDOM.nextInt(9_000_000));
            matched = findTradeByTradeId(generated, account);
        }

        return tradePlatform.getCode().charAt(0) + "-" + generated;
    }

    /**
     * Calculates the equity percentage
     *
     * @param balance account balance
     * @param compare delta
     * @return percentage
     */
    private double calculateEquityPercentage(final double balance, final double compare) {
        if (compare == 0.0) {
            return 100.0;
        } else if (compare == -1.0) {
            return 0.0;
        }

        return MATH_SERVICE.delta(compare, balance);
    }

    /**
     * Computes a percentage for the change in account balance
     *
     * @param balance balance
     * @param delta change
     * @return percentage
     */
    private double computeEquityChange(final double balance, final double delta) {
        return MATH_SERVICE.multiply(MATH_SERVICE.delta(balance, delta), -1.0);
    }

    /**
     * Calculates the drawdown for trade insights
     *
     * @param trade {@link Trade}
     * @param account {@link Account}
     * @return {@link Pair} of drawdown and percentage
     */
    private Pair<Double, Double> calculateDrawdown(final Trade trade, final Account account) {
        validateParameterIsNotNull(trade, CorePlatformConstants.Validation.Trade.TRADE_CANNOT_BE_NULL);
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        final List<MarketPrice> marketPrices = this.marketPriceService.findMarketPricesForTrade(trade, MarketPriceTimeInterval.ONE_MINUTE, trade.getTradePlatform().getDataSource());
        if (CollectionUtils.isNotEmpty(marketPrices)) {
            final double dollarPerPoint = MATH_SERVICE.divide(trade.getNetProfit(), Math.abs(MATH_SERVICE.subtract(trade.getOpenPrice(), trade.getClosePrice())));
            if (trade.getStopLoss() == trade.getClosePrice()) {
                return Pair.with(trade.getNetProfit(), computeEquityChange(account.getBalance(), trade.getNetProfit()));
            }

            final double lowestPoint =
                    trade.getTradeType() == TradeType.BUY ? marketPrices.stream().mapToDouble(MarketPrice::getLow).min().orElse(0.0) : marketPrices.stream().mapToDouble(MarketPrice::getHigh).max().orElse(0.0);

            if (lowestPoint == trade.getOpenPrice()) {
                return Pair.with(0.0, 0.0);
            }

            final double delta = Math.abs(MATH_SERVICE.subtract(lowestPoint, trade.getOpenPrice()));
            final double count = MATH_SERVICE.multiply(MATH_SERVICE.multiply(dollarPerPoint, delta), -1.0);

            return Pair.with(count, computeEquityChange(account.getBalance(), count));
        }

        return Pair.with(0.0, 0.0);
    }
}
