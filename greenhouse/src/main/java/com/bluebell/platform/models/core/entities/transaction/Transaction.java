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
 * @version 0.1.8
 */
@Getter
@Entity
@Builder
@Table(name = "transactions", uniqueConstraints = @UniqueConstraint(name = "UniqueNameAndDateTimeAndAccount", columnNames = {"transaction_name", "transaction_date", "account_id"}))
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
    @Column(name = "transaction_name")
    private String name;

    @Setter
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;

    @Setter
    @Column(name = "transaction_amount")
    private double amount;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Account account;


    //  METHODS

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;
        return this.name.equals(that.name) && this.account.getId().equals(that.account.getId());
    }

    @Override
    public int hashCode() {
        int result = this.name.hashCode();
        result = 31 * result + Long.hashCode(this.account.getId());
        return result;
    }
}
