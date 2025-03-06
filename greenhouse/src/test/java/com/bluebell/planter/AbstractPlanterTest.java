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
import com.bluebell.platform.models.core.nonentities.records.portfolio.Portfolio;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioEquityPoint;
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
import java.util.List;

/**
 * Parent-level testing class to provide testing assistance for planter
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
public abstract class AbstractPlanterTest extends AbstractGenericTest {

    /**
     * Generates a test {@link AccountDetails}
     *
     * @return {@link AccountDetails}
     */
    public AccountDetails generateAccountDetails() {
        return new AccountDetails(
                generateTestAccount(),
                91,
                List.of(
                        new AccountEquityPoint(LocalDateTime.of(2025, 3, 4, 11, 12, 13), 55.0, 12.5, 55.0, 12.5),
                        new AccountEquityPoint(LocalDateTime.of(2025, 4, 4, 11, 12, 13), 88.0, 21.36, 143.0, 33.86)
                ),
                new AccountInsights(
                        2,
                        178.63,
                        -213.36,
                        456.32,
                        -698.14,
                        896.36,
                        4.0,
                        3.6,
                        1.58,
                        2.11,
                        7.89
                ),
                new AccountStatistics(
                        30000.0,
                        25.69,
                        -63.69,
                        9,
                        1.89,
                        89.33,
                        4.05,
                        56,
                        2.15,
                        69,
                        0.45,
                        123L,
                        89L,
                        156L,
                        -369.78
                ),
                CorePlatformConstants.RISK_FREE_RATE_CANADA
        );
    }

    /**
     * Generates a test {@link Portfolio}
     *
     * @return {@link Portfolio}
     */
    public Portfolio generatePortfolio() {
        return new Portfolio(
                true,
                1000000.0,
                145,
                13,
                56,
                new PortfolioStatistics(1.0, 1.0, 1.0, 1.0),
                List.of(new PortfolioEquityPoint(LocalDate.of(2025, 3, 4), 639.89, List.of()))
        );
    }

    /**
     * Generates a test {@link TradeLog}
     *
     * @return {@link TradeLog}
     */
    public TradeLog generateTradeLog() {
        return new TradeLog(
                List.of(
                        new TradeLogEntry(
                                LocalDate.of(2025, 3, 4),
                                LocalDate.of(2025, 3, 4),
                                List.of(new TradeLogEntryRecord(generateTestAccount(), 1234L, "Test", new TradeRecordReport(List.of(), null))),
                                new TradeLogEntryRecordTotals(1, 125.66, 89.63, 5, 52)
                        )
                )
        );
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
        return new TradeRecord(
                LocalDate.MIN,
                LocalDate.MAX,
                387.56,
                -96.85,
                104.25,
                -56.89,
                47.36,
                189.25,
                97.55,
                -111.44,
                -74.32,
                56,
                9,
                7,
                16,
                1.83,
                65,
                TradeRecordTimeInterval.DAILY,
                List.of(
                        new TradeRecordEquityPoint(1, 50.0, 10.0, 50.0, 10.0),
                        new TradeRecordEquityPoint(2, -25.0, -5.0, 25.0, 5.0),
                        new TradeRecordEquityPoint(3, 100.0, 20.0, 125.0, 25.0)
                )
        );
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
