package com.bluebell.planter.core.models.entities.transaction;

import com.bluebell.planter.core.enums.transaction.TransactionStatus;
import com.bluebell.planter.core.enums.transaction.TransactionType;
import com.bluebell.planter.core.models.entities.GenericEntity;
import com.bluebell.planter.core.models.entities.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Class representation of an account transaction
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@Getter
@Entity
@Table(name = "transactions")
public class Transaction implements GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Setter
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Setter
    @Column
    private String name;

    @Setter
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;

    @Setter
    @Column
    private double amount;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Account account;
}
