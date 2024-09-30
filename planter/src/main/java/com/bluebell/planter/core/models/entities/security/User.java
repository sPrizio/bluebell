package com.bluebell.planter.core.models.entities.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.bluebell.planter.core.enums.security.UserRole;
import com.bluebell.planter.core.models.entities.GenericEntity;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.entities.system.PhoneNumber;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

/**
 * Class representation of an individual that can interact with the system, hold accounts and other information
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
@Getter
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "UniqueEmail", columnNames = {"email"}))
@ToString(exclude = "password")
public class User implements GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

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
    @Column
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
    private List<PhoneNumber> phones;

    @Setter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts;

    @Setter
    @ElementCollection
    private List<UserRole> roles;


    //  METHODS

    /**
     * Sets a secured password
     *
     * @param password password
     */
    public void setPassword(String password) {
        this.password = new String(Base64.getEncoder().encode(password.getBytes(StandardCharsets.UTF_8)));
    }
}
