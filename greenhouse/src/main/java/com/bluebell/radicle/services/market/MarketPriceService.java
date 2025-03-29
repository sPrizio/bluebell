package com.bluebell.radicle.services.market;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import com.bluebell.radicle.repositories.market.MarketPriceRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link MarketPrice}
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@Slf4j
@Service
public class MarketPriceService {

    @Resource(name = "marketPriceRepository")
    private MarketPriceRepository marketPriceRepository;


    //  METHODS

    /**
     * Saves all {@link MarketPrice}s within the given {@link AggregatedMarketPrices} to the database
     *
     * @param aggregatedMarketPrices {@link AggregatedMarketPrices}
     * @return sorted {@link Set} of {@link MarketPrice}s
     */
    @Transactional
    public Set<MarketPrice> saveAll(final AggregatedMarketPrices aggregatedMarketPrices) {

        validateParameterIsNotNull(aggregatedMarketPrices, CorePlatformConstants.Validation.MarketPrice.AGGREGATED_PRICES_CANNOT_BE_NULL);
        validateParameterIsNotNull(aggregatedMarketPrices.dataSource(), CorePlatformConstants.Validation.MarketPrice.DATA_SOURCE_CANNOT_BE_NULL);
        validateParameterIsNotNull(aggregatedMarketPrices.interval(), CorePlatformConstants.Validation.MarketPrice.MARKET_PRICE_TIME_INTERVAL_CANNOT_BE_NULL);

        if (CollectionUtils.isEmpty(aggregatedMarketPrices.marketPrices())) {
            LOGGER.warn("No market prices to save for source {} and time interval {}", aggregatedMarketPrices.dataSource().getLabel(), aggregatedMarketPrices.interval().getLabel());
            return Collections.emptySet();
        }

        final Set<MarketPrice> savedPrices = new TreeSet<>();
        aggregatedMarketPrices.marketPrices().forEach(marketPrice -> savedPrices.add(this.marketPriceRepository.save(marketPrice)));
        return savedPrices;
    }

    /**
     * Saves all {@link MarketPrice}s within the given set to the database
     *
     * @param marketPrices {@link AggregatedMarketPrices}
     * @return sorted {@link Set} of {@link MarketPrice}s
     */
    @Transactional
    public Set<MarketPrice> saveAllSet(final Set<MarketPrice> marketPrices) {

        if (CollectionUtils.isEmpty(marketPrices)) {
            return Collections.emptySet();
        }

        final Set<MarketPrice> savedPrices = new TreeSet<>();
        marketPrices.forEach(marketPrice -> savedPrices.add(this.marketPriceRepository.save(marketPrice)));
        return savedPrices;
    }
}
