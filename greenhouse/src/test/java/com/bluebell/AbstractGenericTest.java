package com.bluebell;

import com.bluebell.platform.enums.account.AccountType;
import com.bluebell.platform.enums.account.Broker;
import com.bluebell.platform.enums.account.Currency;
import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.enums.news.MarketNewsSeverity;
import com.bluebell.platform.enums.security.UserRole;
import com.bluebell.platform.enums.system.Country;
import com.bluebell.platform.enums.system.PhoneType;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.enums.transaction.TransactionStatus;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.platform.models.core.entities.job.impl.JobResult;
import com.bluebell.platform.models.core.entities.job.impl.JobResultEntry;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.models.core.entities.news.MarketNews;
import com.bluebell.platform.models.core.entities.news.MarketNewsEntry;
import com.bluebell.platform.models.core.entities.news.MarketNewsSlot;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.entities.system.PhoneNumber;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.integration.models.responses.forexfactory.CalendarNewsEntryResponse;
import com.bluebell.radicle.performable.impl.FetchMarketNewsActionPerformable;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Parent-level testing class to provide testing assistance for the project
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
public abstract class AbstractGenericTest {

    /**
     * Generates a test BUY {@link Trade}
     *
     * @return {@link Trade}
     */
    public Trade generateTestBuyTrade() {
        return Trade.builder()
                .tradeId("testId1")
                .tradePlatform(TradePlatform.CMC_MARKETS)
                .tradeType(TradeType.BUY)
                .closePrice(13098.67)
                .tradeCloseTime(LocalDateTime.of(2022, 8, 24, 11, 37, 24))
                .tradeOpenTime(LocalDateTime.of(2022, 8, 24, 11, 32, 58))
                .lotSize(0.75)
                .netProfit(14.85)
                .openPrice(13083.41)
                .account(generateTestAccount())
                .build();
    }

    /**
     * Generates a test SELL {@link Trade}
     *
     * @return {@link Trade}
     */
    public Trade generateTestSellTrade() {
        return Trade.builder()
                .tradeId("testId2")
                .tradePlatform(TradePlatform.CMC_MARKETS)
                .tradeType(TradeType.SELL)
                .closePrice(13156.12)
                .tradeCloseTime(LocalDateTime.of(2022, 8, 24, 10, 24, 36))
                .tradeOpenTime(LocalDateTime.of(2022, 8, 24, 10, 25, 12))
                .lotSize(0.75)
                .netProfit(-4.50)
                .openPrice(13160.09)
                .account(generateTestAccount())
                .build();
    }

    /**
     * Generates a test {@link Account}
     *
     * @return {@link Account}
     */
    public Account generateTestAccount() {
        return Account
                .builder()
                .id(-1L)
                .defaultAccount(true)
                .accountOpenTime(LocalDateTime.of(2022, 8, 24, 22, 48, 0))
                .balance(1000.0)
                .initialBalance(1000.0)
                .active(true)
                .accountType(AccountType.CFD)
                .accountNumber(1234)
                .name("Test Account")
                .currency(Currency.CANADIAN_DOLLAR)
                .broker(Broker.CMC_MARKETS)
                .tradePlatform(TradePlatform.CMC_MARKETS)
                .lastTraded(LocalDateTime.of(2022, 10, 25, 11, 37, 24))
                .build();
    }

    /**
     * Generates a test {@link Portfolio}
     *
     * @return {@link Portfolio}
     */
    public Portfolio generateTestPortfolio() {
        return Portfolio
                .builder()
                .id(1L)
                .name("Test Portfolio")
                .active(true)
                .defaultPortfolio(true)
                .accounts(new ArrayList<>(List.of(generateTestAccount())))
                .build();
    }

    /**
     * Generates a test {@link User}
     *
     * @return {@link User}
     */
    public User generateTestUser() {
        return User
                .builder()
                .portfolios(new ArrayList<>(List.of(generateTestPortfolio())))
                .email("test@email.com")
                .username("s.prizio")
                .password("1234")
                .firstName("Stephen")
                .lastName("Test")
                .phones(new ArrayList<>(List.of(generateTestPhoneNumber())))
                .roles(new ArrayList<>(List.of(UserRole.ADMINISTRATOR, UserRole.TRADER)))
                .build();
    }

    /**
     * Generates a test {@link PhoneNumber}
     *
     * @return {@link PhoneNumber}
     */
    public PhoneNumber generateTestPhoneNumber() {
        return PhoneNumber
                .builder()
                .phoneType(PhoneType.MOBILE)
                .telephoneNumber(1112223333)
                .countryCode((short) 1)
                .build();
    }

