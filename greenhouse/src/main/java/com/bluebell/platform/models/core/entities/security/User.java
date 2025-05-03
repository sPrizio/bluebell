package com.bluebell.platform.models.core.entities.security;

import com.bluebell.platform.enums.security.UserRole;
import com.bluebell.platform.models.core.entities.GenericEntity;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.system.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * Class representation of an individual that can interact with the system, hold accounts and other information
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@Getter
@Entity
@Builder
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "UniqueEmail", columnNames = {"email"}))
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
public class User implements GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column
    private String apiToken;

    @Setter
    @Column
    private String firstName;

    @Setter
    @Column
    private String lastName;

    @Setter
    @Column(unique = true, name = "username")
    private String username;

    @Setter
    @Column(unique = true, name = "email")
    private String email;

    @Column
    @JsonIgnore
    private String password;

    @Setter
    @Column
    private LocalDateTime dateRegistered;

    @Setter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private @Builder.Default List<PhoneNumber> phones = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private @Builder.Default List<Portfolio> portfolios = new ArrayList<>();

    @Setter
    @ElementCollection
    private @Builder.Default List<UserRole> roles = new ArrayList<>();


    //  METHODS

    /**
     * Sets a secured password
     *
     * @param password password
     */
    public void setPassword(String password) {
        this.password = new String(Base64.getEncoder().encode(password.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Returns a list of portfolios that are marked as active
     *
     * @return {@link List} of {@link Portfolio}
     */
    public List<Portfolio> getActivePortfolios() {
        if (CollectionUtils.isEmpty(this.portfolios)) {
            return Collections.emptyList();
        }

        return this.portfolios.stream().filter(Portfolio::isActive).toList();
    }
}
