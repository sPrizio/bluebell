package com.bluebell.platform.models.core.entities.portfolio;

import com.bluebell.platform.models.core.entities.GenericEntity;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.security.User;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representation of a collection of {@link Account}s
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@Getter
@Entity
@Builder
@Table(name = "portfolios", uniqueConstraints = @UniqueConstraint(name = "UniquePortfolioNumberAndUser", columnNames = {"portfolio_number", "user_id"}))
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio implements GenericEntity {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "portfolio_number")
    private @Builder.Default long portfolioNumber = -1L;

    @Setter
    @Column
    private String name;

    @Setter
    @Column
    private boolean active;

    @Column
    private @Builder.Default LocalDateTime created = LocalDateTime.now();

    @Setter
    @Column
    private boolean defaultPortfolio;

    @Setter
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private @Builder.Default List<Account> accounts = new ArrayList<>();

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id")
    private User user;


    //  METHODS

    /**
     * Returns a list of active accounts
     *
     * @return {@link List} of {@link Account}
     */
    public List<Account> getActiveAccounts() {
        if (CollectionUtils.isEmpty(this.accounts)) {
            return Collections.emptyList();
        }

        return this.accounts.stream().filter(Account::isActive).toList();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;

        Portfolio portfolio = (Portfolio) object;
        return this.id.equals(portfolio.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
