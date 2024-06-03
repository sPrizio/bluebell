package com.bluebell.flowerpot.core.models.entities.account;

import com.bluebell.flowerpot.core.enums.account.AccountType;
import com.bluebell.flowerpot.core.enums.account.Broker;
import com.bluebell.flowerpot.core.enums.account.Currency;
import com.bluebell.flowerpot.core.enums.trade.platform.TradePlatform;
import com.bluebell.flowerpot.core.models.entities.GenericEntity;
import com.bluebell.flowerpot.core.models.entities.security.User;
import com.bluebell.flowerpot.core.models.entities.trade.Trade;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class representation of a trading account, an entity that can hold {@link Trade}s and other information
 *
 * @author Stephen Prizio
 * @version 0.0.3
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;


    //  METHODS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return accountNumber == account.accountNumber;
    }

    @Override
    public int hashCode() {
        return (int) (accountNumber ^ (accountNumber >>> 32));
    }
}
