package com.bluebell.radicle.repositories.trade;

import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.trade.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data-access layer for {@link Trade} entities
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Repository
public interface TradeRepository extends PagingAndSortingRepository<Trade, Long>, CrudRepository<Trade, Long> {

    /**
     * Returns a {@link List} of {@link Trade}s for the given {@link TradeType}
     *
     * @param tradeType {@link TradeType}
     * @param account   {@link Account}
     * @return {@link List} of {@link Trade}s
     */
    List<Trade> findAllByTradeTypeAndAccountOrderByTradeOpenTimeAsc(final TradeType tradeType, final Account account);

    /**
     * Returns a {@link List} of {@link Trade}s that are within the given time span
     *
     * @param start   {@link LocalDateTime} start of interval (inclusive)
     * @param end     {@link LocalDateTime} end of interval (exclusive)
     * @param account {@link Account}
     * @return {@link List} of {@link Trade}s
     */
    @Query("SELECT t FROM Trade t WHERE t.tradeOpenTime >= ?1 AND t.tradeOpenTime < ?2 AND t.account = ?3 ORDER BY t.tradeOpenTime ASC")
    List<Trade> findAllTradesWithinDate(final LocalDateTime start, final LocalDateTime end, final Account account);

    /**
     * Returns a paginated {@link List} of {@link Trade}s that are within the given time span
     *
     * @param start    {@link LocalDateTime} start of interval (inclusive)
     * @param end      {@link LocalDateTime} end of interval (exclusive)
     * @param account  {@link Account}
     * @param pageable {@link Pageable}
     * @return {@link Page} of {@link Trade}s
     */
    @Query("SELECT t FROM Trade t WHERE t.tradeOpenTime >= ?1 AND t.tradeOpenTime < ?2 AND t.account = ?3 ORDER BY t.tradeOpenTime ASC")
    Page<Trade> findAllTradesWithinDatePaged(final LocalDateTime start, final LocalDateTime end, final Account account, final Pageable pageable);

    /**
     * Returns a {@link Trade} for the given tradeId
     *
     * @param tradeId trade id
     * @param account {@link Account
     * @return {@link Trade}
     */
    Trade findTradeByTradeIdAndAccount(final String tradeId, final Account account);

    /**
     * Returns a {@link List} of {@link Trade} for the given account
     *
     * @param account {@link Account}
     * @return {@link List} of {@link Trade}
     */
    List<Trade> findAllByAccount(final Account account);
}
