package com.bluebell.radicle.repositories.market;

import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.radicle.enums.DataSource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Data-access layer for {@link MarketPrice}
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Repository
public interface MarketPriceRepository extends PagingAndSortingRepository<MarketPrice, Long>, CrudRepository<MarketPrice, Long> {

    /**
     * Inserts or updates an existing {@link MarketPrice}
     *
     * @param date date and time
     * @param interval {@link MarketPriceTimeInterval}
     * @param symbol symbol
     * @param open open price of period
     * @param high high price of period
     * @param low low price of period
     * @param close close price of period
     * @param volume volume of period
     * @param dataSource {@link DataSource}
     * @return number of entities inserted/updated
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO market_prices (price_date, market_price_time_interval, symbol, open, high, low, close, volume, data_source) " +
            "VALUES (:date, :interval, :symbol, :open, :high, :low, :close, :volume, :dataSource) " +
            "ON DUPLICATE KEY UPDATE " +
            "open = :open, " +
            "high = :high, " +
            "low = :low, " +
            "close = :close, " +
            "volume = :volume, " +
            "price_date = price_date",
            nativeQuery = true
    )
    int upsertMarketPrice(
            final @Param("date") LocalDateTime date,
            final @Param("interval") MarketPriceTimeInterval interval,
            final @Param("symbol") String symbol,
            final @Param("open") double open,
            final @Param("high") double high,
            final @Param("low") double low,
            final @Param("close") double close,
            final @Param("volume") long volume,
            final @Param("dataSource") DataSource dataSource
    );
}
