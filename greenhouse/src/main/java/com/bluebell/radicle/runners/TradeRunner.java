package com.bluebell.radicle.runners;

import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.services.MathService;
import com.bluebell.radicle.repositories.account.AccountRepository;
import com.bluebell.radicle.repositories.trade.TradeRepository;
import com.bluebell.radicle.services.account.AccountService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 * Generates testing {@link Trade}s
 *
 * @author Stephen Prizio
 * @version 0.2.5
 */
@Component
@Profile("dev")
@ConditionalOnProperty(name = "bluebell.cmdlr.trade.data", havingValue = "true", matchIfMissing = true)
public class TradeRunner extends AbstractRunner implements CommandLineRunner, Ordered {

    @Value("${bluebell.cmdlr.order.trade}")
    private int order;

    private final MathService mathService = new MathService();
    private final Random random = new Random();

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "accountService")
    private AccountService accountService;

    @Resource(name = "tradeRepository")
    private TradeRepository tradeRepository;


    //  METHODS

    @Override
    @Transactional
    public void run(String... args) {

        logStart();

        Account account1 = this.accountRepository.findAccountByAccountNumber(1234);
        Account account2 = this.accountRepository.findAccountByAccountNumber(5678);
        Account account3 = this.accountRepository.findAccountByAccountNumber(9638953);

        generateTrades(account1, 3.0);
        generateTrades(account2, 1.0);
        generateTrades(account3, 1.5);

        account1 = this.accountRepository.findAccountByAccountNumber(1234);
        account2 = this.accountRepository.findAccountByAccountNumber(5678);
        account3 = this.accountRepository.findAccountByAccountNumber(9638953);

        this.accountService.refreshAccount(account1);
        this.accountService.refreshAccount(account2);
        this.accountService.refreshAccount(account3);

        logEnd();
    }

    @Override
    public int getOrder() {
        return this.order;
    }


    //  HELPERS

    /**
     * Generates random trades for an account
     *
     * @param account {@link Account}
     * @param scale profit scale
     */
    private void generateTrades(final Account account, final double scale) {

        final int tradeCount = 25 + this.random.nextInt(351);
        for (int i = 0; i < tradeCount; i++) {
            final TradeType buyOrSell = this.random.nextInt(11) % 2 == 0 ? TradeType.BUY : TradeType.SELL;
            double randomOpenPrice = 17_000 + (this.random.nextDouble() * 4_000);
            randomOpenPrice = Math.round(randomOpenPrice * 100.0) / 100.0;

            double randomPoints = 20.0 + (this.random.nextDouble() * (125.0 - 20.0));
            randomPoints = Math.round(randomPoints * 100.0) / 100.0;
            boolean wasLoss = this.random.nextBoolean();
            double randomClosePrice = randomOpenPrice + (randomPoints * (wasLoss ? -1.0 : 1.0));

            double randomProfit = 20.0 + (this.random.nextDouble() * (150.0 - 20.0));
            randomProfit = Math.round(randomProfit * 100.0) / 100.0;

            final long daysBetween = ChronoUnit.DAYS.between(account.getAccountOpenTime(), LocalDateTime.now());

            final LocalDateTime randomDay =
                    account
                            .getAccountOpenTime()
                            .plusDays(this.random.nextInt((int) daysBetween))
                            .withHour(9 + this.random.nextInt(7))
                            .withMinute(this.random.nextInt(60))
                            .withSecond(this.random.nextInt(60));

            Trade trade = Trade
                    .builder()
                    .tradeId(String.valueOf(100_000_000 + this.random.nextInt(900_000_000)))
                    .product(Math.random() < 0.5 ? "Nasdaq 100" : "S&P 500")
                    .tradePlatform(TradePlatform.METATRADER4)
                    .tradeType(buyOrSell)
                    .tradeOpenTime(randomDay)
                    .tradeCloseTime(randomDay.plusSeconds(this.random.nextInt(350)))
                    .lotSize(1.25)
                    .openPrice(this.mathService.getDouble(randomOpenPrice))
                    .closePrice(this.mathService.getDouble(randomClosePrice))
                    .netProfit(this.mathService.getDouble((wasLoss ? (randomProfit * -1.0) : (randomProfit)) * scale))
                    .stopLoss(this.mathService.getDouble(buyOrSell == TradeType.BUY ? randomOpenPrice - 50.0 : randomOpenPrice + 50.0))
                    .takeProfit(this.mathService.getDouble(buyOrSell == TradeType.BUY ? randomOpenPrice + 150.0 : randomOpenPrice - 100.0))
                    .account(account)
                    .build();

            this.tradeRepository.save(trade);
        }
    }
}
