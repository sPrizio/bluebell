package com.bluebell.anther.models.parameter.strategy.impl;

import com.bluebell.anther.models.parameter.LimitParameter;
import com.bluebell.anther.models.parameter.strategy.StrategyParameters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Static parameters that don't change for anything
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasicStrategyParameters implements StrategyParameters {

    private String description;

    private LimitParameter buyLimit;

    private LimitParameter sellLimit;

    private double lotSize;

    private double pricePerPoint;

    private double initialBalance;


    //  METHODS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicStrategyParameters that = (BasicStrategyParameters) o;
        return this.description.equals(that.description);
    }

    @Override
    public int hashCode() {
        int result = this.description.hashCode();
        result = (int) (31 * result + this.lotSize);
        return result;
    }
}