    /**
     * Generates a test {@link MarketNewsEntry}
     *
     * @return {@link MarketNewsEntry}
     */
    public MarketNewsEntry generateTestMarketNewsEntry() {
        return MarketNewsEntry
                .builder()
                .content("Test News Entry")
                .severity(MarketNewsSeverity.DANGEROUS)
                .country(Country.CANADA)
                .build();
    }

    /**
     * Generates a test {@link MarketNewsSlot}
     *
     * @return {@link MarketNewsSlot}
     */
    public MarketNewsSlot generateTestMarketNewsSlot() {
        return MarketNewsSlot
                .builder()
                .time(LocalTime.of(13, 10))
                .entries(new ArrayList<>(List.of(generateTestMarketNewsEntry())))
                .build();
    }

    /**
     * Generates a test {@link MarketNews}
     *
     * @return {@link MarketNews}
     */
    public MarketNews generateMarketNews() {
        return MarketNews
                .builder()
                .date(LocalDate.of(2023, 1, 19))
                .slots(new ArrayList<>(List.of(generateTestMarketNewsSlot())))
                .build();
    }

    /**
     * Generates a test {@link CalendarNewsEntryResponse}
     *
     * @return {@link CalendarNewsEntryResponse}
     */
    public CalendarNewsEntryResponse generateCalendarNewsEntryResponse() {
        return CalendarNewsEntryResponse
                .builder()
                .title("Currency Account")
                .country("CAD")
                .date("2023-05-30T08:30:00-04:00")
                .impact("Low")
                .forecast("-9.9B")
                .previous("-10.6B")
                .url(StringUtils.EMPTY)
                .build();
    }

    /**
     * Generates a test deposit {@link Transaction}
     *
     * @param account {@link Account}
     * @return {@link Transaction}
     */
    public Transaction generateTestTransactionDeposit(final Account account) {
        return Transaction.builder()
                .name("Test Transaction Deposit")
                .transactionDate(LocalDateTime.of(2022, 8, new Random().nextInt(28) + 1, 12, 24, 36))
                .transactionType(TransactionType.DEPOSIT)
                .amount(123.45)
                .transactionStatus(TransactionStatus.COMPLETED)
                .account(account)
                .build();
    }

    /**
     * Generates a test withdrawal {@link Transaction}
     *
     * @param account {@link Account}
     * @return {@link Transaction}
     */
    public Transaction generateTestTransactionWithdrawal(final Account account) {
        return Transaction.builder()
                .name("Test Transaction Withdrawal")
                .transactionDate(LocalDateTime.of(2022, 8, new Random().nextInt(28) + 1, 12, 24, 36))
                .transactionType(TransactionType.WITHDRAWAL)
                .amount(-563.36)
                .transactionStatus(TransactionStatus.IN_PROGRESS)
                .account(account)
                .build();
    }

    /**
     * Generates a test {@link Action}
     *
     * @return {@link Action}
     */
    public Action generateTestAction() {
        return Action
                .builder()
                .performableAction(new FetchMarketNewsActionPerformable())
                .priority(1)
                .name("Test Action")
                .build();
    }

    /**
     * Generates a test {@link JobResultEntry}
     *
     * @return {@link JobResultEntry}
     */
    public JobResultEntry generateTestJobResultEntry() {
        return JobResultEntry
                .builder()
                .logs("This is a log message")
                .data("Test Data")
                .success(true)
                .build();
    }

    /**
     * Generates a test {@link JobResult}
     *
     * @return {@link JobResult}
     */
    public JobResult generateTestJobResult() {

        final JobResult res = JobResult
                .builder()
                .entries(List.of(generateTestJobResultEntry()))
                .build();

        res.setJob(generateTestJob());
        return res;
    }

    /**
     * Generates a test {@link Job}
     *
     * @return {@link Job}
     */
    public Job generateTestJob() {
        return Job
                .builder()
                .type(JobType.FETCH_MARKET_NEWS)
                .name("Test Job")
                .build();
    }

    /**
     * Generates a test {@link MarketPrice}
     *
     * @return {@link MarketPrice}
     */
    public MarketPrice generateTestMarketPrice() {
        return MarketPrice
                .builder()
                .dataSource(DataSource.FIRST_RATE_DATA)
                .interval(MarketPriceTimeInterval.THIRTY_MINUTE)
                .symbol("Testing")
                .open(11234.05)
                .high(12365.89)
                .low(10258.30)
                .close(11856.34)
                .volume(5689L)
                .build();
    }
}
