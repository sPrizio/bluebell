package com.bluebell.anther.strategies.impl;

import com.bluebell.anther.models.parameter.LimitParameter;
import com.bluebell.anther.models.parameter.strategy.impl.BloomStrategyParameters;
import com.bluebell.anther.models.strategy.StrategyResult;
import com.bluebell.anther.models.trade.AntherTrade;
import com.bluebell.anther.strategies.Strategy;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import com.bluebell.platform.models.core.nonentities.market.MarketPrice;
import com.bluebell.platform.services.MathService;
import lombok.Getter;
import org.javatuples.Pair;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The bloom strategy is a straddle. A long and short position are both opened at the opening of the day, each with a 3:1 Risk/Reward. In general,
 * based on the opening price principle, there's a 70% chance that the open of the day will be within 20% of the high or low of the day. Bloom aims
 * to capture this trend via straddling.
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
public class Bloom implements Strategy<BloomStrategyParameters> {

    private final MathService mathService = new MathService();

    private final BloomStrategyParameters strategyParameters;

    private final Map<String, AntherTrade> openTrades;

    private final Map<String, AntherTrade> closedTrades;

    private final Map<String, String> relatedTrades;


    //  CONSTRUCTORS

    public Bloom(final BloomStrategyParameters strategyParameters) {
        this.strategyParameters = strategyParameters;
        this.openTrades = new HashMap<>();
        this.closedTrades = new HashMap<>();
        this.relatedTrades = new HashMap<>();
    }


    //  METHODS

