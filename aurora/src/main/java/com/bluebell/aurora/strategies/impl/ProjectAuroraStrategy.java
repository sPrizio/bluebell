package com.bluebell.aurora.strategies.impl;

import com.bluebell.aurora.enums.MarketDirection;
import com.bluebell.aurora.enums.TradeType;
import com.bluebell.aurora.models.strategy.StrategyResult;
import com.bluebell.aurora.models.strategy.parameter.StrategyParameters;
import com.bluebell.aurora.models.trade.Trade;
import com.bluebell.aurora.strategies.Strategy;
import com.bluebell.radicle.models.MarketPrice;
import lombok.Getter;

import java.time.LocalDate;
import java.util.*;

/**
 * Implementation of {@link Strategy}
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
public class ProjectAuroraStrategy implements Strategy {

    private final StrategyParameters strategyParameters;

    private final Map<String, Trade> openTrades;

    private final Map<String, Trade> closedTrades;


    //  CONSTRUCTORS

    public ProjectAuroraStrategy(final StrategyParameters strategyParameters) {
        this.strategyParameters = strategyParameters;
        this.openTrades = new HashMap<>();
        this.closedTrades = new HashMap<>();
    }


    //  METHODS

    @Override
    public StrategyResult executeStrategy(final LocalDate startDate, final LocalDate endDate, final Map<LocalDate, TreeSet<MarketPrice>> prices) {

        final boolean shouldCheckDirection = !(this.strategyParameters.startHour() == 9 && this.strategyParameters.startMinute() == 30);
        MarketDirection direction;

        for (final Map.Entry<LocalDate, TreeSet<MarketPrice>> entry : prices.entrySet()) {

            if (entry.getKey().isBefore(startDate) || (entry.getKey().isAfter(endDate) || entry.getKey().isEqual(endDate))) {
                continue;
            }

            if (shouldCheckDirection) {
                final MarketPrice open = getOpenPrice(entry.getValue());
                direction = open.isBullish() ? MarketDirection.BULLISH : MarketDirection.BEARISH;
            } else {
                direction = MarketDirection.BULLISH;
            }

            for (final MarketPrice marketPrice : entry.getValue()) {
                if (isSignalBar(marketPrice)) {
                    final TradeType tradeType = direction == MarketDirection.BULLISH ? TradeType.BUY : TradeType.SELL;
                    final Trade trade = openTrade(
                            tradeType,
                            this.strategyParameters.lotSize(),
                            marketPrice.date(),
                            marketPrice.open(),
                            calculateLimit(marketPrice.open(), this.strategyParameters.stopLoss(), tradeType == TradeType.SELL),
                            calculateLimit(marketPrice.open(), this.strategyParameters.targetProfit(), tradeType != TradeType.SELL)
                    );

                    this.openTrades.put(trade.getId(), trade);
                }

                //  check each market price to see if any of the open trades were hit
                checkTrades(marketPrice);
            }
        }

        final StrategyResult result = new StrategyResult(startDate, endDate, this.closedTrades.values(), this.strategyParameters.targetProfit(), this.strategyParameters.stopLoss(), this.strategyParameters.pricePerPoint());
        this.openTrades.clear();
        this.closedTrades.clear();

        return result;
    }


    //  HELPERS

    /**
     * Returns the open price for a collection of market prices
     *
     * @param prices {@link TreeSet} of {@link MarketPrice}
     * @return {@link MarketPrice}
     */
    private MarketPrice getOpenPrice(final TreeSet<MarketPrice> prices) {
        return prices.stream().filter(pr -> pr.date().getHour() == 9 && pr.date().getMinute() == 30).findFirst().orElse(new MarketPrice());
    }

    /**
     * Returns true if the given {@link MarketPrice} is equal to the start hour and minute
     *
     * @param price {@link MarketPrice}
     * @return true if hour and minute are equal
     */
    private boolean isSignalBar(final MarketPrice price) {
        return price.date().getHour() == this.strategyParameters.startHour() && price.date().getMinute() == this.strategyParameters.startMinute();
    }

    /**
     * Checks whether the given {@link MarketPrice} closes an open {@link Trade}
     *
     * @param marketPrice {@link MarketPrice}
     */
    private void checkTrades(final MarketPrice marketPrice) {
        this.openTrades.forEach((key, value) -> {
            if (value.getTradeType() == TradeType.BUY) {
                if (marketPrice.high() >= value.getTakeProfit()) {
                    // hit take profit
                    closeTrade(value, marketPrice.date(), value.getTakeProfit());
                    this.closedTrades.put(value.getId(), value);
                } else if (marketPrice.low() <= value.getStopLoss()) {
                    // hit stop loss
                    closeTrade(value, marketPrice.date(), value.getStopLoss());
                    this.closedTrades.put(value.getId(), value);
                }
            } else {
                if (marketPrice.low() <= value.getTakeProfit()) {
                    // hit take profit
                    closeTrade(value, marketPrice.date(), value.getTakeProfit());
                    this.closedTrades.put(value.getId(), value);
                } else if (marketPrice.high() >= value.getStopLoss()) {
                    // hit stop loss
                    closeTrade(value, marketPrice.date(), value.getStopLoss());
                    this.closedTrades.put(value.getId(), value);
                }
            }
        });

        this.closedTrades.keySet().forEach(this.openTrades::remove);
    }
}
