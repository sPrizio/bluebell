package com.bluebell.radicle.parsers;

import com.bluebell.radicle.enums.TimeInterval;
import com.bluebell.radicle.models.MarketPrice;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

/**
 * Parses a {@link File} url into a {@link List} of {@link MarketPrice}
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public interface MarketPriceParser {


    //  METHODS

    /**
     * Parses a file of market prices into a {@link TreeSet} of {@link MarketPrice}
     *
     * @param file file path
     * @param interval {@link TimeInterval}
     * @return {@link List} of {@link MarketPrice}
     */
    TreeSet<MarketPrice> parseMarketPrices(final String file, final TimeInterval interval);

    /**
     * Parses a file of market prices into a {@link Map} of {@link MarketPrice} organized by their date (truncates the time)
     *
     * @param file file path
     * @param interval {@link TimeInterval}
     * @return {@link List} of {@link MarketPrice}
     */
    Map<LocalDate, TreeSet<MarketPrice>> parseMarketPricesByDate(final String file, final TimeInterval interval);


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
     * Returns the root folder for sample data
     *
     * @return sample data path
     */
    default String getDataRoot() {
        return Objects.requireNonNull(getClass().getClassLoader().getResource("firstratedata")).getFile();
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
