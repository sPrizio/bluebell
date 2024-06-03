package com.bluebell.flowerpot.core.repositories.news;

import com.bluebell.flowerpot.core.models.entities.news.MarketNews;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Data-access layer for {@link MarketNews}
 *
 * @author Stephen Prizio
 * @version 0.0.4
 */
@Repository
public interface MarketNewsRepository extends PagingAndSortingRepository<MarketNews, Long>, CrudRepository<MarketNews, Long> {

    /**
     * Returns a {@link List} of {@link MarketNews} within the given start & end dates
     *
     * @param start {@link LocalDate} start
     * @param end   {@link LocalDate} ned
     * @return {@link List} of {@link MarketNews}
     */
    @Query("SELECT n FROM MarketNews n WHERE n.date >= ?1 AND n.date < ?2 ORDER BY n.date ASC")
    List<MarketNews> findNewsWithinInterval(final LocalDate start, final LocalDate end);

    /**
     * Returns a {@link MarketNews} by its date which is assumed to be unique
     *
     * @param date {@link LocalDate}
     * @return {@link MarketNews}
     */
    MarketNews findMarketNewsByDate(final LocalDate date);
}
