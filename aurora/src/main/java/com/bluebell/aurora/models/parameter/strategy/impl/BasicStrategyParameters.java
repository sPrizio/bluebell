package com.bluebell.aurora.models.parameter.strategy.impl;

import com.bluebell.aurora.models.parameter.LimitParameter;
import com.bluebell.aurora.models.parameter.strategy.StrategyParameters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Static parameters that don't change for anything
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasicStrategyParameters implements StrategyParameters {

    private String description;

    private LimitParameter buyLimit;

    private LimitParameter sellLimit;

    private int startHour;

    private int startMinute;

    private double lotSize;

    private double pricePerPoint;

    private boolean scaleProfits;

    private double initialBalance;
}
