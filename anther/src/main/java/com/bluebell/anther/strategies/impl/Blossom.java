package com.bluebell.anther.strategies.impl;

import com.bluebell.anther.enums.TradeType;
import com.bluebell.anther.models.parameter.strategy.impl.BlossomStrategyParameters;
import com.bluebell.anther.models.strategy.StrategyResult;
import com.bluebell.anther.models.trade.Trade;
import com.bluebell.anther.strategies.Strategy;
import com.bluebell.radicle.enums.CrossOver;
import com.bluebell.radicle.models.AggregatedMarketPrices;
import com.bluebell.radicle.models.MarketPrice;
import org.javatuples.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ripen strategy. Essentially an EMA crossover strategy
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
public class Blossom implements Strategy<BlossomStrategyParameters> {

    private final BlossomStrategyParameters strategyParameters;

    private final Map<String, Trade> openTrades;

    private final Map<String, Trade> closedTrades;


    //  CONSTRUCTORS

    public Blossom(final BlossomStrategyParameters strategyParameters) {
        this.strategyParameters = strategyParameters;
        this.openTrades = new HashMap<>();
        this.closedTrades = new HashMap<>();
    }


    //  METHODS

    @Override
    public StrategyResult<BlossomStrategyParameters> executeStrategy(LocalDate startDate, LocalDate endDate, Map<LocalDate, AggregatedMarketPrices> prices) {

        for (final Map.Entry<LocalDate, AggregatedMarketPrices> entry : prices.entrySet()) {
            if (entry.getKey().isBefore(startDate) || (entry.getKey().isAfter(endDate) || entry.getKey().isEqual(endDate))) {
                continue;
            }

            final List<MarketPrice> list = new ArrayList<>(entry.getValue().marketPrices());
            for (int i = 1; i < entry.getValue().marketPrices().size(); i++) {
                final MarketPrice previous = list.get(i - 1);
                final MarketPrice marketPrice = list.get(i);

                if (isSignalBar(previous) && this.openTrades.isEmpty()) {
                    final CrossOver crossOver = marketPrice.getCrossOverStatus(this.strategyParameters.getEma().getName());
                    final TradeType tradeType = crossOver.equals(CrossOver.CROSS_ABOVE) ? TradeType.BUY : TradeType.SELL;

                    final Trade trade = openTrade(
                            tradeType,
                            this.strategyParameters.getLotSize(),
                            marketPrice.date(),
                            marketPrice.open(),
                            //sl
                            calculateLimit(marketPrice.open(), getLimitParameter(tradeType).getValue0(), !tradeType.equals(TradeType.BUY)),
                            //tp
                            calculateLimit(marketPrice.open(), getLimitParameter(tradeType).getValue1(), tradeType.equals(TradeType.BUY))
                    );

                    this.openTrades.put(trade.getId(), trade);
                }

                //  check each market price to see if any of the open trades were hit
                checkTrades(this.openTrades, this.closedTrades, marketPrice);
                if (isExitBar(marketPrice)) {
                    cleanExcessTrades(this.openTrades, this.closedTrades, marketPrice);
                }
            }
        }

        final StrategyResult<BlossomStrategyParameters> result = new StrategyResult<>(this.strategyParameters, startDate, endDate, this.closedTrades.values(), this.strategyParameters.getBuyLimit(), this.strategyParameters.getSellLimit(), this.strategyParameters.getPricePerPoint(), this.strategyParameters.isScaleProfits(), this.strategyParameters.getInitialBalance());
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
        return !price.getCrossOverStatus(this.strategyParameters.getEma().getName()).equals(CrossOver.NOT_APPLICABLE);
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
     * Returns the correct limits based on the trade type
     *
     * @param tradeType {@link TradeType}
     * @return {@link Pair}
     */
    private Pair<Double, Double> getLimitParameter(final TradeType tradeType) {
        if (tradeType.equals(TradeType.BUY)) {
            return Pair.with(this.strategyParameters.getBuyLimit().getStopLoss(), this.strategyParameters.getBuyLimit().getTakeProfit());
        } else {
            return Pair.with(this.strategyParameters.getSellLimit().getStopLoss(), this.strategyParameters.getSellLimit().getTakeProfit());
        }
    }
}
