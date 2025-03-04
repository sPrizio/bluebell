package com.bluebell.anther.models.parameter;

import com.bluebell.platform.enums.trade.TradeType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Class representation of a limit parameter, which can act as either a stop loss or take profit level
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
@AllArgsConstructor
public class LimitParameter {

    private TradeType tradeType;

    private double takeProfit;

    private double stopLoss;


    //  CONSTRUCTORS

    public LimitParameter() {
        this.tradeType = TradeType.NA;
        this.takeProfit = -1.0;
        this.stopLoss = -1.0;
    }


    //  METHODS

    @Override
    public String toString() {
        return """
                %s - takeProfit = %s, stopLoss = %s';
                """.formatted(this.tradeType, this.takeProfit, this.stopLoss);
    }
}
