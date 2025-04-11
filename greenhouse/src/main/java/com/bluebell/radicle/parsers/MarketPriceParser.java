package com.bluebell.radicle.parsers;

import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Parses a {@link File} url into a {@link List} of {@link MarketPrice}
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
public interface MarketPriceParser {


    //  METHODS

    /**
     * Parses a file of market prices into {@link AggregatedMarketPrices}
     *
     * @param file file path
     * @param interval {@link MarketPriceTimeInterval}
     * @return {@link AggregatedMarketPrices}
     */
    AggregatedMarketPrices parseMarketPrices(final String file, final MarketPriceTimeInterval interval);

    /**
     * Parses a file of market prices into a {@link Map} of {@link MarketPrice} organized by their date (truncates the time)
     *
     * @param interval {@link MarketPriceTimeInterval}
     * @return {@link List} of {@link MarketPrice}
     */
    Map<LocalDate, AggregatedMarketPrices> parseMarketPricesByDate(final MarketPriceTimeInterval interval);


    //  HELPERS

    /**
     * Validates that the given file path is a valid {@link File}
     *
     * @param file file path
     * @return true if valid
     */
    default boolean validateFile(final String file) {
        final File toValidate = new File(file);
        return toValidate.exists() && toValidate.canRead() && !toValidate.isDirectory();
    }

    /**
     * Safely parses a double from a string
     *
     * @param s string
     * @return double
     */
    default double parseDoubleFromString(final String s) {

        if (StringUtils.isEmpty(s)) {
            return -1.0;
        }

        return BigDecimal.valueOf(Double.parseDouble(s)).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }
}
