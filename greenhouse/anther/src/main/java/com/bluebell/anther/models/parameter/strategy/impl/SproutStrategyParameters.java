package com.bluebell.anther.models.parameter.strategy.impl;

import com.bluebell.anther.models.parameter.strategy.StrategyParameters;
import com.bluebell.anther.strategies.impl.Sprout;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * {@link Sprout}-specific strategy parameters
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
@NoArgsConstructor
public class SproutStrategyParameters extends BasicStrategyParameters implements StrategyParameters {

    private double profitMultiplier;

    private double allowableRisk;

    private double allowableReward;

    private double minimumRisk;

    private double minimumReward;

    private double variance;

    public SproutStrategyParameters(final double profitMultiplier, final double allowableRisk, final double allowableReward, final double minimumRisk, final double minimumReward, final double variance, final BasicStrategyParameters basicStrategyParameters) {
        super(
                basicStrategyParameters.getDescription(),
                basicStrategyParameters.getBuyLimit(),
                basicStrategyParameters.getSellLimit(),
                basicStrategyParameters.getLotSize(),
                basicStrategyParameters.getPricePerPoint(),
                basicStrategyParameters.getInitialBalance()
        );

        this.profitMultiplier = profitMultiplier;
        this.allowableRisk = allowableRisk;
        this.allowableReward = allowableReward;
        this.minimumRisk = minimumRisk;
        this.minimumReward = minimumReward;
        this.variance = variance;
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
