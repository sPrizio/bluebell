package com.bluebell.anther.models.parameter.strategy.impl;

import com.bluebell.anther.models.parameter.strategy.StrategyParameters;
import com.bluebell.anther.strategies.impl.Sprout;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * {@link Sprout}-specific strategy parameters
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
@NoArgsConstructor
public class SproutStrategyParameters extends BasicStrategyParameters implements StrategyParameters {

    private double profitMultiplier;

    private double allowableRisk;

    private double minimumRisk;

    public SproutStrategyParameters(final double profitMultiplier, final double allowableRisk, final double minimumRisk, final BasicStrategyParameters basicStrategyParameters) {
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

        this.profitMultiplier = profitMultiplier;
        this.allowableRisk = allowableRisk;
        this.minimumRisk = minimumRisk;
    }


    //  METHODS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SproutStrategyParameters that = (SproutStrategyParameters) o;
        return Double.compare(this.profitMultiplier, that.profitMultiplier) == 0 && Double.compare(this.allowableRisk, that.allowableRisk) == 0;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Double.hashCode(this.profitMultiplier);
        result = 31 * result + Double.hashCode(this.allowableRisk);
        return result;
    }
}
