package com.bluebell.planter;

import com.bluebell.planter.api.models.dto.account.AccountDTO;
import com.bluebell.planter.core.enums.account.AccountType;
import com.bluebell.planter.core.enums.account.Broker;
import com.bluebell.planter.core.enums.account.Currency;
import com.bluebell.planter.core.enums.news.MarketNewsSeverity;
import com.bluebell.planter.core.enums.security.UserRole;
import com.bluebell.planter.core.enums.system.Country;
import com.bluebell.planter.core.enums.system.FlowerpotTimeInterval;
import com.bluebell.planter.core.enums.system.PhoneType;
import com.bluebell.planter.core.enums.trade.info.TradeType;
import com.bluebell.planter.core.enums.trade.platform.TradePlatform;
import com.bluebell.planter.core.enums.transaction.TransactionStatus;
import com.bluebell.planter.core.enums.transaction.TransactionType;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.entities.news.MarketNews;
import com.bluebell.planter.core.models.entities.news.MarketNewsEntry;
import com.bluebell.planter.core.models.entities.news.MarketNewsSlot;
import com.bluebell.planter.core.models.entities.security.User;
import com.bluebell.planter.core.models.entities.system.PhoneNumber;
import com.bluebell.planter.core.models.entities.trade.Trade;
import com.bluebell.planter.core.models.entities.transaction.Transaction;
import com.bluebell.planter.core.models.nonentities.records.traderecord.TradeRecord;
import com.bluebell.planter.core.models.nonentities.records.traderecord.TradeRecordEquityPoint;
import com.bluebell.planter.integration.models.responses.forexfactory.CalendarNewsEntryResponse;
import com.bluebell.planter.security.constants.SecurityConstants;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Parent-level testing class to provide testing assistance
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
public abstract class AbstractGenericTest {

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
     * Generates a test BUY {@link Trade}
     *
     * @return {@link Trade}
     */
    public Trade generateTestBuyTrade() {

        Trade trade = new Trade();

        trade.setTradeId("testId1");
        trade.setTradePlatform(TradePlatform.CMC_MARKETS);
        trade.setTradeType(TradeType.BUY);
        trade.setClosePrice(13098.67);
        trade.setTradeCloseTime(LocalDateTime.of(2022, 8, 24, 11, 37, 24));
        trade.setTradeOpenTime(LocalDateTime.of(2022, 8, 24, 11, 32, 58));
        trade.setLotSize(0.75);
        trade.setNetProfit(14.85);
        trade.setOpenPrice(13083.41);
        trade.setAccount(generateTestAccount());

        return trade;
    }

