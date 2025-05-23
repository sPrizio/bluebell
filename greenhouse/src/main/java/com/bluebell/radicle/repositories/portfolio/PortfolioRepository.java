package com.bluebell.radicle.repositories.portfolio;

import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link Portfolio}
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@Repository
public interface PortfolioRepository extends PagingAndSortingRepository<Portfolio, Long>, CrudRepository<Portfolio, Long> {

    /**
     * Returns a {@link Portfolio} for the given portfolio number
     *
     * @param portfolioNumber portfolio number
     * @return {@link Portfolio}
     */
    Portfolio findPortfolioByPortfolioNumber(final long portfolioNumber);
}
