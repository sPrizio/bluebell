package com.bluebell.radicle.parsers.impl;

import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.exceptions.parsing.FirstRateDataParsingException;
import com.bluebell.radicle.parsers.MarketPriceParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Parses data from FirstData from data files
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
@Slf4j
public class FirstRateDataParser extends AbstractDataParser implements MarketPriceParser {

    private final boolean isTest;

    public FirstRateDataParser() {
        this.isTest = false;
    }

    public FirstRateDataParser(final boolean isTest) {
        this.isTest = isTest;
    }


    //  METHODS

    @Override
    public AggregatedMarketPrices parseMarketPrices(final String file, final MarketPriceTimeInterval interval) {

        final String sampleFile = getDataRoot() + "/" + file;
        if (!validateFile(sampleFile)) {
            LOGGER.error("File {} was not found!\n", file);
            return AggregatedMarketPrices.builder().marketPrices(new TreeSet<>()).interval(interval).dataSource(DataSource.FIRST_RATE_DATA).build();
        }

        final DateTimeFormatter dateTimeFormatter;
        if (interval == MarketPriceTimeInterval.ONE_DAY) {
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        } else {
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        }

        final TreeSet<MarketPrice> marketPrices = new TreeSet<>();
        try (LineIterator lineIterator = FileUtils.lineIterator(new File(sampleFile))) {
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if (line.startsWith("timestamp")) {
                    line = lineIterator.nextLine();
                }

                final String[] lineComponents = line.split(",");
                marketPrices.add(
                        MarketPrice
                                .builder()
                                .date(interval ==
                                        MarketPriceTimeInterval.ONE_DAY ? LocalDate.parse(lineComponents[0], dateTimeFormatter).atStartOfDay()
                                        : LocalDateTime.parse(lineComponents[0], dateTimeFormatter))
                                .interval(interval)
                                .open(parseDoubleFromString(lineComponents[1]))
                                .high(parseDoubleFromString(lineComponents[2]))
                                .low(parseDoubleFromString(lineComponents[3]))
                                .close(parseDoubleFromString(lineComponents[4]))
                                .build()
                );
            }
        } catch (Exception e) {
            throw new FirstRateDataParsingException(String.format("An error occurred while parsing the file. Error: %s", e.getMessage()), e);
        }

