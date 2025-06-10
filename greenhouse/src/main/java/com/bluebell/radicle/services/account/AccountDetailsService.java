package com.bluebell.radicle.services.account;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.system.TradeRecordTimeInterval;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.models.core.nonentities.records.account.AccountDetails;
import com.bluebell.platform.models.core.nonentities.records.account.AccountEquityPoint;
import com.bluebell.platform.models.core.nonentities.records.account.AccountInsights;
import com.bluebell.platform.models.core.nonentities.records.account.AccountStatistics;
import com.bluebell.platform.models.core.nonentities.records.trade.CumulativeTrade;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecord;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecordReport;
import com.bluebell.platform.services.MathService;
import com.bluebell.radicle.services.trade.TradeRecordService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;


/**
 * Service-layer implementation of {@link AccountDetails}
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Service("accountDetailsService")
public class AccountDetailsService {

    private final MathService mathService = new MathService();

    @Resource(name = "tradeRecordService")
    private TradeRecordService tradeRecordService;


    //  METHODS

    /**
     * Calculates the account's consistency score
     *
     * @param account {@link Account}
     * @return consistency score 0 - 100
     */
    public int calculateConsistencyScore(final Account account) {

        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        final TradeRecordReport report =
                this.tradeRecordService.getTradeRecords(
                        account.getAccountOpenTime().toLocalDate().minusDays(1),
                        account.getLastTraded() == null ? LocalDate.now().plusDays(1) : account.getLastTraded().toLocalDate().plusDays(1),
                        account,
                        TradeRecordTimeInterval.DAILY,
                        CorePlatformConstants.MAX_RESULT_SIZE
                );

        if (report == null || CollectionUtils.isEmpty(report.tradeRecords())) {
            return 0;
        }

        final List<TradeRecord> tradeRecords = report.tradeRecords();
        final List<Double> sorted = tradeRecords.stream().map(tr -> Math.abs(tr.getNetProfit())).sorted(Comparator.comparing(Double::doubleValue)).toList();
        final double max = sorted.get(sorted.size() - 1);
        final double quotient = this.mathService.divide(max, sorted.stream().mapToDouble(Double::doubleValue).sum());

        return this.mathService.getInteger(this.mathService.multiply((this.mathService.subtract(1.0, quotient)), 100.0));
    }

    /**
     * Calculates the account's equity per trade over time
     *
     * @param account {@link Account}
     * @return {@link List} of {@link AccountEquityPoint}
     */
    public List<AccountEquityPoint> calculateEquityPoints(final Account account) {

        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        if (CollectionUtils.isEmpty(account.getTrades())) {
            return List.of(AccountEquityPoint.builder().date(account.getAccountOpenTime()).amount(account.getBalance()).points(0.0).cumAmount(account.getBalance()).cumPoints(0.0).build());
        }

        final List<AccountEquityPoint> equityPoints = new ArrayList<>();
        final List<Trade> trades = account.getTrades().stream().filter(Trade::isClosed).sorted(Comparator.comparing(Trade::getTradeCloseTime).thenComparing(Trade::getTradeOpenTime)).toList();
        final double starterBalance = this.mathService.subtract(account.getBalance(), account.getTrades().stream().mapToDouble(Trade::getNetProfit).sum());

        for (int i = 0; i < trades.size(); i++) {
            final Trade trade = trades.get(i);
            final double points = (trade.getNetProfit() < 0) ? this.mathService.multiply(-1.0, Math.abs(this.mathService.subtract(trade.getClosePrice(), trade.getOpenPrice()))) : Math.abs(this.mathService.subtract(trade.getClosePrice(), trade.getOpenPrice()));

            if (i == 0) {
                equityPoints.add(AccountEquityPoint.builder().date(trade.getTradeCloseTime()).amount(trade.getNetProfit()).points(points).cumAmount(this.mathService.add(starterBalance, trade.getNetProfit())).cumPoints(points).build());
            } else {
                final double cumAmount = this.mathService.add(equityPoints.get(i - 1).cumAmount(), trade.getNetProfit());
                final double cumPoints = this.mathService.add(equityPoints.get(i - 1).cumPoints(), points);

                equityPoints.add(AccountEquityPoint.builder().date(trade.getTradeCloseTime()).amount(trade.getNetProfit()).points(points).cumAmount(cumAmount).cumPoints(cumPoints).build());
            }
        }

        equityPoints.add(0, AccountEquityPoint.builder().date(trades.get(0).getTradeCloseTime().minusDays(1)).amount(0.0).points(0.0).cumAmount(starterBalance).cumPoints(0.0).build());
        return equityPoints;
    }

    /**
     * Calculates the account's insights
     *
     * @param account {@link Account}
     * @return {@link AccountInsights}
     */
    public AccountInsights obtainInsights(final Account account) {

        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        final List<CumulativeTrade> cumulativeTrades = generativeCumulativeTrades(account);
        final double initialBalance = account.getInitialBalance();
        final double currentPL = cumulativeTrades.get(cumulativeTrades.size() - 1).netProfit();
        final double biggestLoss = cumulativeTrades.stream().mapToDouble(CumulativeTrade::singleProfit).min().orElse(0.0);
        final double largestGain = cumulativeTrades.stream().mapToDouble(CumulativeTrade::singleProfit).max().orElse(0.0);
        final double drawdown = calculateDrawdown(cumulativeTrades);
        final double maxProfit = cumulativeTrades.stream().mapToDouble(CumulativeTrade::netProfit).max().orElse(0.0);

        final Map<LocalDate, Trade> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(account.getTrades())) {
            account.getTrades().forEach(tr -> map.put(tr.getTradeOpenTime().toLocalDate(), tr));
        }

        return AccountInsights
                .builder()
                .tradingDays(map.size())
                .currentPL(currentPL)
                .biggestLoss(biggestLoss)
                .largestGain(largestGain)
                .drawdown(drawdown)
                .maxProfit(maxProfit)
                .currentPLDelta(BigDecimal.valueOf(currentPL).divide(BigDecimal.valueOf(initialBalance), 25, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100.0)).setScale(2, RoundingMode.HALF_EVEN).abs().doubleValue())
                .biggestLossDelta(BigDecimal.valueOf(biggestLoss).divide(BigDecimal.valueOf(initialBalance), 25, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100.0)).setScale(2, RoundingMode.HALF_EVEN).abs().doubleValue())
                .largestGainDelta(BigDecimal.valueOf(largestGain).divide(BigDecimal.valueOf(initialBalance), 25, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100.0)).setScale(2, RoundingMode.HALF_EVEN).abs().doubleValue())
                .drawdownDelta(BigDecimal.valueOf(drawdown).divide(BigDecimal.valueOf(initialBalance), 25, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100.0)).setScale(2, RoundingMode.HALF_EVEN).abs().doubleValue())
                .maxProfitDelta(BigDecimal.valueOf(maxProfit).divide(BigDecimal.valueOf(initialBalance), 25, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100.0)).setScale(2, RoundingMode.HALF_EVEN).abs().doubleValue())
                .build();
    }

    /**
     * Calculates the account's statistics
     *
     * @param account {@link Account}
     * @return {@link AccountStatistics}
     */
    public AccountStatistics obtainStatistics(final Account account) {

        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        final List<Trade> trades = CollectionUtils.isEmpty(account.getTrades()) ? Collections.emptyList() : account.getTrades().stream().sorted(Comparator.comparing(Trade::getTradeCloseTime)).toList();
        final double positiveProfitCount = this.mathService.getDouble(trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d > 0).count());
        final double positiveProfit = this.mathService.getDouble(trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d > 0).sum());
        final double negativeProfit = this.mathService.getDouble(trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d < 0).sum());
        final double drawdown = calculateDrawdown(generativeCumulativeTrades(account));
        final double averageLoss = trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d < 0).average().orElse(0.0);

        return AccountStatistics
                .builder()
                .balance(account.getBalance())
                .averageProfit(trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d > 0).average().orElse(0.0))
                .averageLoss(averageLoss)
                .numberOfTrades(trades.size())
                .rrr(calculateRiskToRewardRatio(account))
                .lots(this.mathService.getDouble(trades.stream().mapToDouble(Trade::getLotSize).sum()))
                .expectancy(this.mathService.getDouble(trades.stream().mapToDouble(Trade::getNetProfit).average().orElse(0.0)))
                .winPercentage(this.mathService.wholePercentage(positiveProfitCount, this.mathService.getDouble(trades.size())))
                .profitFactor(this.mathService.divide(positiveProfit, Math.abs(negativeProfit)))
                .retention(this.mathService.wholePercentage(positiveProfit, this.mathService.add(positiveProfit, Math.abs(negativeProfit))))
                .sharpeRatio(calculateSharpeRatio(account))
                .tradeDuration(Math.round(trades.stream().mapToLong(tr -> Math.abs(ChronoUnit.SECONDS.between(tr.getTradeOpenTime(), tr.getTradeCloseTime()))).average().orElse(0.0)))
                .winDuration(Math.round(trades.stream().filter(tr -> tr.getNetProfit() > 0).mapToLong(tr -> Math.abs(ChronoUnit.SECONDS.between(tr.getTradeOpenTime(), tr.getTradeCloseTime()))).average().orElse(0.0)))
                .lossDuration(Math.round(trades.stream().filter(tr -> tr.getNetProfit() < 0).mapToLong(tr -> Math.abs(ChronoUnit.SECONDS.between(tr.getTradeOpenTime(), tr.getTradeCloseTime()))).average().orElse(0.0)))
                .assumedDrawdown(this.mathService.multiply(this.mathService.add(Math.abs(drawdown), Math.abs(averageLoss)), -1.0))
                .build();
    }


    //  HELPERS

    /**
     * Computes the drawdown between cumulative trades
     *
     * @param cumulativeTrades {@link List} of {@link CumulativeTrade}
     * @return drawdown price
     */
    private double calculateDrawdown(final List<CumulativeTrade> cumulativeTrades) {

        if (CollectionUtils.isEmpty(cumulativeTrades)) {
            return 0.0;
        }

        double drawdown = 0.0;
        double swingLow = 0.0;
        double swingHigh = 0.0;

        int swingHighIndex = 0;
        int swingLowIndex;

        for (int i = 0; i < cumulativeTrades.size(); i++) {
            final CumulativeTrade cumulativeTrade = cumulativeTrades.get(i);
            if (cumulativeTrade.netProfit() < swingLow) {
                swingLow = cumulativeTrade.netProfit();
                swingLowIndex = i;

                final double localDrawdown = Math.abs(this.mathService.subtract(cumulativeTrades.get(swingHighIndex).netProfit(), cumulativeTrades.get(swingLowIndex).netProfit()));
                if (localDrawdown > drawdown) {
                    drawdown = localDrawdown;
                }
            } else if (cumulativeTrade.netProfit() > swingHigh) {
                swingHigh = cumulativeTrade.netProfit();
                swingHighIndex = i;
            }
        }

        return this.mathService.multiply(drawdown, -1.0);
    }

    /**
     * Generates a {@link List} of {@link CumulativeTrade}s
     *
     * @param account {@link Account}
     * @return {@link List} of {@link CumulativeTrade}
     */
    private List<CumulativeTrade> generativeCumulativeTrades(final Account account) {

        final List<Trade> trades = CollectionUtils.isEmpty(account.getTrades()) ? Collections.emptyList() : account.getTrades().stream().filter(Trade::isClosed).sorted(Comparator.comparing(Trade::getTradeCloseTime)).toList();
        final List<CumulativeTrade> cumulativeTrades = new ArrayList<>();

        int count = 0;
        double cumProfit = 0.0;
        double cumPoints = 0.0;

        for (final Trade trade : trades) {
            count += 1;
            cumProfit += trade.getNetProfit();
            final double np = Math.abs(this.mathService.subtract(trade.getClosePrice(), trade.getOpenPrice()));
            cumPoints += (trade.getNetProfit() < 0) ? this.mathService.multiply(-1.0, np) : np;

            cumulativeTrades.add(
                    CumulativeTrade
                            .builder()
                            .tradeCloseTime(trade.getTradeCloseTime())
                            .count(count)
                            .singleProfit(trade.getNetProfit())
                            .singlePoints((trade.getNetProfit() < 0) ? this.mathService.multiply(-1.0, np) : np)
                            .netProfit(cumProfit)
                            .netPoints(cumPoints)
                            .build()
            );
        }

        cumulativeTrades.add(0, CumulativeTrade.builder().tradeCloseTime(account.getAccountOpenTime()).count(0).singleProfit(0.0).singlePoints(0.0).netProfit(0.0).netPoints(0.0).build());
        return cumulativeTrades;
    }

    /**
     * Calculates the risk to reward ratio for this account
     *
     * @param account {@link Account}
     * @return RRR
     */
    private double calculateRiskToRewardRatio(final Account account) {

        final List<Trade> trades = CollectionUtils.isEmpty(account.getTrades()) ? Collections.emptyList() :
                account.getTrades()
                        .stream()
                        .sorted(Comparator.comparing(Trade::getTradeCloseTime))
                        .filter(tr -> tr.getStopLoss() > 0.0)
                        .filter(tr -> tr.getTakeProfit() > 0.0)
                        .toList();

        final double averageTP =
                this.mathService.getDouble(
                        trades
                                .stream()
                                .mapToDouble(tr -> Math.abs(this.mathService.subtract(tr.getTakeProfit(), tr.getOpenPrice())))
                                .average()
                                .orElse(0.0)
                );

        final double averageSL =
                this.mathService.getDouble(
                        trades
                                .stream()
                                .mapToDouble(tr -> Math.abs(this.mathService.subtract(tr.getStopLoss(), tr.getOpenPrice())))
                                .average()
                                .orElse(0.0)
                );

        return this.mathService.divide(averageTP, averageSL);
    }

    /**
     * Calculates the sharpe ratio based on the monthly returns of the account
     *
     * @param account {@link Account}
     * @return sharpe ratio
     */
    private double calculateSharpeRatio(final Account account) {

        final TradeRecordReport report = this.tradeRecordService.getTradeRecords(account.getAccountOpenTime().minusYears(1).toLocalDate(), LocalDate.now().plusYears(1), account, TradeRecordTimeInterval.MONTHLY, -1);
        if (report == null) {
            return 0.0;
        }

        final List<TradeRecord> monthlyRecords = report.tradeRecords();
        final double averageMonthlyReturn = monthlyRecords.stream().mapToInt(tr -> this.mathService.wholePercentage(tr.netProfit(), account.getBalance())).average().orElse(0.0);
        final double std = new StandardDeviation().evaluate(monthlyRecords.stream().mapToDouble(TradeRecord::netProfit).toArray());

        if (Double.isNaN(std)) {
            return 0.0;
        } else {
            return this.mathService.divide(this.mathService.subtract(averageMonthlyReturn, CorePlatformConstants.RISK_FREE_RATE_CANADA), std);
        }
    }
}
