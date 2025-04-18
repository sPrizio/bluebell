package com.bluebell.radicle.repositories.trade;

import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.trade.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data-access layer for {@link Trade} entities
 *
 * @author Stephen Prizio
 * @version 0.1.6
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

    /**
     * Inserts or updates an existing {@link Trade}
     *
     * @param tradeId trade id
     * @param product trade symbol
     * @param tradePlatform {@link TradePlatform}
     * @param tradeType {@link TradeType}
     * @param tradeOpenTime trade open time
     * @param tradeCloseTime trade close time
     * @param lotSize lot size
     * @param openPrice trade open price
     * @param closePrice trade close price
     * @param netProfit net profit
     * @param stopLoss stop loss price
     * @param takeProfit take profit price
     * @param accountId {@link Account} id
     * @return number of entries inserted/updated
     */
    @Modifying
    @Transactional
    @Query(value = """
                INSERT INTO trades (
                    trade_id,
                    product,
                    trade_platform,
                    trade_type,
                    trade_open_time,
                    trade_close_time,
                    lot_size,
                    open_price,
                    close_price,
                    net_profit,
                    stop_loss,
                    take_profit,
                    account_id
                )
                VALUES (
                    :tradeId,
                    :product,
                    :tradePlatform,
                    :tradeType,
                    :tradeOpenTime,
                    :tradeCloseTime,
                    :lotSize,
                    :openPrice,
                    :closePrice,
                    :netProfit,
                    :stopLoss,
                    :takeProfit,
                    :accountId
                )
                ON DUPLICATE KEY UPDATE
                    product = :product,
                    trade_platform = :tradePlatform,
                    trade_type = :tradeType,
                    trade_open_time = :tradeOpenTime,
                    trade_close_time = :tradeCloseTime,
                    lot_size = :lotSize,
                    open_price = :openPrice,
                    close_price = :closePrice,
                    net_profit = :netProfit,
                    stop_loss = :stopLoss,
                    take_profit = :takeProfit
            """, nativeQuery = true)
    int upsertTrade(
            @Param("tradeId") String tradeId,
            @Param("product") String product,
            @Param("tradePlatform") TradePlatform tradePlatform,
            @Param("tradeType") TradeType tradeType,
            @Param("tradeOpenTime") LocalDateTime tradeOpenTime,
            @Param("tradeCloseTime") LocalDateTime tradeCloseTime,
            @Param("lotSize") double lotSize,
            @Param("openPrice") double openPrice,
            @Param("closePrice") double closePrice,
            @Param("netProfit") double netProfit,
            @Param("stopLoss") double stopLoss,
            @Param("takeProfit") double takeProfit,
            @Param("accountId") Long accountId
    );
}
