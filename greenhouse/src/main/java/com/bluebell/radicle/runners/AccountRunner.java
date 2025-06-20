package com.bluebell.radicle.runners;

import com.bluebell.platform.enums.account.AccountType;
import com.bluebell.platform.enums.account.Broker;
import com.bluebell.platform.enums.account.Currency;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.radicle.repositories.account.AccountRepository;
import com.bluebell.radicle.repositories.security.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Generates testing {@link Account}s
 *
 * @author Stephen Prizio
 * @version 0.2.6
 */
@Component
@Profile("dev")
@ConditionalOnProperty(name = "bluebell.cmdlr.account.data", havingValue = "true", matchIfMissing = true)
public class AccountRunner extends AbstractRunner implements CommandLineRunner, Ordered {

    @Value("${bluebell.cmdlr.order.account}")
    private int order;

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "userRepository")
    private UserRepository userRepository;


    //  METHODS

    @Override
    @Transactional
    public void run(String... args) {

        logStart();

        final User user = this.userRepository.findUserByUsername("t.test");
        final Portfolio portfolio = user.getActivePortfolios().get(0);

        Account account1 = Account
                .builder()
                .defaultAccount(true)
                .accountOpenTime(LocalDateTime.now().minusMonths(5))
                .initialBalance(30000)
                .balance(30000)
                .active(true)
                .name("Sprout - Demo")
                .accountNumber(1234)
                .currency(Currency.CANADIAN_DOLLAR)
                .broker(Broker.CMC_MARKETS)
                .accountType(AccountType.CFD)
                .tradePlatform(TradePlatform.METATRADER4)
                .lastTraded(LocalDateTime.now())
                .portfolio(portfolio)
                .build();

        this.accountRepository.save(account1);

        Account account2 = Account
                .builder()
                .defaultAccount(false)
                .accountOpenTime(LocalDateTime.now().minusMonths(3))
                .initialBalance(10000)
                .balance(10000)
                .active(true)
                .name("Sprout - FTMO Evaluation")
                .accountNumber(5678)
                .currency(Currency.US_DOLLAR)
                .broker(Broker.FTMO)
                .accountType(AccountType.CFD)
                .tradePlatform(TradePlatform.METATRADER4)
                .lastTraded(LocalDateTime.now().minusDays(5))
                .portfolio(portfolio)
                .build();

        this.accountRepository.save(account2);

        Account account3 = Account
                .builder()
                .defaultAccount(false)
                .accountOpenTime(LocalDateTime.now().minusMonths(6))
                .accountCloseTime(LocalDateTime.now().minusMonths(2))
                .initialBalance(15000)
                .balance(15000)
                .active(false)
                .name("Demo Test")
                .accountNumber(9638953)
                .currency(Currency.CANADIAN_DOLLAR)
                .broker(Broker.NA)
                .accountType(AccountType.CFD)
                .tradePlatform(TradePlatform.BLUEBELL)
                .lastTraded(LocalDateTime.now().minusMonths(2))
                .portfolio(portfolio)
                .build();

        this.accountRepository.save(account3);

        logEnd();
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}
