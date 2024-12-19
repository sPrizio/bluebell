package com.bluebell.planter.core.services.analysis;

import com.bluebell.planter.core.enums.analysis.AnalysisFilter;
import com.bluebell.planter.core.enums.analysis.TradeDurationFilter;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.entities.trade.Trade;
import com.bluebell.planter.core.models.nonentities.records.analysis.AnalysisResult;
import com.bluebell.radicle.enums.RadicleTimeInterval;
import com.bluebell.radicle.services.MathService;
import org.apache.commons.collections4.CollectionUtils;
import org.javatuples.Triplet;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Service-layer for computing analysis of {@link Account}s
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@Service
public class AnalysisService {

    private static final String KEY_PATTERN = "HH:mm";
    private final MathService mathService = new MathService();


    //  METHODS

    /**
     * Computes the time bucket analysis
     *
     * @param account        {@link Account}
     * @param interval       {@link RadicleTimeInterval}
     * @param analysisFilter {@link AnalysisFilter}
     * @param isOpened       opened or closed times
     * @return {@link List} of {@link AnalysisResult}
     */
    public List<AnalysisResult> computeTimeBucketAnalysis(final Account account, final RadicleTimeInterval interval, final AnalysisFilter analysisFilter, final boolean isOpened) {

        final List<Trade> trades = account.getTrades().stream().sorted(Comparator.comparing(Trade::getTradeCloseTime)).toList();
        final Map<String, Triplet<Integer, Double, List<Trade>>> map = generateTimeBucketMap(trades, interval, analysisFilter, isOpened);

        return
                map
                        .entrySet()
                        .stream()
                        .map(e -> new AnalysisResult(e.getKey(), e.getValue().getValue1(), e.getValue().getValue0()))
                        .sorted()
                        .toList();
    }

    /**
     * Computes the weekday analysis
     *
     * @param account        {@link Account}
     * @param analysisFilter {@link AnalysisFilter}
     * @return {@link List} of {@link AnalysisResult}
     */
    public List<AnalysisResult> computeWeekdayAnalysis(final Account account, final AnalysisFilter analysisFilter) {
        final Map<DayOfWeek, Triplet<Integer, Double, List<Trade>>> map = generateWeekdayMap(account.getTrades().stream().sorted(Comparator.comparing(Trade::getTradeCloseTime)).toList(), analysisFilter);
        return
                map
                        .entrySet()
                        .stream()
                        .map(e -> new AnalysisResult(e.getKey().getDisplayName(TextStyle.FULL, Locale.CANADA), e.getValue().getValue1(), e.getValue().getValue0()))
                        .sorted(Comparator.comparing(e -> DayOfWeek.valueOf(e.label().toUpperCase())))
                        .toList();
    }

    /**
     * Computes the time bucket / week day analysis
     *
     * @param account        {@link Account}
     * @param day            {@link DayOfWeek}
     * @param interval       {@link RadicleTimeInterval}
     * @param analysisFilter {@link AnalysisFilter}
     * @return {@link List} of {@link AnalysisResult}
     */
    public List<AnalysisResult> computeWeekdayTimeBucketAnalysis(final Account account, final DayOfWeek day, final RadicleTimeInterval interval, final AnalysisFilter analysisFilter) {

        final Map<DayOfWeek, Triplet<Integer, Double, List<Trade>>> weekdayMap = generateWeekdayMap(account.getTrades().stream().sorted(Comparator.comparing(Trade::getTradeCloseTime)).toList(), analysisFilter);
        final List<Trade> trades = weekdayMap.getOrDefault(day, Triplet.with(0, 0.0, Collections.emptyList())).getValue2();

        if (CollectionUtils.isEmpty(trades)) {
            return Collections.emptyList();
        }

        final Map<String, Triplet<Integer, Double, List<Trade>>> map = generateTimeBucketMap(trades.stream().sorted(Comparator.comparing(Trade::getTradeCloseTime)).toList(), interval, analysisFilter, false);
        return
                map
                        .entrySet()
                        .stream()
                        .map(e -> new AnalysisResult(e.getKey(), e.getValue().getValue1(), e.getValue().getValue0()))
                        .sorted()
                        .toList();
    }