        return AggregatedMarketPrices.builder().marketPrices(marketPrices).interval(interval).dataSource(DataSource.FIRST_RATE_DATA).build();
    }

    @Override
    public Map<LocalDate, AggregatedMarketPrices> parseMarketPricesByDate(final MarketPriceTimeInterval interval) {

        final AggregatedMarketPrices marketPrices;
        switch (interval) {
            case ONE_MINUTE -> marketPrices = parseMarketPrices(computeFileName(MarketPriceTimeInterval.ONE_MINUTE), MarketPriceTimeInterval.ONE_MINUTE);
            case FIVE_MINUTE -> marketPrices = parseMarketPrices(computeFileName(MarketPriceTimeInterval.FIVE_MINUTE), MarketPriceTimeInterval.FIVE_MINUTE);
            case TEN_MINUTE -> marketPrices = parseDynamicMarketPrices(MarketPriceTimeInterval.TEN_MINUTE);
            case FIFTEEN_MINUTE -> marketPrices = parseDynamicMarketPrices(MarketPriceTimeInterval.FIFTEEN_MINUTE);
            case THIRTY_MINUTE -> marketPrices = parseMarketPrices(computeFileName(MarketPriceTimeInterval.THIRTY_MINUTE), MarketPriceTimeInterval.THIRTY_MINUTE);
            case ONE_HOUR -> marketPrices = parseMarketPrices(computeFileName(MarketPriceTimeInterval.ONE_HOUR), MarketPriceTimeInterval.ONE_HOUR);
            case ONE_DAY -> marketPrices = parseMarketPrices(computeFileName(MarketPriceTimeInterval.ONE_DAY), MarketPriceTimeInterval.ONE_DAY);
            default -> marketPrices = AggregatedMarketPrices.builder().marketPrices(new TreeSet<>()).interval(interval).dataSource(DataSource.FIRST_RATE_DATA).build();
        }

        return generateMasterCollection(marketPrices, interval);
    }


    //  HELPERS

    /**
     * Computes the filename depending on whether this parser was instantiated as a testing instance
     *
     * @param interval {@link MarketPriceTimeInterval}
     * @return filename
     */
    private String computeFileName(final MarketPriceTimeInterval interval) {

        String prefix;
        final String format = this.isTest ? "NDX_%s_sample.csv" : "NDX_full_%s.txt";

        switch (interval) {
            case ONE_MINUTE -> prefix = "1min";
            case FIVE_MINUTE -> prefix = "5min";
            case THIRTY_MINUTE -> prefix = "30min";
            case ONE_HOUR -> prefix = "1hour";
            case ONE_DAY -> prefix = "1day";
            default -> throw new FirstRateDataParsingException(String.format("%s is currently not a supported interval", interval));
        }

        return String.format(format, prefix);
    }

    /**
     * Computes the market prices for an interval that doesn't directly map to a file
     *
     * @param interval {@link MarketPriceTimeInterval}
     * @return {@link AggregatedMarketPrices}
     */
    private AggregatedMarketPrices parseDynamicMarketPrices(final MarketPriceTimeInterval interval) {

        final AggregatedMarketPrices smallerPrices = parseMarketPrices(computeFileName(MarketPriceTimeInterval.ONE_MINUTE), MarketPriceTimeInterval.ONE_MINUTE);
        if (CollectionUtils.isEmpty(smallerPrices.marketPrices())) {
            return AggregatedMarketPrices.builder().marketPrices(Collections.emptySortedSet()).interval(interval).dataSource(DataSource.FIRST_RATE_DATA).build();
        }

        final Map<LocalDateTime, MarketPrice> collection = new HashMap<>();
        smallerPrices.marketPrices().forEach(sp -> collection.put(sp.getDate(), sp));

        final LocalDateTime start = smallerPrices.marketPrices().first().getDate();
        final LocalDateTime end = smallerPrices.marketPrices().last().getDate();
        LocalDateTime compare = start;

        final SortedSet<MarketPrice> computed = new TreeSet<>();
        while (compare.isBefore(end) || compare.isEqual(end)) {
            LocalDateTime localCompareStart = compare;
            final LocalDateTime localCompareEnd = compare.plus(interval.getAmount(), interval.getUnit());
            final List<MarketPrice> localPrices = new ArrayList<>();

            while (localCompareStart.isBefore(localCompareEnd)) {
                if (collection.containsKey(localCompareStart)) {
                    localPrices.add(collection.get(localCompareStart));
                }

                localCompareStart = localCompareStart.plusMinutes(1);
            }

            if (CollectionUtils.isNotEmpty(localPrices)) {
                computed.add(
                        MarketPrice
                                .builder()
                                .date(compare)
                                .interval(interval)
                                .open(CollectionUtils.isNotEmpty(localPrices) ? localPrices.get(0).getOpen() : 0.0)
                                .high(new ArrayList<>(localPrices).stream().filter(Objects::nonNull).mapToDouble(MarketPrice::getHigh).max().orElse(0.0))
                                .low(new ArrayList<>(localPrices).stream().filter(Objects::nonNull).mapToDouble(MarketPrice::getLow).min().orElse(0.0))
                                .close(CollectionUtils.isNotEmpty(localPrices) ? localPrices.get(localPrices.size() - 1).getClose() : 0.0)
                                .build()
                );
            }

            compare = localCompareEnd;
        }

        return AggregatedMarketPrices.builder().marketPrices(new TreeSet<>(computed.stream().filter(MarketPrice::isNotEmpty).toList())).interval(interval).dataSource(DataSource.FIRST_RATE_DATA).build();
    }

    /**
     * Returns the root folder for sample data
     *
     * @return sample data path
     */
    private String getDataRoot() {

        final String root = Objects.requireNonNull(getClass().getClassLoader().getResource("firstratedata")).getFile();
        if (this.isTest && !root.contains("test-classes")) {
            return root.replace("classes", "test-classes");
        }

        return root;
    }
}
