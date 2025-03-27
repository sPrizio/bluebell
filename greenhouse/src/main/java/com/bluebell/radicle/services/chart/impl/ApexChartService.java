package com.bluebell.radicle.services.chart.impl;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.nonentities.apexcharts.ApexChartCandleStick;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.radicle.parsers.impl.FirstRateDataParser;
import com.bluebell.radicle.services.chart.ChartService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.bluebell.radicle.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;


/**
 * apexcharts implementation of {@link ChartService}
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
@Service
public class ApexChartService implements ChartService<ApexChartCandleStick> {

    private FirstRateDataParser firstRateDataParser = new FirstRateDataParser(false);


    //  METHODS

    @Override
    public List<ApexChartCandleStick> getChartData(final LocalDate startDate, final LocalDate endDate, final MarketPriceTimeInterval timeInterval) {

        validateParameterIsNotNull(startDate, CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(endDate, CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(startDate.atStartOfDay(), endDate.atStartOfDay(), CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        validateParameterIsNotNull(timeInterval, "timeInterval cannot be null");

        final Map<LocalDate, AggregatedMarketPrices> collection;
        switch (timeInterval) {
            case ONE_MINUTE -> collection = new HashMap<>(this.firstRateDataParser.parseMarketPricesByDate(MarketPriceTimeInterval.ONE_MINUTE));
            case FIVE_MINUTE -> collection = new HashMap<>(this.firstRateDataParser.parseMarketPricesByDate(MarketPriceTimeInterval.FIVE_MINUTE));
            case TEN_MINUTE -> collection = new HashMap<>(this.firstRateDataParser.parseMarketPricesByDate(MarketPriceTimeInterval.TEN_MINUTE));
            case FIFTEEN_MINUTE -> collection = new HashMap<>(this.firstRateDataParser.parseMarketPricesByDate(MarketPriceTimeInterval.FIFTEEN_MINUTE));
            case THIRTY_MINUTE -> collection = new HashMap<>(this.firstRateDataParser.parseMarketPricesByDate(MarketPriceTimeInterval.THIRTY_MINUTE));
            case ONE_HOUR -> collection = new HashMap<>(this.firstRateDataParser.parseMarketPricesByDate(MarketPriceTimeInterval.ONE_HOUR));
            case ONE_DAY -> collection = new HashMap<>(this.firstRateDataParser.parseMarketPricesByDate(MarketPriceTimeInterval.ONE_DAY));
            default -> collection = new HashMap<>();
        }

        return convertToCandleSticks(startDate, endDate, collection);
    }


    //  HELPERS

    /**
     * Converts the market prices into a list of candlesticks appropriate for apexcharts
     *
     * @param prices {@link Map} of {@link MarketPrice}
     * @return {@link List} of {@link ApexChartCandleStick}
     */
    private List<ApexChartCandleStick> convertToCandleSticks(final LocalDate startDate, final LocalDate endDate, final Map<LocalDate, AggregatedMarketPrices> prices) {

        if (MapUtils.isEmpty(prices)) {
            return Collections.emptyList();
        }

        final List<ApexChartCandleStick> candleSticks = new ArrayList<>();
        prices.forEach((key, values) -> {
            if ((key.isEqual(startDate) || key.isAfter(startDate)) && (key.isBefore(endDate))) {
                if (CollectionUtils.isEmpty(values.marketPrices())) {
                    candleSticks.add(ApexChartCandleStick.builder().x(key.atStartOfDay(ZoneId.of(CorePlatformConstants.EASTERN_TIMEZONE)).toEpochSecond()).y(new double[0]).build());
                } else {
                    values.marketPrices().forEach(val -> candleSticks.add(ApexChartCandleStick.builder().x(val.getDate().atZone(ZoneId.of(CorePlatformConstants.EASTERN_TIMEZONE)).toInstant().toEpochMilli()).y(new double[]{val.getOpen(), val.getHigh(), val.getLow(), val.getClose()}).build()));
                }
            }
        });

        return candleSticks.stream().sorted(Comparator.comparing(ApexChartCandleStick::getX)).toList();
    }


    //  MUTATORS

    public void setTest(final boolean test) {
        this.firstRateDataParser = new FirstRateDataParser(test);
    }
}
