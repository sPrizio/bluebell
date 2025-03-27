package com.bluebell.radicle.repositories.market;

import com.bluebell.platform.models.core.entities.market.MarketPrice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link MarketPrice}
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
@Repository
public interface MarketPriceRepository extends PagingAndSortingRepository<MarketPrice, Long>, CrudRepository<MarketPrice, Long> {
}
