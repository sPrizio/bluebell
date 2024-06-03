package com.bluebell.aurora.models.strategy.parameter.impl;

import com.bluebell.aurora.models.strategy.parameter.StrategyParameters;

/**
 * Static parameters that don't change for anything
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public record StaticStrategyParameters(int targetProfit, int stopLoss, int startHour, int startMinute, double lotSize, double pricePerPoint) implements StrategyParameters {}
