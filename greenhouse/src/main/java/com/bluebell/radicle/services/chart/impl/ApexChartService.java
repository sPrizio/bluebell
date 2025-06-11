package com.bluebell.radicle.services.chart.impl;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.models.core.nonentities.apexcharts.ApexChartCandleStick;
import com.bluebell.radicle.services.chart.ChartService;
import com.bluebell.radicle.services.market.MarketPriceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;


/**
 * apexcharts implementation of {@link ChartService}
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Service
public class ApexChartService implements ChartService<ApexChartCandleStick> {

    @Resource(name = "marketPriceService")
    private MarketPriceService marketPriceService;


    //  METHODS

    @Override
    public List<ApexChartCandleStick> getChartDataForTrade(final Trade trade, final MarketPriceTimeInterval timeInterval) {
        validateParameterIsNotNull(trade, CorePlatformConstants.Validation.Trade.TRADE_CANNOT_BE_NULL);
        validateParameterIsNotNull(timeInterval, CorePlatformConstants.Validation.System.TIME_INTERVAL_CANNOT_BE_NULL);

        return convertToCandleSticks(this.marketPriceService.findMarketPricesForTrade(trade, timeInterval));
    }


    //  HELPERS

    /**
     * Converts a list of market prices to a list of candle sticks
     *
     * @param marketPrices {@link List} of {@link MarketPrice}
     * @return {@link List} of {@link ApexChartCandleStick}
     */
    private List<ApexChartCandleStick> convertToCandleSticks(final List<MarketPrice> marketPrices) {
        return marketPrices
                .stream()
                .map(
                        mp ->
                                ApexChartCandleStick
                                        .builder()
                                        .x(mp.getDate().atZone(ZoneId.of(CorePlatformConstants.EASTERN_TIMEZONE)).toInstant().toEpochMilli())
                                        .y(new double[]{mp.getOpen(), mp.getHigh(), mp.getLow(), mp.getClose()})
                                        .build())
                .toList();
    }
}
