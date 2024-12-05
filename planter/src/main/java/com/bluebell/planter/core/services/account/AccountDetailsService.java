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
import com.bluebell.planter.core.models.nonentities.records.trade.TradeRecord;
import com.bluebell.planter.core.services.trade.TradeRecordService;
import com.bluebell.radicle.services.MathService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.bluebell.planter.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer implementation of {@link AccountDetails}
 *
 * @author Stephen Prizio
 * @version 0.0.7
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

        final List<Trade> trades = account.getTrades();
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

        equityPoints.addFirst(new AccountEquityPoint(trades.getFirst().getTradeCloseTime().minusDays(1), 0.0, 0.0, starterBalance, 0.0));
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
        final List<CumulativeTrade> cumulativeTrades = generativeCumulativeTrades(account);

        return new AccountInsights(
                tradeRecords.size(),
                account.getTrades().size(),
                tradeRecords.stream().mapToDouble(TradeRecord::netProfit).min().orElse(0.0),
                cumulativeTrades.stream().mapToDouble(CumulativeTrade::netProfit).min().orElse(0.0),
                tradeRecords.stream().mapToDouble(TradeRecord::netProfit).max().orElse(0.0),
                cumulativeTrades.stream().mapToDouble(CumulativeTrade::netProfit).max().orElse(0.0)
        );
    }

    /**
     * Calculates the account's statistics
     *
     * @param account {@link Account}
     * @return {@link AccountStatistics}
     */
    public AccountStatistics obtainStatistics(final Account account) {

        final List<Trade> trades = account.getTrades();
        final double positiveProfitCount = this.mathService.getDouble(trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d > 0).count());
        final double positiveProfit = this.mathService.getDouble(trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d > 0).sum());
        final double negativeProfit = this.mathService.getDouble(trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d < 0).sum());

        return new AccountStatistics(
                account.getBalance(),
                trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d > 0).average().orElse(0.0),
                trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d < 0).average().orElse(0.0),
                trades.size(),
                calculateRiskToRewardRatio(account),
                trades.stream().mapToDouble(Trade::getLotSize).sum(),
                trades.stream().mapToDouble(Trade::getNetProfit).average().orElse(0.0),
                this.mathService.wholePercentage(positiveProfitCount, this.mathService.getDouble(trades.size())),
                this.mathService.divide(positiveProfit, Math.abs(negativeProfit)),
                this.mathService.wholePercentage(positiveProfit, this.mathService.add(positiveProfit, Math.abs(negativeProfit))),
                calculateSharpeRatio(account)
        );
    }


    //  HELPERS

    /**
     * Generates a {@link List} of {@link CumulativeTrade}s
     *
     * @param account {@link Account}
     * @return {@link List} of {@link CumulativeTrade}
     */
    private List<CumulativeTrade> generativeCumulativeTrades(final Account account) {

        final List<Trade> trades = account.getTrades();
        final List<CumulativeTrade> cumulativeTrades = new ArrayList<>();

        int count = 0;
        double cumProfit = 0.0;
        double cumPoints = 0.0;

        for (final Trade trade : trades) {
            count += 1;
            cumProfit += trade.getNetProfit();
            final double np  = Math.abs(this.mathService.subtract(trade.getClosePrice(), trade.getOpenPrice()));
            cumPoints += (trade.getNetProfit() < 0) ? this.mathService.multiply(-1.0, np) : np;

            cumulativeTrades.add(new CumulativeTrade(trade.getTradeCloseTime(), count, cumProfit, cumPoints));
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
