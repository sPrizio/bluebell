package com.bluebell.planter.core.services.trade;

import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.system.FlowerpotTimeInterval;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.entities.trade.Trade;
import com.bluebell.planter.core.models.nonentities.TradeRecord;
import com.bluebell.radicle.services.MathService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.bluebell.planter.core.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.bluebell.planter.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for calculating {@link TradeRecord}s
 *
 * @author Stephen Prizio
 * @version 0.0.6
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
     * @param start start of time period
     * @param end end of time period
     * @param account {@link Account}
     * @param count number of results
     * @return {@link List} of {@link TradeRecord}
     */
    public List<TradeRecord> getTradeRecords(final LocalDate start, final LocalDate end, final Account account, final FlowerpotTimeInterval flowerpotTimeInterval, final int count) {

        validateParameterIsNotNull(start, CoreConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start.atStartOfDay(), end.atStartOfDay(), CoreConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        validateParameterIsNotNull(account, CoreConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
        validateParameterIsNotNull(flowerpotTimeInterval, CoreConstants.Validation.System.TIME_INTERVAL_CANNOT_BE_NULL);

        final int limit = (count == CoreConstants.MAX_RESULT_SIZE) ? 1000000 : count;
        LocalDate tempStart = flowerpotTimeInterval == FlowerpotTimeInterval.MONTHLY ? start.with(TemporalAdjusters.firstDayOfMonth()) : start;
        LocalDate tempEnd = tempStart.plus(flowerpotTimeInterval.amount, flowerpotTimeInterval.unit);

        final List<TradeRecord> records = new ArrayList<>();
        while (tempStart.isBefore(end) || tempStart.isEqual(end)) {
            records.add(generateRecord(tempStart, tempEnd, this.tradeService.findAllTradesWithinTimespan(tempStart.atStartOfDay(), tempEnd.atStartOfDay(), account)));

            tempStart = tempStart.plus(flowerpotTimeInterval.amount, flowerpotTimeInterval.unit);
            tempEnd = tempEnd.plus(flowerpotTimeInterval.amount, flowerpotTimeInterval.unit);
        }

        return records.stream().filter(tr -> tr.trades() > 0).sorted(Comparator.reverseOrder()).limit(limit).toList();
    }


    //  HELPERS

    /**
     * Generates a {@link TradeRecord} from the given {@link List} of {@link Trade}s for the given start and end dates
     *
     * @param start {@link LocalDate}
     * @param end {@link LocalDate}
     * @param trades {@link List} of {@link Trade}
     * @return {@link TradeRecord}
     */
    private TradeRecord generateRecord(final LocalDate start, final LocalDate end, final List<Trade> trades) {

        if (CollectionUtils.isEmpty(trades)) {
            return new TradeRecord(start, end, 0,0,0,0,0,0,0,0, 0, 0, 0,0,0,0,0);
        }

        final List<Trade> tradesWon = trades.stream().filter(t -> t.getNetProfit() > 0.0).sorted(Comparator.comparing(Trade::getNetProfit).reversed()).toList();
        final List<Trade> tradesLost = trades.stream().filter(t -> t.getNetProfit() < 0.0).sorted(Comparator.comparing(Trade::getNetProfit)).toList();

        final double netProfit = this.mathService.getDouble(trades.stream().mapToDouble(Trade::getNetProfit).sum());
        final double pointsGained = this.mathService.getDouble(tradesWon.stream().mapToDouble(this::getPoints).sum());
        final double pointsLost = this.mathService.getDouble(tradesLost.stream().mapToDouble(this::getPoints).sum());
        final double largestWin = tradesWon.isEmpty() ? 0.0 : this.mathService.getDouble(tradesWon.getFirst().getNetProfit());;
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
                this.mathService.wholePercentage(pointsGained, this.mathService.add(pointsGained, Math.abs(pointsLost)))
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
}