    @Override
    public StrategyResult<BloomStrategyParameters> executeStrategy(final LocalDate startDate, final LocalDate endDate, final Map<LocalDate, AggregatedMarketPrices> prices) {

        for (final Map.Entry<LocalDate, AggregatedMarketPrices> entry : prices.entrySet()) {

            if (entry.getKey().isBefore(startDate) || (entry.getKey().isAfter(endDate) || entry.getKey().isEqual(endDate))) {
                continue;
            }

            for (final MarketPrice marketPrice : entry.getValue().marketPrices()) {
                if (isSignalBar(marketPrice)) {
                    final AntherTrade tradeBuy = openTrade(
                            TradeType.BUY,
                            this.strategyParameters.getLotSize(),
                            marketPrice.date(),
                            marketPrice.open(),
                            calculateLimit(marketPrice.open(), normalize(TradeType.BUY).getValue1(), false),
                            calculateLimit(marketPrice.open(), normalize(TradeType.BUY).getValue0(), true)
                    );

                    final AntherTrade tradeSell = openTrade(
                            TradeType.SELL,
                            this.strategyParameters.getLotSize(),
                            marketPrice.date(),
                            marketPrice.open(),
                            calculateLimit(marketPrice.open(), normalize(TradeType.SELL).getValue1(), true),
                            calculateLimit(marketPrice.open(), normalize(TradeType.SELL).getValue0(), false)
                    );

                    this.openTrades.put(tradeBuy.getId(), tradeBuy);
                    this.openTrades.put(tradeSell.getId(), tradeSell);

                    if (this.strategyParameters.isBreakEvenStop()) {
                        this.relatedTrades.put(tradeBuy.getId(), tradeSell.getId());
                        this.relatedTrades.put(tradeSell.getId(), tradeBuy.getId());
                    }
                }

                //  check each market price to see if any of the open trades were hit
                checkRelatedTrades(this.openTrades, this.closedTrades, marketPrice);

                //  close the trading day
                closeDay(marketPrice, this.openTrades, this.closedTrades);
            }
        }

        final StrategyResult<BloomStrategyParameters> result = new StrategyResult<>(this.strategyParameters, startDate, endDate, this.closedTrades.values(), this.strategyParameters.getBuyLimit(), this.strategyParameters.getSellLimit(), this.strategyParameters.getPricePerPoint(), this.strategyParameters.getInitialBalance());
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
     * Looks for a related trade for the given trade
     *
     * @param trade {@link AntherTrade}
     * @return {@link Optional} {@link AntherTrade}
     */
    private Optional<AntherTrade> getRelatedTrade(final AntherTrade trade) {
        final AntherTrade result = this.openTrades.getOrDefault(this.relatedTrades.getOrDefault(trade.getId(), null), null);
        return Optional.ofNullable(result);
    }

    /**
     * Overrides the checkTrades logic to allow checking for related trades to update related trades based on the current trade's value
     *
     * @param openTrades open trades
     * @param closedTrades closed trades
     * @param marketPrice {@link MarketPrice}
     */
    private void checkRelatedTrades(final Map<String, AntherTrade> openTrades, final Map<String, AntherTrade> closedTrades, final MarketPrice marketPrice) {

        if (!this.strategyParameters.isBreakEvenStop()) {
            checkTrades(openTrades, closedTrades, marketPrice);
        } else {
            openTrades.forEach((key, value) -> {
                if (value.getTradeType() == TradeType.BUY) {
                    if (marketPrice.high() >= value.getTakeProfit()) {
                        // hit take profit
                        closeTrade(value, marketPrice.date(), value.getTakeProfit());
                        closedTrades.put(value.getId(), value);
                        this.relatedTrades.remove(value.getId());
                    } else if (marketPrice.low() <= value.getStopLoss()) {
                        // hit stop loss
                        closeTrade(value, marketPrice.date(), value.getStopLoss());
                        closedTrades.put(value.getId(), value);

                        final Optional<AntherTrade> relatedTrade = getRelatedTrade(value);
                        relatedTrade.ifPresent(trade -> trade.setStopLoss(this.mathService.add(trade.getOpenPrice(), 15.0)));
                        this.relatedTrades.remove(value.getId());
                    }
                } else {
                    if (marketPrice.low() <= value.getTakeProfit()) {
                        // hit take profit
                        closeTrade(value, marketPrice.date(), value.getTakeProfit());
                        closedTrades.put(value.getId(), value);
                        this.relatedTrades.remove(value.getId());
                    } else if (marketPrice.high() >= value.getStopLoss()) {
                        // hit stop loss
                        closeTrade(value, marketPrice.date(), value.getStopLoss());
                        closedTrades.put(value.getId(), value);

                        final Optional<AntherTrade> relatedTrade = getRelatedTrade(value);
                        relatedTrade.ifPresent(trade -> trade.setStopLoss(this.mathService.subtract(trade.getOpenPrice(), 15.0)));
                        this.relatedTrades.remove(value.getId());
                    }
                }
            });

            closedTrades.keySet().forEach(openTrades::remove);
        }
    }

    /**
     * Returns true if the given {@link MarketPrice} is equal to the start of day
     *
     * @param price {@link MarketPrice}
     * @return true if hour and minute are equal
     */
    private boolean isSignalBar(final MarketPrice price) {
        return price.date().getHour() == this.strategyParameters.getStartHour() && price.date().getMinute() == this.strategyParameters.getStartMinute();
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
                    this.mathService.multiply(this.strategyParameters.getBuyLimit().getTakeProfit(), this.strategyParameters.getVariance()),
                    this.mathService.multiply(this.strategyParameters.getBuyLimit().getStopLoss(), this.strategyParameters.getVariance())
            );
        }

        switch (tradeType) {
            case BUY -> {
                tp = this.strategyParameters.getBuyLimit().getTakeProfit();
                sl = this.strategyParameters.getSellLimit().getStopLoss();
            }
            case SELL -> {
                tp = this.strategyParameters.getSellLimit().getTakeProfit();
                sl = this.strategyParameters.getBuyLimit().getStopLoss();
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
            return this.strategyParameters.getBuyLimit();
        } else {
            return this.strategyParameters.getSellLimit();
        }
    }
}
