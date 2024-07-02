package com.bluebell.anther.models.parameter.strategy.impl;

import com.bluebell.anther.models.parameter.strategy.StrategyParameters;
import com.bluebell.anther.strategies.impl.Blossom;
import com.bluebell.radicle.indicators.impl.ExponentialMovingAverageIndicator;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Strategy parameters specific to {@link Blossom}
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
@Getter
@NoArgsConstructor
public class BlossomStrategyParameters extends BasicStrategyParameters implements StrategyParameters {

    private ExponentialMovingAverageIndicator ema;


    //  CONSTRUCTORS

    public BlossomStrategyParameters(final ExponentialMovingAverageIndicator ema, final BasicStrategyParameters basicStrategyParameters) {
        super(
                basicStrategyParameters.getDescription(),
                basicStrategyParameters.getBuyLimit(),
                basicStrategyParameters.getSellLimit(),
                basicStrategyParameters.getStartHour(),
                basicStrategyParameters.getStartMinute(),
                basicStrategyParameters.getLotSize(),
                basicStrategyParameters.getPricePerPoint(),
                basicStrategyParameters.isScaleProfits(),
                basicStrategyParameters.getInitialBalance()
        );

        this.ema = ema;
    }


    //  METHODS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BlossomStrategyParameters that = (BlossomStrategyParameters) o;
        return this.ema.equals(that.ema);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.ema.hashCode();
        return result;
    }
}
