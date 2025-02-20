package com.bluebell.radicle;

import com.bluebell.platform.enums.time.PlatformTimeInterval;
import com.bluebell.radicle.parsers.impl.TradingViewDataParser;

/**
 * Executes the radicle module
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class Radicle {
    public static void main(String[] args) {
        TradingViewDataParser parser = new TradingViewDataParser(false, "US100");
        parser.parseMarketPricesByDate(PlatformTimeInterval.THIRTY_MINUTE);
    }
}