    /**
     * Generates a test SELL {@link Trade}
     *
     * @return {@link Trade}
     */
    public Trade generateTestSellTrade() {

        Trade trade = new Trade();

        trade.setTradeId("testId2");
        trade.setTradePlatform(TradePlatform.CMC_MARKETS);
        trade.setTradeType(TradeType.SELL);
        trade.setClosePrice(13156.12);
        trade.setTradeCloseTime(LocalDateTime.of(2022, 8, 24, 10, 24, 36));
        trade.setTradeOpenTime(LocalDateTime.of(2022, 8, 24, 10, 25, 12));
        trade.setLotSize(0.75);
        trade.setNetProfit(-4.50);
        trade.setOpenPrice(13160.09);
        trade.setAccount(generateTestAccount());

        return trade;
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
                FlowerpotTimeInterval.DAILY,
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
     */
    public Transaction generateTestDepositTransaction() {

        Transaction transaction = new Transaction();

        transaction.setTransactionDate(LocalDateTime.of(2024, 12, 6, 12, 0, 0));
        transaction.setName("Test Deposit");
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setAmount(125.0);
        transaction.setAccount(generateTestAccount());

        return transaction;
    }

    /**
     * Generates a test withdrawal {@link Transaction}
     *
     * @return {@link Transaction}
     */
    public Transaction generateTestWithdrawalTransaction() {

        Transaction transaction = new Transaction();

        transaction.setTransactionDate(LocalDateTime.of(2024, 12, 6, 14, 0, 0));
        transaction.setName("Test Withdrawal");
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setTransactionStatus(TransactionStatus.FAILED);
        transaction.setAmount(-96.30);
        transaction.setAccount(generateTestAccount());

        return transaction;
    }

    /**
     * Generates a test {@link Account}
     *
     * @return {@link Account}
     */
    public Account generateTestAccount() {

        Account account = new Account();

        account.setId(-1L);
        account.setDefaultAccount(true);
        account.setAccountOpenTime(LocalDateTime.of(2022, 10, 25, 22, 48, 0));
        account.setBalance(1000.0);
        account.setInitialBalance(1000.0);
        account.setActive(true);
        account.setAccountType(AccountType.CFD);
        account.setAccountNumber(1234);
        account.setName("Test Account");
        account.setCurrency(Currency.CANADIAN_DOLLAR);
        account.setBroker(Broker.CMC_MARKETS);
        account.setTradePlatform(TradePlatform.CMC_MARKETS);

        return account;
    }

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

    /**
     * Generates a test {@link User}
     *
     * @return {@link User}
     */
    public User generateTestUser() {
        User user = new User();

        user.setAccounts(List.of(generateTestAccount()));
        user.setEmail("test@email.com");
        user.setUsername("s.prizio");
        user.setPassword("1234");
        user.setFirstName("Stephen");
        user.setLastName("Test");
        user.setPhones(List.of(generateTestPhoneNumber()));
        user.setRoles(List.of(UserRole.ADMINISTRATOR, UserRole.TRADER));

        return user;
    }

    /**
     * Generates a test {@link PhoneNumber}
     *
     * @return {@link PhoneNumber}
     */
    public PhoneNumber generateTestPhoneNumber() {

        final PhoneNumber phoneNumber = new PhoneNumber();

        phoneNumber.setPhoneType(PhoneType.MOBILE);
        phoneNumber.setTelephoneNumber(1112223333);
        phoneNumber.setCountryCode((short) 1);

        return phoneNumber;
    }

    /**
     * Generates a test {@link MarketNewsEntry}
     *
     * @return {@link MarketNewsEntry}
     */
    public MarketNewsEntry generateTestMarketNewsEntry() {

        final MarketNewsEntry marketNewsEntry = new MarketNewsEntry();

        marketNewsEntry.setContent("Test News Entry");
        marketNewsEntry.setSeverity(MarketNewsSeverity.DANGEROUS);
        marketNewsEntry.setCountry(Country.CANADA);

        return marketNewsEntry;
    }

    /**
     * Generates a test {@link MarketNewsSlot}
     *
     * @return {@link MarketNewsSlot}
     */
    public MarketNewsSlot generateTestMarketNewsSlot() {

        final MarketNewsSlot marketNewsSlot = new MarketNewsSlot();

        marketNewsSlot.setTime(LocalTime.of(13, 10));
        marketNewsSlot.setEntries(new ArrayList<>(List.of(generateTestMarketNewsEntry())));

        return marketNewsSlot;
    }

    /**
     * Generates a test {@link MarketNews}
     *
     * @return {@link MarketNews}
     */
    public MarketNews generateMarketNews() {

        final MarketNews marketNews = new MarketNews();

        marketNews.setDate(LocalDate.of(2023, 1, 19));
        marketNews.setSlots(new ArrayList<>(List.of(generateTestMarketNewsSlot())));

        return marketNews;
    }

    /**
     * Generates a test {@link CalendarNewsEntryResponse}
     *
     * @return {@link CalendarNewsEntryResponse}
     */
    public CalendarNewsEntryResponse generateCalendarNewsEntryResponse() {
        return new CalendarNewsEntryResponse("Currency Account", "CAD", "2023-05-30T08:30:00-04:00", "Low", "-9.9B", "-10.6B", "");
    }
}
