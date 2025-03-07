package com.bluebell.anther.models.parameter;

import com.bluebell.platform.enums.trade.TradeType;
import lombok.Builder;
import lombok.Getter;

/**
 * Class representation of a limit parameter, which can act as either a stop loss or take profit level
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
@Builder
public class LimitParameter {

    private @Builder.Default TradeType tradeType = TradeType.NA;

    private @Builder.Default double takeProfit = -1.0;

    private @Builder.Default double stopLoss = -1.0;


    //  METHODS

    @Override
    public String toString() {
        return """
                %s - takeProfit = %s, stopLoss = %s';
                """.formatted(this.tradeType, this.takeProfit, this.stopLoss);
    }
}
