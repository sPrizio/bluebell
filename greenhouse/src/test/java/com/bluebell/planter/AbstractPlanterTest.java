package com.bluebell.planter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.enums.system.TradeRecordTimeInterval;
import com.bluebell.platform.models.api.dto.account.AccountDTO;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecord;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecordEquityPoint;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

/**
 * Parent-level testing class to provide testing assistance for planter
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
public abstract class AbstractPlanterTest extends AbstractGenericTest {

    /**
     * Generates a {@link User} context for use within the testing suite
     *
     * @return {@link RequestPostProcessor}
     */
    /*public RequestPostProcessor testUserContext() {
        return request -> {
            // EasyMock initializations...
            request.setAttribute(SecurityConstants.USER_REQUEST_KEY, generateTestUser());
            return request;
        };
    }
*/
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
     * Generates a test deposit {@link Transaction}
     *
     * @return {@link Transaction}
    public Transaction generateTestDepositTransaction() {

        Transaction transaction = new Transaction();

        transaction.setTransactionDate(LocalDateTime.of(2024, 12, 6, 12, 0, 0));
        transaction.setName("Test Deposit");
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setAmount(125.0);
        transaction.setAccount(generateTestAccount());

        return transaction;
    }*/

    /**
     * Generates a test withdrawal {@link Transaction}
     *
     * @return {@link Transaction}
     */
    /*public Transaction generateTestWithdrawalTransaction() {

        Transaction transaction = new Transaction();

        transaction.setTransactionDate(LocalDateTime.of(2024, 12, 6, 14, 0, 0));
        transaction.setName("Test Withdrawal");
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setTransactionStatus(TransactionStatus.FAILED);
        transaction.setAmount(-96.30);
        transaction.setAccount(generateTestAccount());

        return transaction;
    }*/

    /**
     * Generates a test {@link AccountDTO}
     *
     * @return {@link AccountDTO}
     */
    public AccountDTO generateTestAccountDTO() {

        AccountDTO accountDTO = new AccountDTO();

        accountDTO.setAccountOpenTime(LocalDateTime.of(2022, 10, 25, 22, 48, 0));
        accountDTO.setBalance(1000.0);
        accountDTO.setActive(true);

        return accountDTO;
    }
}
