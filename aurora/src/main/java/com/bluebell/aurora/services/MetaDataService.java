package com.bluebell.aurora.services;

import com.bluebell.aurora.models.metadata.MetaData;
import com.bluebell.radicle.models.MarketPrice;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Computes metadata about a collection of market prices
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public class MetaDataService {

    /**
     * Computes metadata for the given time period with the given collection of market prices
     *
     * @param start {@link LocalDate}
     * @param end {@link LocalDate}
     * @param unit {@link ChronoUnit}
     * @param prices {@link Map} of {@link MarketPrice} indexed by their date
     * @return {@link MetaData}
     */
    public List<MetaData> getMetaData(final LocalDate start, final LocalDate end, final ChronoUnit unit, final Map<LocalDate, TreeSet<MarketPrice>> prices) {

        final List<MetaData> metaData = new ArrayList<>();
        LocalDate compare = start;
        while (compare.isBefore(end)) {
            metaData.add(new MetaData(compare, compare.plus(1, unit), getPrices(compare, compare.plus(1, unit), prices)));
            compare = compare.plus(1, unit);
        }

        return metaData.stream().filter(md -> md.getAverageChange() > 0.0).toList();
    }


    //  HELPERS

    /**
     * Returns the correct {@link MarketPrice} to aggregate
     *
     * @param start start
     * @param end end
     * @param prices collection of market prices
     * @return {@link SortedSet}
     */
    private SortedSet<MarketPrice> getPrices(final LocalDate start, final LocalDate end, final Map<LocalDate, TreeSet<MarketPrice>> prices) {

        if (prices.containsKey(start)) {
            return prices.get(start);
        }

        final TreeSet<MarketPrice> set = new TreeSet<>();
        prices.forEach((date, marketPrices) -> {
            if ((date.isEqual(start) || date.isAfter(start)) && (date.isEqual(end) || date.isBefore(end))) {
                set.addAll(marketPrices);
            }
        });

        return set;
    }
}
