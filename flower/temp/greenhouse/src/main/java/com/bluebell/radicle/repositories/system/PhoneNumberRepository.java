package com.bluebell.radicle.repositories.system;

import com.bluebell.platform.enums.system.PhoneType;
import com.bluebell.platform.models.core.entities.system.PhoneNumber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link PhoneNumber} entities
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Repository
public interface PhoneNumberRepository extends PagingAndSortingRepository<PhoneNumber, Long>, CrudRepository<PhoneNumber, Long> {

    /**
     * Returns a {@link PhoneNumber} for the given {@link PhoneType}, country code and telephone number
     *
     * @param phoneType       {@link PhoneType}
     * @param countryCode     country code, ex: 1 for USA/Canada
     * @param telephoneNumber actual phone number
     * @return {@link PhoneNumber}
     */
    PhoneNumber findPhoneNumberByPhoneTypeAndCountryCodeAndTelephoneNumber(final PhoneType phoneType, final short countryCode, final long telephoneNumber);
}
