package com.bluebell.planter.core.services.chart.impl;

import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.chart.IntradayInterval;
import com.bluebell.planter.core.models.nonentities.apexcharts.ApexChartCandleStick;
import com.bluebell.planter.core.services.chart.ChartService;
import com.bluebell.radicle.enums.RadicleTimeInterval;
import com.bluebell.radicle.models.AggregatedMarketPrices;
import com.bluebell.radicle.models.MarketPrice;
import com.bluebell.radicle.parsers.impl.FirstRateDataParser;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.bluebell.planter.core.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.bluebell.planter.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * apexcharts implementation of {@link ChartService}
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@Service
public class ApexChartService implements ChartService<ApexChartCandleStick> {

    private FirstRateDataParser firstRateDataParser = new FirstRateDataParser(false);


    //  METHODS

    @Override
    public List<ApexChartCandleStick> getChartData(final LocalDate startDate, final LocalDate endDate, final IntradayInterval timeInterval) {

        validateParameterIsNotNull(startDate, CoreConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(endDate, CoreConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(startDate.atStartOfDay(), endDate.atStartOfDay(), CoreConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        validateParameterIsNotNull(timeInterval, "timeInterval cannot be null");

        final Map<LocalDate, AggregatedMarketPrices> collection;
        switch (timeInterval) {
            case ONE_MINUTE -> collection = new HashMap<>(this.firstRateDataParser.parseMarketPricesByDate(RadicleTimeInterval.ONE_MINUTE));
            case FIVE_MINUTES -> collection = new HashMap<>(this.firstRateDataParser.parseMarketPricesByDate(RadicleTimeInterval.FIVE_MINUTE));
            case TEN_MINUTES -> collection = new HashMap<>(this.firstRateDataParser.parseMarketPricesByDate(RadicleTimeInterval.TEN_MINUTE));
            case FIFTEEN_MINUTES -> collection = new HashMap<>(this.firstRateDataParser.parseMarketPricesByDate(RadicleTimeInterval.FIFTEEN_MINUTE));
            case THIRTY_MINUTES -> collection = new HashMap<>(this.firstRateDataParser.parseMarketPricesByDate(RadicleTimeInterval.THIRTY_MINUTE));
            case ONE_HOUR -> collection = new HashMap<>(this.firstRateDataParser.parseMarketPricesByDate(RadicleTimeInterval.ONE_HOUR));
            case ONE_DAY -> collection = new HashMap<>(this.firstRateDataParser.parseMarketPricesByDate(RadicleTimeInterval.ONE_DAY));
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
                    candleSticks.add(new ApexChartCandleStick(key.atStartOfDay(ZoneId.of(CoreConstants.EASTERN_TIMEZONE)).toEpochSecond(), new double[0]));
                } else {
                    values.marketPrices().forEach(val -> candleSticks.add(new ApexChartCandleStick(val.date().atZone(ZoneId.of(CoreConstants.EASTERN_TIMEZONE)).toInstant().toEpochMilli(), new double[]{val.open(), val.high(), val.low(), val.close()})));
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
