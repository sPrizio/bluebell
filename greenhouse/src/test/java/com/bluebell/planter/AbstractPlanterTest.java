package com.bluebell.planter;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.system.TradeRecordTimeInterval;
import com.bluebell.platform.enums.transaction.TransactionStatus;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.api.dto.account.AccountDTO;
import com.bluebell.platform.models.api.dto.transaction.TransactionDTO;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import com.bluebell.platform.models.core.nonentities.records.account.AccountDetails;
import com.bluebell.platform.models.core.nonentities.records.account.AccountEquityPoint;
import com.bluebell.platform.models.core.nonentities.records.account.AccountInsights;
import com.bluebell.platform.models.core.nonentities.records.account.AccountStatistics;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioEquityPoint;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioRecord;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioStatistics;
import com.bluebell.platform.models.core.nonentities.records.tradelog.TradeLog;
import com.bluebell.platform.models.core.nonentities.records.tradelog.TradeLogEntry;
import com.bluebell.platform.models.core.nonentities.records.tradelog.TradeLogEntryRecord;
import com.bluebell.platform.models.core.nonentities.records.tradelog.TradeLogEntryRecordTotals;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecord;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecordEquityPoint;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecordReport;
import com.bluebell.radicle.security.constants.SecurityConstants;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Parent-level testing class to provide testing assistance for planter
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
public abstract class AbstractPlanterTest extends AbstractGenericTest {

    /**
     * Generates a test {@link AccountDetails}
     *
     * @return {@link AccountDetails}
     */
    public AccountDetails generateAccountDetails() {
        return AccountDetails
                .builder()
                .account(generateTestAccount())
                .consistency(91)
                .equity(List.of(
                        AccountEquityPoint.builder().date(LocalDateTime.of(2025, 3, 4, 11, 12, 13)).amount(55.0).points(12.5).cumAmount(55.0).cumPoints(12.5).build(),
                        AccountEquityPoint.builder().date(LocalDateTime.of(2025, 4, 4, 11, 12, 13)).amount(88.0).points(21.36).cumAmount(143.0).cumPoints(33.86).build()
                ))
                .insights(
                        AccountInsights
                                .builder()
                                .tradingDays(2)
                                .currentPL(178.63)
                                .biggestLoss(-213.36)
                                .largestGain(456.32)
                                .drawdown(-698.14)
                                .maxProfit(896.36)
                                .currentPLDelta(4.0)
                                .biggestLossDelta(3.6)
                                .largestGainDelta(1.58)
                                .drawdownDelta(2.11)
                                .maxProfitDelta(7.89)
                                .build()
                )
                .statistics(
                        AccountStatistics
                                .builder()
                                .balance(30000.0)
                                .averageProfit(25.69)
                                .averageLoss(-63.39)
                                .numberOfTrades(9)
                                .rrr(1.89)
                                .lots(89.33)
                                .expectancy(4.05)
                                .winPercentage(56)
                                .profitFactor(2.15)
                                .retention(69)
                                .sharpeRatio(0.45)
                                .tradeDuration(123L)
                                .winDuration(89L)
                                .lossDuration(156L)
                                .assumedDrawdown(-369.78)
                                .build()
                )
                .riskFreeRate(CorePlatformConstants.RISK_FREE_RATE_CANADA)
                .build();
    }

    /**
     * Generates a test {@link PortfolioRecord}
     *
     * @return {@link PortfolioRecord}
     */
    public PortfolioRecord generatePortfolioRecord() {
        return PortfolioRecord
                .builder()
                .newPortfolio(true)
                .netWorth(1000000.0)
                .trades(145)
                .deposits(13)
                .withdrawals(56)
                .statistics(
                        PortfolioStatistics
                                .builder()
                                .deltaNetWorth(1.0)
                                .deltaTrades(1.0)
                                .deltaDeposits(1.0)
                                .deltaWithdrawals(1.0)
                                .build()
                )
                .equity(List.of(PortfolioEquityPoint.builder().date(LocalDate.of(2025, 3, 4)).portfolio(639.89).accounts(Collections.emptyList()).build()))
                .build();
    }