    /**
     * Computes the trade duration analysis
     *
     * @param account             {@link Account}
     * @param analysisFilter      {@link AnalysisFilter}
     * @param tradeDurationFilter {@link TradeDurationFilter}
     * @return {@link List} of {@link AnalysisResult}
     */
    public List<AnalysisResult> computeTradeDurationAnalysis(final Account account, final AnalysisFilter analysisFilter, final TradeDurationFilter tradeDurationFilter) {

        final Map<Long, Triplet<Integer, Double, List<Trade>>> map = new HashMap<>();
        map.put((Long) 5L, Triplet.with(0, 0.0, Collections.emptyList()));
        map.put((Long) 30L, Triplet.with(0, 0.0, Collections.emptyList()));
        map.put((Long) 60L, Triplet.with(0, 0.0, Collections.emptyList()));
        map.put((Long) 90L, Triplet.with(0, 0.0, Collections.emptyList()));
        map.put((Long) 120L, Triplet.with(0, 0.0, Collections.emptyList()));
        map.put((Long) 150L, Triplet.with(0, 0.0, Collections.emptyList()));
        map.put((Long) 180L, Triplet.with(0, 0.0, Collections.emptyList()));
        map.put((Long) 210L, Triplet.with(0, 0.0, Collections.emptyList()));
        map.put((Long) 99999L, Triplet.with(0, 0.0, Collections.emptyList()));

        final List<Trade> trades = account.getTrades().stream().sorted(Comparator.comparing(Trade::getTradeCloseTime)).toList();
        trades.forEach(trade -> {
            final Long key = getTradeDurationKey(trade);
            if (map.containsKey(key)) {
                map.replace(key, increment(map.get(key), trade, analysisFilter));
            } else {
                map.put(key, increment(Triplet.with(0, 0.0, new ArrayList<>()), trade, analysisFilter));
            }
        });

        final List<AnalysisResult> results = new ArrayList<>();
        map
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .toList()
                .forEach(e -> results.add(new AnalysisResult(e.getKey().toString(), e.getValue().getValue1(), e.getValue().getValue0())));

        return results;
    }


    //  HELPERS

    /**
     * Generates a map of weekday trade buckets
     *
     * @param trades         {@link List} of {@link Trade}s
     * @param analysisFilter {@link AnalysisFilter}
     * @return {@link Map} of buckets
     */
    private Map<DayOfWeek, Triplet<Integer, Double, List<Trade>>> generateWeekdayMap(final List<Trade> trades, final AnalysisFilter analysisFilter) {

        final Map<DayOfWeek, Triplet<Integer, Double, List<Trade>>> map = new EnumMap<>(DayOfWeek.class);
        map.put(DayOfWeek.MONDAY, Triplet.with(0, 0.0, new ArrayList<>()));
        map.put(DayOfWeek.TUESDAY, Triplet.with(0, 0.0, new ArrayList<>()));
        map.put(DayOfWeek.WEDNESDAY, Triplet.with(0, 0.0, new ArrayList<>()));
        map.put(DayOfWeek.THURSDAY, Triplet.with(0, 0.0, new ArrayList<>()));
        map.put(DayOfWeek.FRIDAY, Triplet.with(0, 0.0, new ArrayList<>()));

        trades.forEach(trade -> {
            final DayOfWeek key = trade.getTradeCloseTime().getDayOfWeek();
            if (map.containsKey(key)) {
                map.replace(key, increment(map.get(key), trade, analysisFilter));
            } else {
                map.put(key, increment(Triplet.with(0, 0.0, new ArrayList<>()), trade, analysisFilter));
            }
        });

        return map;
    }

