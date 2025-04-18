package com.bluebell.anther.strategies;

import com.bluebell.anther.models.parameter.strategy.impl.BasicStrategyParameters;
import com.bluebell.anther.models.strategy.StrategyResult;
import com.bluebell.anther.models.trade.AntherTrade;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Defines a trading strategy
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
public interface Strategy<P extends BasicStrategyParameters> {

    /**
     * Executes a {@link Strategy} on the given collection of market prices
     *
     * @param startDate start date
     * @param endDate end date (NOTE: the end date is exclusive)
     * @param prices {@link Map} of {@link MarketPrice}
     * @return {@link StrategyResult}
     */
    StrategyResult<P> executeStrategy(final LocalDate startDate, final LocalDate endDate, final Map<LocalDate, AggregatedMarketPrices> prices);

    /**
     * Is the market price considered an exit
     *
     * @param price {@link MarketPrice}
     * @return true if the trade should be exited based on the exit criteria and the value of this price
     */
    boolean isExitBar(final MarketPrice price);


    //  HELPERS

    /**
     * Creates a new {@link AntherTrade} that is considered 'active'. In this case active means that a {@link AntherTrade} does not have a close price
     *
     * @param tradeType {@link TradeType}
     * @param lotSize trade size
     * @param tradeOpenTime time of open
     * @param openPrice price at open
     * @param stopLoss limit loss
     * @param takeProfit limit profit
     * @return {@link AntherTrade}
     */
    default AntherTrade openTrade(final TradeType tradeType, final double lotSize, final LocalDateTime tradeOpenTime, final double openPrice, final double stopLoss, final double takeProfit) {
        return AntherTrade
                .builder()
                .tradeType(tradeType)
                .lotSize(lotSize)
                .tradeOpenTime(tradeOpenTime)
                .openPrice(openPrice)
                .stopLoss(stopLoss)
                .takeProfit(takeProfit)
                .build();
    }

    /**
     * Updates the given {@link AntherTrade} with a close time and price
     *
     * @param trade {@link AntherTrade}
     * @param tradeCloseTime close time
     * @param closePrice close price
     */
    default void closeTrade(final AntherTrade trade, final LocalDateTime tradeCloseTime, final double closePrice) {
        trade.setTradeCloseTime(tradeCloseTime);
        trade.setClosePrice(closePrice);
    }

    /**
     * Calculates the limit used as either a take profit or stop loss
     *
     * @param price trade price
     * @param increment increment to add or subtract
     * @param shouldAdd if true, add
     * @return limit level
     */
    default double calculateLimit(final double price, final double increment, final boolean shouldAdd) {

        if (shouldAdd) {
            return BigDecimal.valueOf(price)
                    .add(BigDecimal.valueOf(increment))
                    .setScale(2, RoundingMode.HALF_EVEN)
                    .doubleValue();
        } else {
            return BigDecimal.valueOf(price)
                    .subtract(BigDecimal.valueOf(increment))
                    .setScale(2, RoundingMode.HALF_EVEN)
                    .doubleValue();
        }
    }

    /**
     * Checks whether the given {@link MarketPrice} closes an open {@link AntherTrade}
     *
     * @param marketPrice {@link MarketPrice}
     */
    default void checkTrades(final Map<String, AntherTrade> openTrades, final Map<String, AntherTrade> closedTrades, final MarketPrice marketPrice) {
        openTrades.forEach((key, value) -> {
            if (value.getTradeType() == TradeType.BUY) {
                if (marketPrice.getHigh() >= value.getTakeProfit()) {
                    // hit take profit
                    closeTrade(value, marketPrice.getDate(), value.getTakeProfit());
                    closedTrades.put(value.getId(), value);
                } else if (marketPrice.getLow() <= value.getStopLoss()) {
                    // hit stop loss
                    closeTrade(value, marketPrice.getDate(), value.getStopLoss());
                    closedTrades.put(value.getId(), value);
                }
            } else {
                if (marketPrice.getLow() <= value.getTakeProfit()) {
                    // hit take profit
                    closeTrade(value, marketPrice.getDate(), value.getTakeProfit());
                    closedTrades.put(value.getId(), value);
                } else if (marketPrice.getHigh() >= value.getStopLoss()) {
                    // hit stop loss
                    closeTrade(value, marketPrice.getDate(), value.getStopLoss());
                    closedTrades.put(value.getId(), value);
                }
            }
        });

        closedTrades.keySet().forEach(openTrades::remove);
    }

    /**
     * Closes the day, meaning close all trades on the exit signal
     *
     * @param currentPrice {@link MarketPrice} current bar
     * @param openTrades open trades
     * @param closedTrades closed trades
     */
    default void closeDay(final MarketPrice currentPrice, final Map<String, AntherTrade> openTrades, final Map<String, AntherTrade> closedTrades) {
        if (isExitBar(currentPrice) && !openTrades.isEmpty()) {
            openTrades.forEach((key, trade) -> {
                closeTrade(trade, currentPrice.getDate(), currentPrice.getOpen());
                closedTrades.put(trade.getId(), trade);
            });

            closedTrades.keySet().forEach(openTrades::remove);
        }
    }
}
