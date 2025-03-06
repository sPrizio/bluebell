package com.bluebell.platform.models.core.entities.account;

import com.bluebell.platform.enums.account.AccountType;
import com.bluebell.platform.enums.account.Broker;
import com.bluebell.platform.enums.account.Currency;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.models.core.entities.GenericEntity;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.platform.services.MathService;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Class representation of a trading account, an entity that can hold {@link Trade}s and other information
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
@Setter
@Entity
@Builder
@Table(name = "accounts", uniqueConstraints = @UniqueConstraint(name = "UniqueAccountNumber", columnNames = {"account_number"}))
@NoArgsConstructor
@AllArgsConstructor
public class Account implements GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private boolean defaultAccount;

    @Column
    private LocalDateTime accountOpenTime;

    @Column
    private LocalDateTime accountCloseTime;

    @Column
    private double initialBalance;

    @Column
    private double balance;

    @Column
    private boolean active;

    @Column
    private String name;

    @Column(name = "account_number", unique = true)
    private long accountNumber;

    @Column
    private Currency currency;

    @Column
    private Broker broker;

    @Column
    private AccountType accountType;

    @Column
    private TradePlatform tradePlatform;

    @Column
    private LocalDateTime lastTraded;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private @Builder.Default List<Trade> trades = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private @Builder.Default List<Transaction> transactions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private User user;


    //  METHODS

    /**
     * Refreshes certain account attributes when necessary
     *
     * @return {@link Account} with updated values
     */
    public Account refreshAccount() {

        final MathService mathService = new MathService();

        Account account = this;
        account.setBalance(mathService.add(this.balance, calculateNetProfit()));
        account.setLastTraded(calculateLastTraded());

        return account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return this.accountNumber == account.accountNumber;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.accountNumber);
    }


    //  HELPERS

    /**
     * Calculates the netProfit of this account
     *
     * @return double
     */
    private double calculateNetProfit() {
        if (CollectionUtils.isNotEmpty(this.trades)) {
            return this.trades.stream().mapToDouble(Trade::getNetProfit).sum();
        } else {
            return 0.0;
        }
    }

    /**
     * Calculates the most recently traded date & time
     *
     * @return most recently closed {@link Trade}
     */
    private LocalDateTime calculateLastTraded() {
        if (CollectionUtils.isEmpty(this.trades)) {
            return null;
        } else {
            return this.trades.stream().map(Trade::getTradeCloseTime).max(LocalDateTime::compareTo).orElse(null);
        }
    }
}
