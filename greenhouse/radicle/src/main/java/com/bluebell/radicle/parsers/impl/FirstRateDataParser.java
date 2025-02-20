package com.bluebell.radicle.parsers.impl;

import com.bluebell.platform.enums.time.PlatformTimeInterval;
import com.bluebell.platform.models.core.nonentities.market.AggregatedMarketPrices;
import com.bluebell.platform.models.core.nonentities.market.MarketPrice;
import com.bluebell.radicle.exceptions.FirstRateDataParsingException;
import com.bluebell.radicle.parsers.MarketPriceParser;
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
 * @version 0.0.9
 */
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
    public AggregatedMarketPrices parseMarketPrices(final String file, final PlatformTimeInterval interval) {

        final String sampleFile = getDataRoot() + "/" + file;
        if (!validateFile(sampleFile)) {
            System.out.printf("File %s was not found!%n", file);
            return new AggregatedMarketPrices(new TreeSet<>(), interval);
        }

        final DateTimeFormatter dateTimeFormatter;
        if (interval == PlatformTimeInterval.ONE_DAY) {
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
                marketPrices.add(new MarketPrice(
                        interval ==
                                PlatformTimeInterval.ONE_DAY ? LocalDate.parse(lineComponents[0], dateTimeFormatter).atStartOfDay()
                                : LocalDateTime.parse(lineComponents[0], dateTimeFormatter),
                        interval,
                        parseDoubleFromString(lineComponents[1]),
                        parseDoubleFromString(lineComponents[2]),
                        parseDoubleFromString(lineComponents[3]),
                        parseDoubleFromString(lineComponents[4])
                ));
            }
        } catch (Exception e) {
            throw new FirstRateDataParsingException(String.format("An error occurred while parsing the file. Error: %s", e.getMessage()), e);
        }

        return new AggregatedMarketPrices(marketPrices, interval);
    }

    @Override
    public Map<LocalDate, AggregatedMarketPrices> parseMarketPricesByDate(final PlatformTimeInterval interval) {

        final AggregatedMarketPrices marketPrices;
        switch (interval) {
            case ONE_MINUTE -> marketPrices = parseMarketPrices(computeFileName(PlatformTimeInterval.ONE_MINUTE), PlatformTimeInterval.ONE_MINUTE);
            case FIVE_MINUTE -> marketPrices = parseMarketPrices(computeFileName(PlatformTimeInterval.FIVE_MINUTE), PlatformTimeInterval.FIVE_MINUTE);
            case TEN_MINUTE -> marketPrices = parseDynamicMarketPrices(PlatformTimeInterval.TEN_MINUTE);
            case FIFTEEN_MINUTE -> marketPrices = parseDynamicMarketPrices(PlatformTimeInterval.FIFTEEN_MINUTE);
            case THIRTY_MINUTE -> marketPrices = parseMarketPrices(computeFileName(PlatformTimeInterval.THIRTY_MINUTE), PlatformTimeInterval.THIRTY_MINUTE);
            case ONE_HOUR -> marketPrices = parseMarketPrices(computeFileName(PlatformTimeInterval.ONE_HOUR), PlatformTimeInterval.ONE_HOUR);
            case ONE_DAY -> marketPrices = parseMarketPrices(computeFileName(PlatformTimeInterval.ONE_DAY), PlatformTimeInterval.ONE_DAY);
            default -> marketPrices = new AggregatedMarketPrices(new TreeSet<>(), interval);
        }

        return generateMasterCollection(marketPrices, interval);
    }


    //  HELPERS

    /**
     * Computes the filename depending on whether this parser was instantiated as a testing instance
     *
     * @param interval {@link PlatformTimeInterval}
     * @return filename
     */
    private String computeFileName(final PlatformTimeInterval interval) {

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
     * @param interval {@link PlatformTimeInterval}
     * @return {@link AggregatedMarketPrices}
     */
    private AggregatedMarketPrices parseDynamicMarketPrices(final PlatformTimeInterval interval) {

        final AggregatedMarketPrices smallerPrices = parseMarketPrices(computeFileName(PlatformTimeInterval.ONE_MINUTE), PlatformTimeInterval.ONE_MINUTE);
        if (CollectionUtils.isEmpty(smallerPrices.marketPrices())) {
            return new AggregatedMarketPrices(Collections.emptySortedSet(), interval);
        }

        final Map<LocalDateTime, MarketPrice> collection = new HashMap<>();
        smallerPrices.marketPrices().forEach(sp -> collection.put(sp.date(), sp));

        final LocalDateTime start = smallerPrices.marketPrices().first().date();
        final LocalDateTime end = smallerPrices.marketPrices().last().date();
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
                        new MarketPrice(
                                compare,
                                interval,
                                CollectionUtils.isNotEmpty(localPrices) ? localPrices.get(0).open() : 0.0,
                                new ArrayList<>(localPrices).stream().filter(Objects::nonNull).mapToDouble(MarketPrice::high).max().orElse(0.0),
                                new ArrayList<>(localPrices).stream().filter(Objects::nonNull).mapToDouble(MarketPrice::low).min().orElse(0.0),
                                CollectionUtils.isNotEmpty(localPrices) ? localPrices.get(localPrices.size() - 1).close() : 0.0
                        )
                );
            }

            compare = localCompareEnd;
        }

        return new AggregatedMarketPrices(new TreeSet<>(computed.stream().filter(MarketPrice::isNotEmpty).toList()), interval);
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
