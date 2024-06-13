package com.bluebell.aurora.models.parameter;

import com.bluebell.aurora.enums.TradeType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Class representation of a limit parameter, which can act as either a stop loss or take profit level
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
@AllArgsConstructor
public class LimitParameter {

    private TradeType tradeType;

    private double takeProfit;

    private double stopLoss;


    //  METHODS

    @Override
    public String toString() {
        return """
                %s - takeProfit = %s, stopLoss = %s';
                """.formatted(this.tradeType, this.takeProfit, this.stopLoss);
    }
}
