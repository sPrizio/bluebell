package com.bluebell.anther.strategies.impl;

import com.bluebell.anther.enums.TradeSignal;
import com.bluebell.anther.models.parameter.strategy.impl.SproutStrategyParameters;
import com.bluebell.anther.models.strategy.StrategyResult;
import com.bluebell.anther.models.trade.Trade;
import com.bluebell.anther.strategies.Strategy;
import com.bluebell.radicle.models.AggregatedMarketPrices;
import com.bluebell.radicle.models.MarketPrice;
import com.bluebell.radicle.services.MathService;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Sprout strategy. Sprout looks for highs and lows that are violated in the opposite direction but the proceeding candle.
 * In cases like these, a small (or large) reversal is likely taking place
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
public class Sprout implements Strategy<SproutStrategyParameters> {

    private final MathService mathService = new MathService();

    private final SproutStrategyParameters strategyParameters;

    private final Map<String, Trade> openTrades;

    private final Map<String, Trade> closedTrades;


    //  CONSTRUCTORS

    public Sprout(final SproutStrategyParameters strategyParameters) {
        this.strategyParameters = strategyParameters;
        this.openTrades = new HashMap<>();
        this.closedTrades = new HashMap<>();
    }


    //  METHODS

    @Override
    public StrategyResult<SproutStrategyParameters> executeStrategy(final LocalDate startDate, final LocalDate endDate, final Map<LocalDate, AggregatedMarketPrices> prices) {

        for (final Map.Entry<LocalDate, AggregatedMarketPrices> entry : prices.entrySet()) {

            if (entry.getKey().isBefore(startDate) || (entry.getKey().isAfter(endDate) || entry.getKey().isEqual(endDate))) {
                continue;
            }

            final List<MarketPrice> marketPrices = new ArrayList<>(entry.getValue().marketPrices());
            for (int i = 2; i < marketPrices.size(); i++) {
                final MarketPrice referencePrice = marketPrices.get(i - 2);
                final MarketPrice signalPrice = marketPrices.get(i - 1);
                final MarketPrice currentPrice = marketPrices.get(i);

                final TradeSignal tradeSignal = getTradeSignal(referencePrice, signalPrice, currentPrice);
                if (tradeSignal != TradeSignal.NO_SIGNAL) {
                    final double price = tradeSignal == TradeSignal.BUY_SIGNAL ? signalPrice.high() : signalPrice.low();
                    final Trade trade = openTrade(
                            tradeSignal.getTradeType(),
                            this.strategyParameters.getLotSize(),
                            currentPrice.date(),
                            price,
                            tradeSignal == TradeSignal.BUY_SIGNAL ? calculateActualLimit(signalPrice.getFullSize(true), price, this.strategyParameters.getBuyLimit().getStopLoss(), false, false) : calculateActualLimit(0.0, price, this.strategyParameters.getSellLimit().getStopLoss(), true, false),
                            tradeSignal == TradeSignal.BUY_SIGNAL ? calculateActualLimit(signalPrice.getFullSize(true), price, this.strategyParameters.getBuyLimit().getTakeProfit(), true, true) : calculateActualLimit(0.0, price, this.strategyParameters.getSellLimit().getTakeProfit(), false, true)
                    );

                    this.openTrades.put(trade.getId(), trade);
                }

                //  check each market price to see if any of the open trades were hit
                checkTrades(this.openTrades, this.closedTrades, currentPrice);

                //  close the trading day
                closeDay(currentPrice, this.openTrades, this.closedTrades);
            }
        }

        final StrategyResult<SproutStrategyParameters> result = new StrategyResult<>(this.strategyParameters, startDate, endDate, this.closedTrades.values(), this.strategyParameters.getBuyLimit(), this.strategyParameters.getSellLimit(), this.strategyParameters.getPricePerPoint(), this.strategyParameters.isScaleProfits(), this.strategyParameters.getInitialBalance());
        this.openTrades.clear();
        this.closedTrades.clear();

        return result;
    }

    /**
     * Returns true if the given {@link MarketPrice} is equal to the end of day
     *
     * @param price {@link MarketPrice}
     * @return true if hour and minute are equal
     */
    public boolean isExitBar(final MarketPrice price) {
        return price.date().getHour() == 16 && price.date().getMinute() == 0;
    }


    //  HELPERS

    /**
     * Computes trade signals
     *
     * @param ref     reference price
     * @param signal  signal bar
     * @param current entry bar
     * @return {@link TradeSignal}
     */
    private TradeSignal getTradeSignal(final MarketPrice ref, final MarketPrice signal, final MarketPrice current) {

        //  new high
        if (signal.high() > ref.high() && current.low() < signal.low()) {
            return TradeSignal.SELL_SIGNAL;
        }

        //  new low
        if (signal.low() < ref.low() && current.high() > signal.high()) {
            return TradeSignal.BUY_SIGNAL;
        }

        return TradeSignal.NO_SIGNAL;
    }

    /**
     * Computes the limit based on the given parameters
     *
     * @param window            overrides limit if it is greater than the given limit
     * @param price             start price
     * @param increment         limit amount
     * @param shouldAdd         add or subtract
     * @param includeMultiplier multiplier for take profits
     * @return limit
     */
    private double calculateActualLimit(final double window, final double price, final double increment, final boolean shouldAdd, final boolean includeMultiplier) {
        return (window < increment) ? calculateLimit(price, increment, shouldAdd) : includeMultiplier ? calculateLimit(price, this.mathService.multiply(window, this.strategyParameters.getProfitMultiplier()), shouldAdd) : calculateLimit(price, window, shouldAdd);
    }
}
