package com.bluebell;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bluebell.platform.enums.account.AccountType;
import com.bluebell.platform.enums.account.Broker;
import com.bluebell.platform.enums.account.Currency;
import com.bluebell.platform.enums.news.MarketNewsSeverity;
import com.bluebell.platform.enums.security.UserRole;
import com.bluebell.platform.enums.system.Country;
import com.bluebell.platform.enums.system.PhoneType;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.enums.transaction.TransactionStatus;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.news.MarketNews;
import com.bluebell.platform.models.core.entities.news.MarketNewsEntry;
import com.bluebell.platform.models.core.entities.news.MarketNewsSlot;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.entities.system.PhoneNumber;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.radicle.integration.models.responses.forexfactory.CalendarNewsEntryResponse;

/**
 * Parent-level testing class to provide testing assistance for radicle
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
public abstract class AbstractGenericTest {

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
        account.setLastTraded(LocalDateTime.of(2022, 8, 24, 11, 37, 24));

        return account;
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

    /**
     * Generates a test deposit {@link Transaction}
     *
     * @param account {@link Account}
     * @return {@link Transaction}
     */
    public Transaction generateTestTransactionDeposit(final Account account) {

        final Transaction transaction = new Transaction();

        transaction.setName("Test Transaction Deposit");
        transaction.setTransactionDate(LocalDateTime.of(2022, 8, new Random().nextInt(28) + 1, 12, 24, 36));
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(123.45);
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setAccount(account);

        return transaction;
    }

    /**
     * Generates a test withdrawal {@link Transaction}
     *
     * @param account {@link Account}
     * @return {@link Transaction}
     */
    public Transaction generateTestTransactionWithdrawal(final Account account) {

        final Transaction transaction = new Transaction();

        transaction.setName("Test Transaction Withdrawal");
        transaction.setTransactionDate(LocalDateTime.of(2022, 8, new Random().nextInt(28) + 1, 12, 24, 36));
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setAmount(-563.36);
        transaction.setTransactionStatus(TransactionStatus.IN_PROGRESS);
        transaction.setAccount(account);

        return transaction;
    }
}