    /**
     * Generates a map of time-bucket trade buckets
     *
     * @param trades         {@link List} of {@link Trade}s
     * @param interval       {@link RadicleTimeInterval}
     * @param analysisFilter {@link AnalysisFilter}
     * @param isOpened       closed or opened trade times
     * @return {@link Map} of buckets
     */
    private Map<String, Triplet<Integer, Double, List<Trade>>> generateTimeBucketMap(final List<Trade> trades, final RadicleTimeInterval interval, final AnalysisFilter analysisFilter, boolean isOpened) {

        final Map<String, Triplet<Integer, Double, List<Trade>>> map = new HashMap<>();
        LocalTime compare = LocalTime.of(9, 30);
        while (compare.isBefore(LocalTime.of(16, 30))) {
            map.put(compare.format(DateTimeFormatter.ofPattern(KEY_PATTERN)), Triplet.with(0, 0.0, new ArrayList<>()));
            compare = compare.plus(interval.getAmount(), interval.getUnit());
        }

        trades.forEach(trade -> {
            final String key = getTimeBucketKey(isOpened ? trade.getTradeOpenTime().toLocalTime() : trade.getTradeCloseTime().toLocalTime(), interval);
            if (map.containsKey(key)) {
                map.replace(key, increment(map.get(key), trade, analysisFilter));
            } else {
                map.put(key, increment(Triplet.with(0, 0.0, new ArrayList<>()), trade, analysisFilter));
            }
        });

        return map;
    }

    /**
     * Computes a time bucket key
     *
     * @param dateTime {@link LocalTime}
     * @param interval {@link RadicleTimeInterval}
     * @return key string
     */
    private String getTimeBucketKey(final LocalTime dateTime, final RadicleTimeInterval interval) {

        if (interval == RadicleTimeInterval.THIRTY_MINUTE) {
            int min = 0;
            if (dateTime.getMinute() > 30) {
                min = 30;
            }

            return dateTime.withMinute(min).format(DateTimeFormatter.ofPattern(KEY_PATTERN));
        } else {
            return dateTime.format(DateTimeFormatter.ofPattern(KEY_PATTERN));
        }
    }

    /**
     * Computes a trade duration key
     *
     * @param trade {@link Trade}
     * @return {@link Long}
     */
    private Long getTradeDurationKey(final Trade trade) {

        final long val = Math.abs(ChronoUnit.MINUTES.between(trade.getTradeCloseTime(), trade.getTradeOpenTime()));
        if (val < 5) {
            return (Long) 5L;
        } else if (val < 30) {
            return (Long) 30L;
        } else if (val < 60) {
            return (Long) 60L;
        } else if (val < 90) {
            return (Long) 90L;
        } else if (val < 120) {
            return (Long) 120L;
        } else if (val < 150) {
            return (Long) 150L;
        } else if (val < 180) {
            return (Long) 180L;
        } else if (val < 210) {
            return (Long) 210L;
        }

        return (Long) 99999L;
    }

    /**
     * Increments the counter for analysis objects with the given {@link Trade}
     *
     * @param triplet        initial {@link Triplet}
     * @param trade          {@link Trade}
     * @param analysisFilter {@link AnalysisFilter}
     * @return updated {@link Triplet}
     */
    private Triplet<Integer, Double, List<Trade>> increment(final Triplet<Integer, Double, List<Trade>> triplet, final Trade trade, final AnalysisFilter analysisFilter) {

        int count = triplet.getValue0();
        double val = triplet.getValue1();
        final List<Trade> trades = new ArrayList<>(triplet.getValue2());
        trades.add(trade);

        switch (analysisFilter) {
            case AnalysisFilter.PROFIT -> {
                return Triplet.with(count + 1, this.mathService.add(val, trade.getNetProfit()), trades);
            }
            case AnalysisFilter.POINTS -> {
                final double np = Math.abs(this.mathService.subtract(trade.getClosePrice(), trade.getOpenPrice()));
                double fp = (trade.getNetProfit() < 0) ? this.mathService.multiply(-1.0, np) : np;
                return Triplet.with(count + 1, this.mathService.add(val, fp), trades);
            }
            default -> {
                return Triplet.with(count + 1, (double) this.mathService.wholePercentage(trades.stream().filter(tr -> tr.getNetProfit() > 0).count(), trades.size()), trades);
            }
        }
    }
}
