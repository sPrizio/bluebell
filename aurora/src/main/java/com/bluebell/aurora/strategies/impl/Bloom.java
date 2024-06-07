package com.bluebell.aurora.strategies.impl;

import com.bluebell.aurora.enums.TradeType;
import com.bluebell.aurora.models.parameter.LimitParameter;
import com.bluebell.aurora.models.strategy.StrategyResult;
import com.bluebell.aurora.models.parameter.strategy.impl.BloomStrategyParameters;
import com.bluebell.aurora.models.trade.Trade;
import com.bluebell.aurora.strategies.Strategy;
import com.bluebell.core.services.MathService;
import com.bluebell.radicle.models.MarketPrice;
import lombok.Getter;
import org.javatuples.Pair;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * The bloom strategy is a straddle. A long and short position are both opened at the opening of the day, each with a 3:1 Risk/Reward. In general,
 * based on the opening price principle, there's a 70% chance that the open of the day will be within 20% of the high or low of the day. Bloom aims
 * to capture this trend via straddling.
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
public class Bloom implements Strategy {

    private final MathService mathService = new MathService();

    private final BloomStrategyParameters strategyParameters;

    private final Map<String, Trade> openTrades;

    private final Map<String, Trade> closedTrades;


    //  CONSTRUCTORS

    public Bloom(final BloomStrategyParameters strategyParameters) {
        this.strategyParameters = strategyParameters;
        this.openTrades = new HashMap<>();
        this.closedTrades = new HashMap<>();
    }


    //  METHODS

    @Override
    public StrategyResult executeStrategy(final LocalDate startDate, final LocalDate endDate, final Map<LocalDate, TreeSet<MarketPrice>> prices) {

        for (final Map.Entry<LocalDate, TreeSet<MarketPrice>> entry : prices.entrySet()) {

            if (entry.getKey().isBefore(startDate) || (entry.getKey().isAfter(endDate) || entry.getKey().isEqual(endDate))) {
                continue;
            }

            for (final MarketPrice marketPrice : entry.getValue()) {
                if (isSignalBar(marketPrice)) {
                    final Trade tradeBuy = openTrade(
                            TradeType.BUY,
                            this.strategyParameters.getBasicStrategyParameters().getLotSize(),
                            marketPrice.date(),
                            marketPrice.open(),
                            calculateLimit(marketPrice.open(), normalize(TradeType.BUY).getValue1(), false),
                            calculateLimit(marketPrice.open(), normalize(TradeType.BUY).getValue0(), true)
                    );

                    final Trade tradeSell = openTrade(
                            TradeType.SELL,
                            this.strategyParameters.getBasicStrategyParameters().getLotSize(),
                            marketPrice.date(),
                            marketPrice.open(),
                            calculateLimit(marketPrice.open(), normalize(TradeType.SELL).getValue1(), true),
                            calculateLimit(marketPrice.open(), normalize(TradeType.SELL).getValue0(), false)
                    );

                    this.openTrades.put(tradeBuy.getId(), tradeBuy);
                    this.openTrades.put(tradeSell.getId(), tradeSell);
                }

                //  check each market price to see if any of the open trades were hit
                checkTrades(this.openTrades, this.closedTrades, marketPrice);

                if (isExitBar(marketPrice) && !this.openTrades.isEmpty()) {
                    this.openTrades.forEach((key, trade) -> {
                        closeTrade(trade, marketPrice.date(), marketPrice.open());
                        this.closedTrades.put(trade.getId(), trade);
                    });

                    this.closedTrades.keySet().forEach(this.openTrades::remove);
                }
            }
        }

        final StrategyResult result = new StrategyResult(startDate, endDate, this.closedTrades.values(), this.strategyParameters.getBasicStrategyParameters().getBuyLimit(), this.strategyParameters.getBasicStrategyParameters().getSellLimit(), this.strategyParameters.getBasicStrategyParameters().getPricePerPoint());
        this.openTrades.clear();
        this.closedTrades.clear();

        return result;
    }


