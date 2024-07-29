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
                if (tradeSignal != TradeSignal.NO_SIGNAL && hasConfirmation(tradeSignal, referencePrice, signalPrice, currentPrice)) {
                    final double price = tradeSignal == TradeSignal.BUY_SIGNAL ? signalPrice.high() : signalPrice.low();

                    if (signalPrice.getBodySize(true) <= this.strategyParameters.getAllowableRisk()) {
                        final Trade trade = openTrade(
                                tradeSignal.getTradeType(),
                                this.strategyParameters.getLotSize(),
                                currentPrice.date(),
                                price,
                                tradeSignal == TradeSignal.BUY_SIGNAL ? calculateActualLimit(signalPrice.getFullSize(true), price, false, false) : calculateActualLimit(signalPrice.getFullSize(true), price, true, false),
                                tradeSignal == TradeSignal.BUY_SIGNAL ? calculateActualLimit(signalPrice.getFullSize(true), price, true, true) : calculateActualLimit(signalPrice.getFullSize(true), price, false, true)
                        );

                        this.openTrades.put(trade.getId(), trade);
                    }
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

        // don't trade at EOD
        if (current.date().toLocalTime().getHour() == 16) {
            return TradeSignal.NO_SIGNAL;
        }

        TradeSignal result = TradeSignal.NO_SIGNAL;
        if (signal.high() > ref.high() && current.low() < signal.low()) {
            result = TradeSignal.SELL_SIGNAL;
        } else if (signal.low() < ref.low() && current.high() > signal.high()) {
            result = TradeSignal.BUY_SIGNAL;
        }

        return result;
    }

    /**
     * Returns true if a new high/low was set (returns false if a new high and a new low were set at the same time)
     *
     * @param isHigh if true, look for highs
     * @param ref reference price
     * @param signal signal price
     * @return true if new high/low was set
     */
    private boolean newMarker(final boolean isHigh, final MarketPrice ref, final MarketPrice signal) {
        if (!isHigh) {
            return signal.high() > ref.high() && signal.low() > ref.low();
        } else {
            return signal.low() < ref.low() && signal.high() < ref.high();
        }
    }

    /**
     * Computes the limit based on the given parameters. Pass -1 to not include the window calculation
     *
     * @param window    overrides limit if it is greater than the given limit
     * @param price     start price
     * @param shouldAdd add or subtract
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
        } else {
            return calculateLimit(price, profitWindow, shouldAdd);
        }
    }

    /**
     * Calculates the confirmation of the given {@link TradeSignal}
     *
     * @param tradeSignal {@link TradeSignal}
     * @param ref reference bar
     * @param signal signal bar
     * @param current current bar
     * @return true if trade is confirmed and valid to enter
     */
    private boolean hasConfirmation(final TradeSignal tradeSignal, final MarketPrice ref, final MarketPrice signal, final MarketPrice current) {

        if (tradeSignal == TradeSignal.BUY_SIGNAL) {
            return signal.hasBullishIndication();
        } else if (tradeSignal == TradeSignal.SELL_SIGNAL) {
            return signal.hasBearishIndication();
        }

        return false;
    }
}
