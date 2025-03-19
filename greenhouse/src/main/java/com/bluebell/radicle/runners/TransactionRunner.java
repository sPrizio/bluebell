package com.bluebell.radicle.runners;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.transaction.TransactionStatus;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.platform.services.MathService;
import com.bluebell.radicle.repositories.account.AccountRepository;
import com.bluebell.radicle.repositories.transaction.TransactionRepository;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Generates testing {@link Transaction}s
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@Component
@Order(5)
@Profile("dev")
public class TransactionRunner extends AbstractRunner implements CommandLineRunner {

    private static final List<String> WORDS = new ArrayList<>(CorePlatformConstants.RANDOM_WORDS);
    private final MathService mathService = new MathService();
    private final Random random = new Random();

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "transactionRepository")
    private TransactionRepository transactionRepository;


    //  METHODS

    @Override
    @Transactional
    public void run(String... args) {

        logStart();

        Account account1 = this.accountRepository.findAccountByAccountNumber(1234);
        Account account2 = this.accountRepository.findAccountByAccountNumber(5678);
        Account account3 = this.accountRepository.findAccountByAccountNumber(9638953);

        generateTransactions(account1);
        generateTransactions(account2);
        generateTransactions(account3);

        logEnd();
    }


    //  HELPERS

    /**
     * Generates random transactions for an account
     *
     * @param account {@link Account}
     */
    private void generateTransactions(final Account account) {

        final int transactionCount = 1 + this.random.nextInt(25);
        for (int i = 0; i < transactionCount; i++) {
            final TransactionType randomType = this.random.nextInt(11) % 2 == 0 ? TransactionType.DEPOSIT : TransactionType.WITHDRAWAL;
            final TransactionStatus randomStatus = TransactionStatus.values()[this.random.nextInt(TransactionStatus.values().length)];
            double randomAmount = 20.0 + (this.random.nextDouble() * (150.0 - 20.0));
            randomAmount = Math.round(randomAmount * 100.0) / 100.0;
            final LocalDateTime randomDay =
                    account
                            .getAccountOpenTime()
                            .plusDays(this.random.nextInt(44))
                            .withHour(this.random.nextInt(24))
                            .withMinute(this.random.nextInt(60))
                            .withSecond(this.random.nextInt(60));

            final double amount = this.mathService.multiply(randomAmount, 2.0 + this.random.nextInt(12));

            Transaction transaction = Transaction
                    .builder()
                    .transactionType(randomType)
                    .transactionDate(randomDay)
                    .name(getRandomName(randomType))
                    .transactionStatus(randomStatus)
                    .amount(this.mathService.getDouble(randomType == TransactionType.WITHDRAWAL ? (amount * -1.0) : amount))
                    .account(account)
                    .build();

            this.transactionRepository.save(transaction);
        }
    }

    /**
     * Generates a random transaction name
     * @param transactionType {@link TransactionType}
     * @return string
     */
    private String getRandomName(final TransactionType transactionType) {

        Collections.shuffle(WORDS);
        final List<String> selectedWords = WORDS
                .subList(0, 1 + this.random.nextInt(3))
                .stream()
                .map(StringUtils::capitalize)
                .toList();

        return String.join(" ", selectedWords) + " " + transactionType.getLabel();
    }
}
