package com.bluebell.anther.strategies.impl;

import com.bluebell.anther.enums.TradeSignal;
import com.bluebell.anther.enums.TradeType;
import com.bluebell.anther.models.parameter.strategy.impl.SproutStrategyParameters;
import com.bluebell.anther.models.strategy.StrategyResult;
import com.bluebell.anther.models.trade.Trade;
import com.bluebell.anther.strategies.Strategy;
import com.bluebell.radicle.models.AggregatedMarketPrices;
import com.bluebell.radicle.models.MarketPrice;
import com.bluebell.radicle.services.MathService;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Sprout strategy. Sprout looks for highs and lows that are violated in the opposite direction but the proceeding candle.
 * In cases like these, a small (or large) reversal is likely taking place
 *
 * @author Stephen Prizio
 * @version 0.0.2
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


    //  TODO: code cleanup for Sprout.mq4


    //  METHODS

    @Override
    public StrategyResult<SproutStrategyParameters> executeStrategy(final LocalDate startDate, final LocalDate endDate, final Map<LocalDate, AggregatedMarketPrices> prices) {

        for (final Map.Entry<LocalDate, AggregatedMarketPrices> entry : prices.entrySet()) {

            //  disregard data if it falls outside of the requested interval period
            if (entry.getKey().isBefore(startDate) || (entry.getKey().isAfter(endDate) || entry.getKey().isEqual(endDate))) {
                continue;
            }

            int count = 0;
            final List<MarketPrice> marketPrices = new ArrayList<>(entry.getValue().marketPrices());

            for (int i = 0; i < marketPrices.size(); i++) {

                if (!isWithinWindow(marketPrices.get(i).date().toLocalTime()) || count == 3) {
                    continue;
                }

                final MarketPrice referencePrice = marketPrices.get(i - 2);
                final MarketPrice signalPrice = marketPrices.get(i - 1);
                final MarketPrice currentPrice = marketPrices.get(i);

                final TradeSignal tradeSignal = getTradeSignal(referencePrice, signalPrice, currentPrice);
                if (tradeSignal != TradeSignal.NO_SIGNAL && this.openTrades.isEmpty()/* && hasConfirmation(tradeSignal, referencePrice, signalPrice, currentPrice)*/) {
                    final double price = tradeSignal == TradeSignal.BUY_SIGNAL ? signalPrice.high() : signalPrice.low();

                    final Trade trade = openTrade(
                            tradeSignal.getTradeType(),
                            this.strategyParameters.getLotSize(),
                            currentPrice.date(),
                            price,
                            calculateDynamicVariance(signalPrice, tradeSignal.getTradeType()),
                            tradeSignal == TradeSignal.BUY_SIGNAL ? calculateActualLimit(signalPrice.getFullSize(true), price, true, true) : calculateActualLimit(signalPrice.getFullSize(true), price, false, true)
                    );

                    this.openTrades.put(trade.getId(), trade);
                    count += 1;
                }


                if (isExitBar(currentPrice)) {
                    //  close the trading day
                    closeDay(currentPrice, this.openTrades, this.closedTrades);
                } else {
                    //  check each market price to see if any of the open trades were hit
                    checkTrades(this.openTrades, this.closedTrades, currentPrice);
                }
            }
        }

        final StrategyResult<SproutStrategyParameters> result = new StrategyResult<>(this.strategyParameters, startDate, endDate, this.closedTrades.values(), this.strategyParameters.getBuyLimit(), this.strategyParameters.getSellLimit(), this.strategyParameters.getPricePerPoint(), this.strategyParameters.getInitialBalance());
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
     * Calculates the dynamically variant stop loss
     *
     * @param price signal price
     * @param tradeType {@link TradeType}
     * @return stop loss price
     */
    private double calculateDynamicVariance(final MarketPrice price, final TradeType tradeType) {

        double point;
        final double diff = this.mathService.multiply(Math.abs(this.mathService.subtract(price.high(), price.low())), this.strategyParameters.getVariance());

        if (tradeType == TradeType.BUY) {
            point = price.low();
        } else {
            point = price.high();
        }

        return tradeType == TradeType.BUY ? (point + diff) : (point - diff);
    }

    /**
     * Computes trade signals
     *
     * @param ref     reference price
     * @param signal  signal bar
     * @param current entry bar
     * @return {@link TradeSignal}
     */
    private TradeSignal getTradeSignal(final MarketPrice ref, final MarketPrice signal, final MarketPrice current) {

        // don't trade at EOD
        if (current.date().toLocalTime().getHour() == 16) {
            return TradeSignal.NO_SIGNAL;
        }

        final double refHigh = ref.high();
        final double refLow = ref.low();

        final double sigHigh = signal.high();
        final double sigLow = signal.low();

        //  look for mutually exclusive prices
        if (current.isMutuallyExclusive(signal) || signal.isMutuallyExclusive(ref)) {
            return TradeSignal.NO_SIGNAL;
        }

        if (sigLow < refLow && current.high() > sigHigh && hasConfirmation(TradeSignal.BUY_SIGNAL, ref, signal, current)) {
            return TradeSignal.BUY_SIGNAL;
        } else if (sigHigh > refHigh && current.low() < sigLow && hasConfirmation(TradeSignal.SELL_SIGNAL, ref, signal, current)) {
            return TradeSignal.SELL_SIGNAL;
        } else {
            return TradeSignal.NO_SIGNAL;
        }
    }

    /**
     * Computes the limit based on the given parameters. Pass -1 to not include the window calculation
     *
     * @param window            overrides limit if it is greater than the given limit
     * @param price             start price
     * @param shouldAdd         add or subtract
     * @param includeMultiplier multiply for tp
     * @return limit
     */
    private double calculateActualLimit(final double window, final double price, final boolean shouldAdd, final boolean includeMultiplier) {

        if (!includeMultiplier) {
            if (window < this.strategyParameters.getMinimumRisk()) {
                return calculateLimit(price, this.strategyParameters.getMinimumRisk(), shouldAdd);
            } else {
                return calculateLimit(price, window, shouldAdd);
            }
        }

        final double profitWindow = this.mathService.multiply(window, this.strategyParameters.getProfitMultiplier());
        if (profitWindow < this.strategyParameters.getMinimumReward()) {
            return calculateLimit(price, this.strategyParameters.getMinimumReward(), shouldAdd);
        } else if (profitWindow > this.strategyParameters.getAllowableReward()) {
            return calculateLimit(price, this.strategyParameters.getAllowableReward(), shouldAdd);
        } else {
            return calculateLimit(price, profitWindow, shouldAdd);
        }
    }

    /**
     * Calculates the confirmation of the given {@link TradeSignal}
     *
     * @param tradeSignal {@link TradeSignal}
     * @param ref         reference bar
     * @param signal      signal bar
     * @param current     current bar
     * @return true if trade is confirmed and valid to enter
     */
    private boolean hasConfirmation(final TradeSignal tradeSignal, final MarketPrice ref, final MarketPrice signal, final MarketPrice current) {

        if (tradeSignal == TradeSignal.BUY_SIGNAL) {
            return current.low() > signal.low() && signal.hasBullishIndication();
        } else if (tradeSignal == TradeSignal.SELL_SIGNAL) {
            return current.high() < signal.high() && signal.hasBearishIndication();
        }

        return false;
    }

    /**
     * Returns true if the given {@link LocalTime} is within the trading window
     *
     * @param localTime {@link LocalTime}
     * @return true if after 10:30 am and before 4:30 pm
     */
    private boolean isWithinWindow(final LocalTime localTime) {
        return (localTime.isAfter(LocalTime.of(9, 30))) && (localTime.isBefore(LocalTime.of(16, 30)));
    }
}
