package com.bluebell.planter.core.models.entities.account;

import com.bluebell.planter.core.enums.account.AccountType;
import com.bluebell.planter.core.enums.account.Broker;
import com.bluebell.planter.core.enums.account.Currency;
import com.bluebell.planter.core.enums.trade.platform.TradePlatform;
import com.bluebell.planter.core.models.entities.GenericEntity;
import com.bluebell.planter.core.models.entities.security.User;
import com.bluebell.planter.core.models.entities.trade.Trade;
import com.bluebell.planter.core.models.entities.transaction.Transaction;
import com.bluebell.radicle.services.MathService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class representation of a trading account, an entity that can hold {@link Trade}s and other information
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@Getter
@Setter
@Entity
@Table(name = "accounts", uniqueConstraints = @UniqueConstraint(name = "UniqueAccountNumber", columnNames = {"account_number"}))
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
    private List<Trade> trades;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

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

        return accountNumber == account.accountNumber;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(accountNumber);
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
            return this.trades.stream().map(Trade::getTradeCloseTime).max(LocalDateTime::compareTo).get();
        }
    }
}
