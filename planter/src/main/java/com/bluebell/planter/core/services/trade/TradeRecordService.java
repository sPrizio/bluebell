package com.bluebell.planter.core.services.trade;

import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.system.FlowerpotTimeInterval;
import com.bluebell.planter.core.exceptions.trade.TradeRecordComputationException;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.entities.security.User;
import com.bluebell.planter.core.models.entities.trade.Trade;
import com.bluebell.planter.core.models.nonentities.records.trade.*;
import com.bluebell.planter.core.models.nonentities.records.tradeRecord.TradeRecord;
import com.bluebell.planter.core.models.nonentities.records.tradeRecord.TradeRecordEquityPoint;
import com.bluebell.planter.core.models.nonentities.records.tradeRecord.TradeRecordReport;
import com.bluebell.planter.core.models.nonentities.records.tradeRecord.TradeRecordTotals;
import com.bluebell.planter.core.models.nonentities.records.tradeRecord.controls.TradeRecordControls;
import com.bluebell.planter.core.models.nonentities.records.tradeRecord.controls.TradeRecordControlsMonthEntry;
import com.bluebell.planter.core.models.nonentities.records.tradeRecord.controls.TradeRecordControlsYearEntry;
import com.bluebell.radicle.services.MathService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static com.bluebell.planter.core.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.bluebell.planter.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for calculating {@link TradeRecord}s
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@Service
public class TradeRecordService {

    private final MathService mathService = new MathService();

    @Resource(name = "tradeService")
    private TradeService tradeService;


    //  METHODS

