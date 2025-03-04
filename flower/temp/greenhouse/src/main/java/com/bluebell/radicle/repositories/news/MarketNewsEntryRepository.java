package com.bluebell.radicle.repositories.news;

import com.bluebell.platform.models.core.entities.news.MarketNewsEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link MarketNewsEntry}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Repository
public interface MarketNewsEntryRepository extends PagingAndSortingRepository<MarketNewsEntry, Long>, CrudRepository<MarketNewsEntry, Long> {
}
