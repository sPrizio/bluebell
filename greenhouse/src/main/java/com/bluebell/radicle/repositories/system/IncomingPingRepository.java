package com.bluebell.radicle.repositories.system;

import com.bluebell.platform.models.core.entities.system.IncomingPing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link IncomingPing}
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Repository
public interface IncomingPingRepository extends PagingAndSortingRepository<IncomingPing, Long>, CrudRepository<IncomingPing, Long> {

    /**
     * Looks for the {@link IncomingPing} for the given system name
     *
     * @param systemName system name
     * @return {@link IncomingPing}
     */
    IncomingPing findIncomingPingBySystemName(final String systemName);
}
