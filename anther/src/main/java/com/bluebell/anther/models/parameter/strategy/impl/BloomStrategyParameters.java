package com.bluebell.anther.models.parameter.strategy.impl;

import com.bluebell.anther.models.parameter.strategy.StrategyParameters;
import com.bluebell.anther.strategies.impl.Bloom;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * {@link Bloom}-specific strategy parameters
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
@NoArgsConstructor
public class BloomStrategyParameters extends BasicStrategyParameters implements StrategyParameters {

    private double variance;

    private boolean normalize;

    private boolean breakEvenStop;

    private double absoluteProfitTarget;

    public BloomStrategyParameters(final double variance, final boolean normalize, final boolean breakEvenStop, final double absoluteProfitTarget, final BasicStrategyParameters basicStrategyParameters) {
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

        this.variance = variance;
        this.normalize = normalize;
        this.breakEvenStop = breakEvenStop;
        this.absoluteProfitTarget = absoluteProfitTarget;
    }


    //  METHODS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BloomStrategyParameters that = (BloomStrategyParameters) o;
        return Double.compare(this.variance, that.variance) == 0 && this.normalize == that.normalize && this.getStartHour() == that.getStartHour() && this.getStartMinute() == that.getStartMinute();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Double.hashCode(this.variance);
        result = 31 * result + Boolean.hashCode(this.normalize);
        result = 31 * result + Integer.hashCode(this.getStartHour());
        result = 31 * result + Integer.hashCode(this.getStartMinute());
        return result;
    }
}