    /**
     * Generates a test {@link TradeLog}
     *
     * @return {@link TradeLog}
     */
    public TradeLog generateTradeLog() {
        return TradeLog
                .builder()
                .entries(
                        List.of(
                                TradeLogEntry
                                        .builder()
                                        .start(LocalDate.of(2025, 3, 4))
                                        .end(LocalDate.of(2025, 4, 4))
                                        .records(List.of(
                                                TradeLogEntryRecord
                                                        .builder()
                                                        .account(generateTestAccount())
                                                        .accountNumber(1234L)
                                                        .accountName("Test")
                                                        .report(TradeRecordReport.builder().tradeRecords(Collections.emptyList()).tradeRecordTotals(null).build())
                                                        .build()
                                        ))
                                        .totals(
                                                TradeLogEntryRecordTotals
                                                        .builder()
                                                        .accountsTraded(1)
                                                        .netProfit(125.66)
                                                        .netPoints(89.63)
                                                        .trades(5)
                                                        .winPercentage(52)
                                                        .build()
                                        )
                                        .build()
                        )
                )
                .build();
    }

    /**
     * Generates a test {@link TransactionDTO}
     *
     * @return {@link TransactionDTO}
     */
    public TransactionDTO generateTransactionDTO() {
        return TransactionDTO
                .builder()
                .uid("MTE4")
                .transactionType(EnumDisplay.builder().code(TransactionType.DEPOSIT.getCode()).label(TransactionType.DEPOSIT.getLabel()).build())
                .transactionDate(LocalDateTime.of(2025, 3, 4, 11, 12, 13))
                .name("Test")
                .transactionStatus(EnumDisplay.builder().code(TransactionStatus.COMPLETED.getCode()).label(TransactionStatus.COMPLETED.getLabel()).build())
                .amount(1563.66)
                .build();
    }

    /**
     * Generates a {@link User} context for use within the testing suite
     *
     * @return {@link RequestPostProcessor}
     */
    public RequestPostProcessor testUserContext() {
        return request -> {
            // EasyMock initializations...
            request.setAttribute(SecurityConstants.USER_REQUEST_KEY, generateTestUser());
            return request;
        };
    }

    /**
     * Generates a test {@link TradeRecord}
     *
     * @return {@link TradeRecord}
     */
    public TradeRecord generateTradeRecord() {
        return TradeRecord
                .builder()
                .start(LocalDate.MIN)
                .end(LocalDate.MAX)
                .netProfit(387.56)
                .lowestPoint(-96.85)
                .pointsGained(104.25)
                .pointsLost(-56.89)
                .points(47.36)
                .largestWin(189.25)
                .winAverage(97.55)
                .largestLoss(-111.44)
                .lossAverage(-74.32)
                .winPercentage(56)
                .wins(9)
                .losses(7)
                .trades(16)
                .profitability(1.83)
                .retention(65)
                .interval(TradeRecordTimeInterval.DAILY)
                .equityPoints(
                        List.of(
                                TradeRecordEquityPoint.builder().count(1).amount(50.0).points(10.0).cumAmount(50.0).cumPoints(10.0).build(),
                                TradeRecordEquityPoint.builder().count(2).amount(-25.0).points(-5.0).cumAmount(25.0).cumPoints(5.0).build(),
                                TradeRecordEquityPoint.builder().count(3).amount(100.0).points(20.0).cumAmount(125.0).cumPoints(25.0).build()
                        )
                )
                .build();
    }

    /**
     * Generates a test {@link AccountDTO}
     *
     * @return {@link AccountDTO}
     */
    public AccountDTO generateTestAccountDTO() {
        return AccountDTO
                .builder()
                .accountOpenTime(LocalDateTime.of(2022, 10, 25, 22, 48, 0))
                .balance(1000.0)
                .active(true)
                .build();
    }
}
