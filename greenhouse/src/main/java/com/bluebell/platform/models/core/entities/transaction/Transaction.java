package com.bluebell.platform.models.core.entities.transaction;

import com.bluebell.platform.enums.transaction.TransactionStatus;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.core.entities.GenericEntity;
import com.bluebell.platform.models.core.entities.account.Account;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Class representation of an account transaction
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
@Entity
@Builder
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
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
