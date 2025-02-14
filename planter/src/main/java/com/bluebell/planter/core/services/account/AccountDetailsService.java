package com.bluebell.planter.core.services.account;

import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.system.FlowerpotTimeInterval;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.entities.trade.Trade;
import com.bluebell.planter.core.models.nonentities.records.account.AccountDetails;
import com.bluebell.planter.core.models.nonentities.records.account.AccountEquityPoint;
import com.bluebell.planter.core.models.nonentities.records.account.AccountInsights;
import com.bluebell.planter.core.models.nonentities.records.account.AccountStatistics;
import com.bluebell.planter.core.models.nonentities.records.trade.CumulativeTrade;
import com.bluebell.planter.core.models.nonentities.records.traderecord.TradeRecord;
import com.bluebell.planter.core.services.trade.TradeRecordService;
import com.bluebell.radicle.services.MathService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.javatuples.Triplet;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.bluebell.planter.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer implementation of {@link AccountDetails}
 *
 * @author Stephen Prizio
 * @version 0.0.9
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
        //  TODO: implement this
        return 49;
    }

    /**
     * Calculates the account's equity per trade over time
     *
     * @param account {@link Account}
     * @return {@link List} of {@link AccountEquityPoint}
     */
    public List<AccountEquityPoint> calculateEquityPoints(final Account account) {

        validateParameterIsNotNull(account, CoreConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        if (CollectionUtils.isEmpty(account.getTrades())) {
            return List.of(new AccountEquityPoint(account.getAccountOpenTime(), account.getBalance(), 0.0, account.getBalance(), 0.0));
        }

        final List<AccountEquityPoint> equityPoints = new ArrayList<>();

        final List<Trade> trades = account.getTrades().stream().sorted(Comparator.comparing(Trade::getTradeCloseTime).thenComparing(Trade::getTradeOpenTime)).toList();
        final double starterBalance = this.mathService.subtract(account.getBalance(), account.getTrades().stream().mapToDouble(Trade::getNetProfit).sum());

        for (int i = 0; i < trades.size(); i++) {
            final Trade trade = trades.get(i);
            final double points = (trade.getNetProfit() < 0) ? this.mathService.multiply(-1.0, Math.abs(this.mathService.subtract(trade.getClosePrice(), trade.getOpenPrice()))) : Math.abs(this.mathService.subtract(trade.getClosePrice(), trade.getOpenPrice()));

            if (i == 0) {
                equityPoints.add(new AccountEquityPoint(trade.getTradeCloseTime(), trade.getNetProfit(), points, this.mathService.add(starterBalance, trade.getNetProfit()), points));
            } else {
                final double cumAmount = this.mathService.add(equityPoints.get(i - 1).cumAmount(), trade.getNetProfit());
                final double cumPoints = this.mathService.add(equityPoints.get(i - 1).cumPoints(), points);

                equityPoints.add(new AccountEquityPoint(trade.getTradeCloseTime(), trade.getNetProfit(), points, cumAmount, cumPoints));
            }
        }

        equityPoints.add(0, new AccountEquityPoint(trades.get(0).getTradeCloseTime().minusDays(1), 0.0, 0.0, starterBalance, 0.0));
        return equityPoints;
    }

    /**
     * Calculates the account's insights
     *
     * @param account {@link Account}
     * @return {@link AccountInsights}
     */
    public AccountInsights obtainInsights(final Account account) {

        final List<TradeRecord> tradeRecords = this.tradeRecordService.getTradeRecords(account.getAccountOpenTime().minusYears(1).toLocalDate(), LocalDate.now().plusYears(1), account, FlowerpotTimeInterval.DAILY, -1).tradeRecords();
        if (CollectionUtils.isEmpty(tradeRecords)) {
            return new AccountInsights(0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0);
        }

        final List<CumulativeTrade> cumulativeTrades = generativeCumulativeTrades(account);
        if (CollectionUtils.isEmpty(cumulativeTrades)) {
            return new AccountInsights(0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0);
        }

        final double initialBalance = account.getInitialBalance();
        final double currentPL = cumulativeTrades.get(cumulativeTrades.size() - 1).netProfit();
        final double biggestLoss = cumulativeTrades.stream().mapToDouble(CumulativeTrade::singleProfit).min().orElse(0.0);
        final double largestGain = cumulativeTrades.stream().mapToDouble(CumulativeTrade::singleProfit).max().orElse(0.0);
        final double drawdown = calculateDrawdown(cumulativeTrades);
        final double maxProfit = cumulativeTrades.stream().mapToDouble(CumulativeTrade::netProfit).max().orElse(0.0);

        return new AccountInsights(
                tradeRecords.size(),
                currentPL,
                biggestLoss,
                largestGain,
                drawdown,
                maxProfit,
                BigDecimal.valueOf(currentPL).divide(BigDecimal.valueOf(initialBalance), 25, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100.0)).setScale(2, RoundingMode.HALF_EVEN).abs().doubleValue(),
                BigDecimal.valueOf(biggestLoss).divide(BigDecimal.valueOf(initialBalance), 25, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100.0)).setScale(2, RoundingMode.HALF_EVEN).abs().doubleValue(),
                BigDecimal.valueOf(largestGain).divide(BigDecimal.valueOf(initialBalance), 25, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100.0)).setScale(2, RoundingMode.HALF_EVEN).abs().doubleValue(),
                BigDecimal.valueOf(drawdown).divide(BigDecimal.valueOf(initialBalance), 25, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100.0)).setScale(2, RoundingMode.HALF_EVEN).abs().doubleValue(),
                BigDecimal.valueOf(maxProfit).divide(BigDecimal.valueOf(initialBalance), RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100.0)).doubleValue()
        );
    }

    /**
     * Calculates the account's statistics
     *
     * @param account {@link Account}
     * @return {@link AccountStatistics}
     */
    public AccountStatistics obtainStatistics(final Account account) {

        final List<Trade> trades = account.getTrades().stream().sorted(Comparator.comparing(Trade::getTradeCloseTime)).toList();
        final double positiveProfitCount = this.mathService.getDouble(trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d > 0).count());
        final double positiveProfit = this.mathService.getDouble(trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d > 0).sum());
        final double negativeProfit = this.mathService.getDouble(trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d < 0).sum());
        final double drawdown = calculateDrawdown(generativeCumulativeTrades(account));
        final double averageLoss = trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d < 0).average().orElse(0.0);

        return new AccountStatistics(
                account.getBalance(),
                trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d > 0).average().orElse(0.0),
                averageLoss,
                trades.size(),
                calculateRiskToRewardRatio(account),
                trades.stream().mapToDouble(Trade::getLotSize).sum(),
                trades.stream().mapToDouble(Trade::getNetProfit).average().orElse(0.0),
                this.mathService.wholePercentage(positiveProfitCount, this.mathService.getDouble(trades.size())),
                this.mathService.divide(positiveProfit, Math.abs(negativeProfit)),
                this.mathService.wholePercentage(positiveProfit, this.mathService.add(positiveProfit, Math.abs(negativeProfit))),
                calculateSharpeRatio(account),
                Math.round(trades.stream().mapToLong(tr -> Math.abs(ChronoUnit.SECONDS.between(tr.getTradeOpenTime(), tr.getTradeCloseTime()))).average().orElse(0.0)),
                Math.round(trades.stream().filter(tr -> tr.getNetProfit() > 0).mapToLong(tr -> Math.abs(ChronoUnit.SECONDS.between(tr.getTradeOpenTime(), tr.getTradeCloseTime()))).average().orElse(0.0)),
                Math.round(trades.stream().filter(tr -> tr.getNetProfit() < 0).mapToLong(tr -> Math.abs(ChronoUnit.SECONDS.between(tr.getTradeOpenTime(), tr.getTradeCloseTime()))).average().orElse(0.0)),
                this.mathService.add(Math.abs(drawdown), Math.abs(averageLoss))
        );
    }


    //  HELPERS

    /**
     * Computes the drawdown between cumulative trades
     *
     * @param cumulativeTrades {@link List} of {@link CumulativeTrade}
     * @return drawdown price
     */
    private double calculateDrawdown(final List<CumulativeTrade> cumulativeTrades) {

        double drawdown = 0.0;
        double max = 0.0;

        final List<Triplet<Integer, CumulativeTrade, Boolean>> highEntries = new ArrayList<>();
        for (int i = 0; i < cumulativeTrades.size(); i++) {
            boolean hitMax = cumulativeTrades.get(i).netProfit() > max;
            highEntries.add(Triplet.with(i, cumulativeTrades.get(i), hitMax));

            if (hitMax) {
                max = cumulativeTrades.get(i).netProfit();
            }
        }

        final List<Integer> indices = highEntries.stream().filter(Triplet::getValue2).map(Triplet::getValue0).toList();
        if (CollectionUtils.isNotEmpty(indices) && indices.size() > 1) {
            for (int i = 0; i < indices.size() - 1; i++) {
                final List<CumulativeTrade> subList = cumulativeTrades.subList(indices.get(i), indices.get(i + 1) + 1);
                final double calc = this.mathService.subtract(subList.get(0).netProfit(), subList.stream().mapToDouble(CumulativeTrade::netProfit).min().orElse(0.0));

                if (calc > drawdown) {
                    drawdown = calc;
                }
            }
        }

        return drawdown;
    }

    /**
     * Generates a {@link List} of {@link CumulativeTrade}s
     *
     * @param account {@link Account}
     * @return {@link List} of {@link CumulativeTrade}
     */
    private List<CumulativeTrade> generativeCumulativeTrades(final Account account) {

        final List<Trade> trades = account.getTrades().stream().sorted(Comparator.comparing(Trade::getTradeCloseTime)).toList();
        final List<CumulativeTrade> cumulativeTrades = new ArrayList<>();

        int count = 0;
        double cumProfit = 0.0;
        double cumPoints = 0.0;

        for (final Trade trade : trades) {
            count += 1;
            cumProfit += trade.getNetProfit();
            final double np = Math.abs(this.mathService.subtract(trade.getClosePrice(), trade.getOpenPrice()));
            cumPoints += (trade.getNetProfit() < 0) ? this.mathService.multiply(-1.0, np) : np;

            cumulativeTrades.add(new CumulativeTrade(trade.getTradeCloseTime(), count, trade.getNetProfit(), (trade.getNetProfit() < 0) ? this.mathService.multiply(-1.0, np) : np, cumProfit, cumPoints));
        }

        return cumulativeTrades;
    }

    /**
     * Calculates the risk to reward ratio for this account
     *
     * @param account {@link Account}
     * @return RRR
     */
    private double calculateRiskToRewardRatio(final Account account) {

        final List<Trade> trades =
                account.getTrades()
                        .stream()
                        .sorted(Comparator.comparing(Trade::getTradeCloseTime))
                        .filter(tr -> tr.getStopLoss() > 0.0)
                        .filter(tr -> tr.getTakeProfit() > 0.0)
                        .toList();

        final double averageTP =
                trades
                        .stream()
                        .mapToDouble(tr -> Math.abs(this.mathService.subtract(tr.getTakeProfit(), tr.getOpenPrice())))
                        .average()
                        .orElse(0.0);

        final double averageSL =
                trades
                        .stream()
                        .mapToDouble(tr -> Math.abs(this.mathService.subtract(tr.getStopLoss(), tr.getOpenPrice())))
                        .average()
                        .orElse(0.0);

        return this.mathService.divide(averageTP, averageSL);
    }

    /**
     * Calculates the sharpe ratio based on the monthly returns of the account
     *
     * @param account {@link Account}
     * @return sharpe ratio
     */
    private double calculateSharpeRatio(final Account account) {

        final List<TradeRecord> monthlyRecords = this.tradeRecordService.getTradeRecords(account.getAccountOpenTime().minusYears(1).toLocalDate(), LocalDate.now().plusYears(1), account, FlowerpotTimeInterval.MONTHLY, -1).tradeRecords();
        final double averageMonthlyReturn = monthlyRecords.stream().mapToInt(tr -> this.mathService.wholePercentage(tr.netProfit(), account.getBalance())).average().orElse(0.0);
        final double std = new StandardDeviation().evaluate(monthlyRecords.stream().mapToDouble(TradeRecord::netProfit).toArray());

        if (Double.isNaN(std)) {
            return 0.0;
        } else {
            return this.mathService.divide(this.mathService.subtract(averageMonthlyReturn, CoreConstants.RISK_FREE_RATE_CANADA), std);
        }
    }
}
