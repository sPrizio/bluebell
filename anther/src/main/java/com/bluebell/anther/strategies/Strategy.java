package com.bluebell.anther.strategies;

import com.bluebell.anther.enums.TradeType;
import com.bluebell.anther.models.parameter.strategy.impl.BasicStrategyParameters;
import com.bluebell.anther.models.strategy.StrategyResult;
import com.bluebell.anther.models.trade.Trade;
import com.bluebell.radicle.models.AggregatedMarketPrices;
import com.bluebell.radicle.models.MarketPrice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Defines a trading strategy
 *
 * @author Stephen Prizio
 * @version 0.0.1
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


    //  HELPERS

    /**
     * Creates a new {@link Trade} that is considered 'active'. In this case active means that a {@link Trade} does not have a close price
     *
     * @param tradeType {@link TradeType}
     * @param lotSize trade size
     * @param tradeOpenTime time of open
     * @param openPrice price at open
     * @param stopLoss limit loss
     * @param takeProfit limit profit
     * @return {@link Trade}
     */
    default Trade openTrade(final TradeType tradeType, final double lotSize, final LocalDateTime tradeOpenTime, final double openPrice, final double stopLoss, final double takeProfit) {
       return new Trade(tradeType, lotSize, tradeOpenTime, openPrice, stopLoss, takeProfit);
    }

    /**
     * Updates the given {@link Trade} with a close time and price
     *
     * @param trade {@link Trade}
     * @param tradeCloseTime close time
     * @param closePrice close price
     */
    default void closeTrade(final Trade trade, final LocalDateTime tradeCloseTime, final double closePrice) {
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
     * Checks whether the given {@link MarketPrice} closes an open {@link Trade}
     *
     * @param marketPrice {@link MarketPrice}
     */
    default void checkTrades(final Map<String, Trade> openTrades, final Map<String, Trade> closedTrades, final MarketPrice marketPrice) {
        openTrades.forEach((key, value) -> {
            if (value.getTradeType() == TradeType.BUY) {
                if (marketPrice.high() >= value.getTakeProfit()) {
                    // hit take profit
                    closeTrade(value, marketPrice.date(), value.getTakeProfit());
                    closedTrades.put(value.getId(), value);
                } else if (marketPrice.low() <= value.getStopLoss()) {
                    // hit stop loss
                    closeTrade(value, marketPrice.date(), value.getStopLoss());
                    closedTrades.put(value.getId(), value);
                }
            } else {
                if (marketPrice.low() <= value.getTakeProfit()) {
                    // hit take profit
                    closeTrade(value, marketPrice.date(), value.getTakeProfit());
                    closedTrades.put(value.getId(), value);
                } else if (marketPrice.high() >= value.getStopLoss()) {
                    // hit stop loss
                    closeTrade(value, marketPrice.date(), value.getStopLoss());
                    closedTrades.put(value.getId(), value);
                }
            }
        });

        closedTrades.keySet().forEach(openTrades::remove);
    }
}
