package com.bluebell.radicle.enums;

import com.bluebell.platform.enums.GenericEnum;
import lombok.Getter;

/**
 * Enum representing the different data sources for historical trading data
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Getter
public enum DataSource implements GenericEnum<DataSource> {
    FIRST_RATE_DATA("FIRST_RATE_DATA", "First Rate Data", "firstratedata"),
    METATRADER4("METATRADER4", "Metatrader 4", "mt4"),
    METATRADER5("METATRADER5", "Metatrader 5", "mt5"),
    TRADING_VIEW("TRADING_VIEW", "Trading View", "tradingview"),
    BLUEBELL("BLUEBELL", "Bluebell", "bluebell"),
    CTRADER("CTRADER", "cTrader", "ctrader");

    private final String code;

    private final String label;

    private final String dataRoot;

    DataSource(final String code, final String label, final String dataRoot) {
        this.code = code;
        this.label = label;
        this.dataRoot = dataRoot;
    }
}
