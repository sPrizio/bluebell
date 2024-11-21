package com.bluebell.radicle;

import com.bluebell.radicle.enums.RadicleTimeInterval;
import com.bluebell.radicle.parsers.impl.TradingViewDataParser;

public class Radicle {
    public static void main(String[] args) {
        TradingViewDataParser parser = new TradingViewDataParser(false, "US100");
        parser.parseMarketPricesByDate(RadicleTimeInterval.THIRTY_MINUTE);
    }
}