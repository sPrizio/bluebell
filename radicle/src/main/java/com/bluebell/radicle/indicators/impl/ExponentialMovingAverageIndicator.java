package com.bluebell.radicle.indicators.impl;

import com.bluebell.radicle.indicators.Indicator;
import com.bluebell.radicle.models.MarketPrice;
import com.bluebell.radicle.services.MathService;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class representation of an EMA value
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
public class ExponentialMovingAverageIndicator implements Indicator {

    private final MathService mathService = new MathService();

    @Getter
    private final long period;


    //  CONSTRUCTORS

    public ExponentialMovingAverageIndicator(final long period) {
        this.period = period;
    }


    //  METHODS

    @Override
    public String getName() {
        return String.format("EMA_%d", this.period);
    }

    @Override
    public void computeValue(final Collection<MarketPrice> previousPrices, final MarketPrice currentPrice) {

        if (CollectionUtils.isEmpty(previousPrices) || this.period == 0.0) {
            currentPrice.setIndicatorValue(getName(), currentPrice.close());
            return;
        }

        final List<MarketPrice> previousList = new ArrayList<>(previousPrices);
        final MarketPrice previous = previousList.getLast();
        final double multiplier = this.mathService.divide(2.0, this.mathService.add(this.period, 1));
        final double ema = this.mathService.add(this.mathService.multiply(currentPrice.close(), multiplier), this.mathService.multiply(previous.getIndicatorValue(getName()), this.mathService.subtract(1.0, multiplier)));

        currentPrice.setIndicatorValue(getName(), ema);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExponentialMovingAverageIndicator that = (ExponentialMovingAverageIndicator) o;
        return this.period == that.period && getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(this.period);
        result = 31 * result + Long.hashCode(this.getName().hashCode());
        return result;
    }

}