    /**
     * Obtains a {@link List} of {@link TradeRecord}s
     *
     * @param start                 start of time period
     * @param end                   end of time period
     * @param account               {@link Account}
     * @param flowerpotTimeInterval {@link FlowerpotTimeInterval}
     * @param count                 number of results
     * @return {@link List} of {@link TradeRecord}
     */
    public TradeRecordReport getTradeRecords(final LocalDate start, final LocalDate end, final Account account, final FlowerpotTimeInterval flowerpotTimeInterval, final int count) {

        validateParameterIsNotNull(start, CoreConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start.atStartOfDay(), end.atStartOfDay(), CoreConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        validateParameterIsNotNull(account, CoreConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
        validateParameterIsNotNull(flowerpotTimeInterval, CoreConstants.Validation.System.TIME_INTERVAL_CANNOT_BE_NULL);

        LocalDate tempStart = flowerpotTimeInterval == FlowerpotTimeInterval.MONTHLY ? start.with(TemporalAdjusters.firstDayOfMonth()) : start;
        LocalDate tempEnd = tempStart.plus(flowerpotTimeInterval.amount, flowerpotTimeInterval.unit);

        final List<TradeRecord> records = new ArrayList<>();
        while (tempStart.isBefore(end) || tempStart.isEqual(end)) {
            records.add(generateRecord(tempStart, tempEnd, this.tradeService.findAllTradesWithinTimespan(tempStart.atStartOfDay(), tempEnd.atStartOfDay(), account)));

            tempStart = tempStart.plus(flowerpotTimeInterval.amount, flowerpotTimeInterval.unit);
            tempEnd = tempEnd.plus(flowerpotTimeInterval.amount, flowerpotTimeInterval.unit);
        }

        final List<TradeRecord> tradeRecords;
        if (count == CoreConstants.MAX_RESULT_SIZE) {
            tradeRecords = records.stream().filter(tr -> tr.trades() > 0).sorted(Comparator.reverseOrder()).toList();
        } else {
            tradeRecords = records.stream().filter(tr -> tr.trades() > 0).sorted(Comparator.reverseOrder()).limit(count).toList();
        }

        return new TradeRecordReport(tradeRecords, computeTotals(tradeRecords));
    }

    /**
     * Obtains a {@link List} of the most recent {@link TradeRecord}s for the given {@link Account}
     *
     * @param account {@link Account}
     * @param flowerpotTimeInterval {@link FlowerpotTimeInterval}
     * @param count limit
     * @return {@link List} of {@link TradeRecord}
     */
    public TradeRecordReport getRecentTradeRecords(final Account account, final FlowerpotTimeInterval flowerpotTimeInterval, final int count) {

        final LocalDateTime lastTraded = account.getLastTraded();
        if (lastTraded == null) {
            return new TradeRecordReport(Collections.emptyList(), null);
        }

        final LocalDateTime firstTraded = account.getTrades().getFirst().getTradeCloseTime();
        if (firstTraded == null) {
            throw new TradeRecordComputationException("This account doesn't have any closed trades. This must be revised!");
        }

        final List<TradeRecord> tradeRecords = new ArrayList<>();
        LocalDateTime compare = lastTraded;
        while (tradeRecords.size() < count && (compare.isAfter(firstTraded) || compare.isEqual(firstTraded))) {
            tradeRecords.addAll(getTradeRecords(compare.minusMonths(1).toLocalDate(), compare.toLocalDate(), account, flowerpotTimeInterval, -1).tradeRecords());
            compare = compare.minusMonths(1);
        }

        final List<TradeRecord> finalList = tradeRecords.stream().sorted(Comparator.reverseOrder()).limit(count).toList();
        return new TradeRecordReport(finalList, computeTotals(finalList));
    }

    /**
     * Obtains a {@link TradeLog}
     *
     * @param user                  {@link User}
     * @param start                 start of time period
     * @param end                   end of time period
     * @param flowerpotTimeInterval {@link FlowerpotTimeInterval}
     * @param count                 number of results
     * @return {@link TradeLog}
     */
    public List<TradeLog> getTradeLog(final User user, final LocalDate start, final LocalDate end, final FlowerpotTimeInterval flowerpotTimeInterval, final int count) {

        final Map<LocalDate, List<TradeRecord>> map = new HashMap<>();
        final List<TradeRecord> tradeRecords = user.getAccounts().stream().map(acc -> getTradeRecords(start, end, acc, flowerpotTimeInterval, count).tradeRecords()).toList().stream().flatMap(List::stream).toList();

        for (final TradeRecord trec : tradeRecords) {
            List<TradeRecord> temp;
            if (map.containsKey(trec.start())) {
                temp = new ArrayList<>(map.get(trec.start()));
            } else {
                temp = new ArrayList<>();
            }

            temp.add(trec);
            map.put(trec.start(), temp);
        }

        return map.values().stream().map(records -> new TradeLog(start, end, records)).toList();
    }

    /**
     * Generates a {@link TradeRecordControls} for an {@link Account}s trades
     *
     * @param account {@link Account}
     * @param flowerpotTimeInterval {@link FlowerpotTimeInterval}
     * @return {@link TradeRecordControls}
     */
    public TradeRecordControls getTradeRecordControls(final Account account, final FlowerpotTimeInterval flowerpotTimeInterval) {

        final List<Trade> trades = account.getTrades();
        final Map<String, Map<String, Integer>> map = new HashMap<>();

        for (final Trade trade : trades) {
            final String yearKey = String.valueOf(trade.getTradeCloseTime().getYear());
            final Map<String, Integer> monthMap;

            if (map.containsKey(yearKey)) {
                monthMap = map.get(yearKey);
            } else {
                monthMap = generateMonthMap();
            }

            final String monthKey = trade.getTradeCloseTime().getMonth().toString();
            final int count;
            count = monthMap.getOrDefault(monthKey, 0);

            monthMap.put(monthKey, count + 1);
            map.put(yearKey, monthMap);
        }

        return new TradeRecordControls(map.entrySet().stream().map(entry -> new TradeRecordControlsYearEntry(entry.getKey(), entry.getValue().entrySet().stream().map(e -> new TradeRecordControlsMonthEntry(Month.valueOf(e.getKey().toUpperCase()).getValue(), e.getKey(), e.getValue())).sorted(Comparator.comparing(TradeRecordControlsMonthEntry::monthNumber)).toList())).toList());
    }


    //  HELPERS

    /**
     * Initializes a map of months
     *
     * @return {@link Map}
     */
    private Map<String, Integer> generateMonthMap() {

        final Map<String, Integer> monthMap = new HashMap<>();

        monthMap.put("JANUARY", 0);
        monthMap.put("FEBRUARY", 0);
        monthMap.put("MARCH", 0);
        monthMap.put("APRIL", 0);
        monthMap.put("MAY", 0);
        monthMap.put("JUNE", 0);
        monthMap.put("JULY", 0);
        monthMap.put("AUGUST", 0);
        monthMap.put("SEPTEMBER", 0);
        monthMap.put("OCTOBER", 0);
        monthMap.put("NOVEMBER", 0);
        monthMap.put("DECEMBER", 0);

        return monthMap;
    }

    /**
     * Generates a {@link TradeRecord} from the given {@link List} of {@link Trade}s for the given start and end dates
     *
     * @param start  {@link LocalDate}
     * @param end    {@link LocalDate}
     * @param trades {@link List} of {@link Trade}
     * @return {@link TradeRecord}
     */
    private TradeRecord generateRecord(final LocalDate start, final LocalDate end, final List<Trade> trades) {

        if (CollectionUtils.isEmpty(trades)) {
            return new TradeRecord(start, end, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, Collections.emptyList());
        }

        final List<Trade> tradesWon = trades.stream().filter(t -> t.getNetProfit() > 0.0).sorted(Comparator.comparing(Trade::getNetProfit).reversed()).toList();
        final List<Trade> tradesLost = trades.stream().filter(t -> t.getNetProfit() < 0.0).sorted(Comparator.comparing(Trade::getNetProfit)).toList();

        final double netProfit = this.mathService.getDouble(trades.stream().mapToDouble(Trade::getNetProfit).sum());
        final double pointsGained = this.mathService.getDouble(tradesWon.stream().mapToDouble(this::getPoints).sum());
        final double pointsLost = this.mathService.getDouble(tradesLost.stream().mapToDouble(this::getPoints).sum());
        final double largestWin = tradesWon.isEmpty() ? 0.0 : this.mathService.getDouble(tradesWon.getFirst().getNetProfit());
        final double winAverage = this.mathService.getDouble(tradesWon.stream().mapToDouble(Trade::getNetProfit).average().orElse(0.0));
        final double largestLoss = tradesLost.isEmpty() ? 0.0 : this.mathService.getDouble(tradesLost.getFirst().getNetProfit());
        final double lossAverage = this.mathService.getDouble(tradesLost.stream().mapToDouble(Trade::getNetProfit).average().orElse(0.0));
        final int wins = tradesWon.size();
        final int losses = tradesLost.size();

        return new TradeRecord(
                start,
                end,
                netProfit,
                calculateLowestPoint(trades),
                pointsGained,
                pointsLost,
                this.mathService.subtract(pointsGained, pointsLost),
                largestWin,
                winAverage,
                largestLoss,
                lossAverage,
                this.mathService.wholePercentage(tradesWon.size(), trades.size()),
                wins,
                losses,
                trades.size(),
                this.mathService.divide(pointsGained, pointsLost),
                this.mathService.wholePercentage(pointsGained, this.mathService.add(pointsGained, Math.abs(pointsLost))),
                computeTradeRecordEquityPoints(trades)
        );
    }

    /**
     * Calculates the point difference for the given {@link Trade}
     *
     * @param trade {@link Trade}
     * @return points
     */
    private double getPoints(final Trade trade) {
        return Math.abs(this.mathService.subtract(trade.getOpenPrice(), trade.getClosePrice()));
    }

    /**
     * Calculates the absolute minimum (draw-down) of the day
     *
     * @param trades {@link List} of {@link Trade}s
     * @return lowest point
     */
    private double calculateLowestPoint(final List<Trade> trades) {

        if (CollectionUtils.isEmpty(trades)) {
            return 0.0;
        }

        double sum = 0.0;
        double lowest = trades.getFirst().getNetProfit();

        for (Trade trade : trades) {
            sum = this.mathService.add(sum, trade.getNetProfit());
            if (sum < lowest) {
                lowest = sum;
            }
        }

        return lowest;
    }

    /**
     * Computes a {@link TradeRecordTotals} from the given {@link TradeRecord}s
     *
     * @param tradeRecords {@link List} of {@link TradeRecord}s
     * @return {@link TradeRecordTotals}
     */
    private TradeRecordTotals computeTotals(final List<TradeRecord> tradeRecords) {

        final int wins = tradeRecords.stream().mapToInt(TradeRecord::wins).sum();
        final int losses = tradeRecords.stream().mapToInt(TradeRecord::losses).sum();
        final double netProfit = this.mathService.getDouble(tradeRecords.stream().mapToDouble(TradeRecord::netProfit).sum());
        final double netPoints = this.mathService.getDouble(tradeRecords.stream().mapToDouble(TradeRecord::points).sum());


        return new TradeRecordTotals(tradeRecords.size(), (wins + losses), this.mathService.wholePercentage(wins, (wins + losses)), netProfit, netPoints);
    }

    /**
     * Computes a {@link List} of {@link TradeRecordEquityPoint}s
     *
     * @param trades {@link List} of {@link Trade}s
     * @return {@link List} of {@link TradeRecordEquityPoint}s
     */
    private List<TradeRecordEquityPoint> computeTradeRecordEquityPoints(final List<Trade> trades) {

        if (CollectionUtils.isEmpty(trades)) {
            return Collections.emptyList();
        }

        final List<TradeRecordEquityPoint> equityPoints = new ArrayList<>();
        for (int i = 0; i < trades.size(); i++) {
            final Trade trade = trades.get(i);
            final double points = (trade.getNetProfit() < 0) ? this.mathService.multiply(-1.0, Math.abs(this.mathService.subtract(trade.getClosePrice(), trade.getOpenPrice()))) : Math.abs(this.mathService.subtract(trade.getClosePrice(), trade.getOpenPrice()));

            if (i == 0) {
                equityPoints.add(new TradeRecordEquityPoint(1, trade.getNetProfit(), points, this.mathService.add(0.0, trade.getNetProfit()), points));
            } else {
                final double cumAmount = this.mathService.add(equityPoints.get(i - 1).cumAmount(), trade.getNetProfit());
                final double cumPoints = this.mathService.add(equityPoints.get(i - 1).cumPoints(), points);

                equityPoints.add(new TradeRecordEquityPoint(i + 1, trade.getNetProfit(), points, cumAmount, cumPoints));
            }
        }

        equityPoints.addFirst(new TradeRecordEquityPoint(0, 0.0, 0.0, 0.0, 0.0));
        return equityPoints;
    }
}
