package com.bluebell.platform.models.core.entities.system;

import com.bluebell.platform.enums.system.PhoneType;
import com.bluebell.platform.models.core.entities.GenericEntity;
import com.bluebell.platform.models.core.entities.security.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * Class representation of a phone number
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
@Entity
@Builder
@Table(name = "phone_numbers")
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumber implements GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column
    private PhoneType phoneType;

    @Setter
    @Column
    private short countryCode;

    @Setter
    @Column
    private long telephoneNumber;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private User user;


    //  METHODS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneNumber that = (PhoneNumber) o;

        if (this.countryCode != that.countryCode) return false;
        if (this.telephoneNumber != that.telephoneNumber) return false;
        return this.phoneType == that.phoneType;
    }

    @Override
    public int hashCode() {
        int result = this.phoneType.hashCode();
        result = 31 * result + this.countryCode;
        result = 31 * result + Long.hashCode(this.telephoneNumber);
        return result;
    }
}