    //  HELPERS

    /**
     * Returns true if the given {@link MarketPrice} is equal to the start of day
     *
     * @param price {@link MarketPrice}
     * @return true if hour and minute are equal
     */
    private boolean isSignalBar(final MarketPrice price) {
        return price.date().getHour() == this.strategyParameters.getBasicStrategyParameters().getStartHour() && price.date().getMinute() == this.strategyParameters.getBasicStrategyParameters().getStartMinute();
    }

    /**
     * Returns true if the given {@link MarketPrice} is equal to the end of day
     *
     * @param price {@link MarketPrice}
     * @return true if hour and minute are equal
     */
    private boolean isExitBar(final MarketPrice price) {
        return price.date().getHour() == 16 && price.date().getMinute() == 0;
    }

    /**
     * Attempts to normalize the stop loss and take profits to ensure the absolute profit target is maintained
     *
     * @param tradeType {@link TradeType}
     * @return new stop loss and take profit levels
     */
    private Pair<Double, Double> normalize(final TradeType tradeType) {

        final double tp;
        final double sl;

        if (!this.strategyParameters.isNormalize()) {
            return Pair.with(
                    this.mathService.multiply(this.strategyParameters.getBasicStrategyParameters().getBuyLimit().getTakeProfit(), this.strategyParameters.getVariance()),
                    this.mathService.multiply(this.strategyParameters.getBasicStrategyParameters().getBuyLimit().getStopLoss(), this.strategyParameters.getVariance())
            );
        }

        switch (tradeType) {
            case TradeType.BUY -> {
                tp = this.strategyParameters.getBasicStrategyParameters().getBuyLimit().getTakeProfit();
                sl = this.strategyParameters.getBasicStrategyParameters().getSellLimit().getStopLoss();
            }
            case TradeType.SELL -> {
                tp = this.strategyParameters.getBasicStrategyParameters().getSellLimit().getTakeProfit();
                sl = this.strategyParameters.getBasicStrategyParameters().getBuyLimit().getStopLoss();
            }
            default -> {
                return Pair.with(0.0, 0.0);
            }
        }

        final double difference = this.mathService.subtract(tp, sl);
        final double absoluteDifference = this.mathService.subtract(this.strategyParameters.getAbsoluteProfitTarget(), difference);
        if (difference > this.strategyParameters.getAbsoluteProfitTarget()) {
            return Pair.with(
                    this.mathService.multiply(this.mathService.subtract(getLimitParameterForTradeType(tradeType).getTakeProfit(), absoluteDifference), this.strategyParameters.getVariance()),
                    this.mathService.multiply(getLimitParameterForTradeType(tradeType).getStopLoss(), this.strategyParameters.getVariance())
            );
        } else if (difference < this.strategyParameters.getAbsoluteProfitTarget()) {
            return Pair.with(
                    this.mathService.multiply(this.mathService.add(getLimitParameterForTradeType(tradeType).getTakeProfit(), absoluteDifference), this.strategyParameters.getVariance()),
                    this.mathService.multiply(getLimitParameterForTradeType(tradeType).getStopLoss(), this.strategyParameters.getVariance())
            );
        } else {
            return Pair.with(
                    this.mathService.multiply(getLimitParameterForTradeType(tradeType).getTakeProfit(), this.strategyParameters.getVariance()),
                    this.mathService.multiply(getLimitParameterForTradeType(tradeType).getStopLoss(), this.strategyParameters.getVariance())
            );
        }
    }


    /**
     * Obtains the correct limit parameter for the {@link TradeType}
     *
     * @param tradeType {@link TradeType}
     * @return {@link LimitParameter}
     */
    private LimitParameter getLimitParameterForTradeType(final TradeType tradeType) {
        if (tradeType == TradeType.BUY) {
            return this.strategyParameters.getBasicStrategyParameters().getBuyLimit();
        } else {
            return this.strategyParameters.getBasicStrategyParameters().getSellLimit();
        }
    }
}
