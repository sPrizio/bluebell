package com.bluebell.radicle.parsers.impl;

import com.bluebell.radicle.enums.RadicleTimeInterval;
import com.bluebell.radicle.exceptions.parser.FirstRateDataParsingException;
import com.bluebell.radicle.models.AggregatedMarketPrices;
import com.bluebell.radicle.models.MarketPrice;
import com.bluebell.radicle.parsers.MarketPriceParser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Parses data from FirstData from data files
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
public class FirstRateDataParser extends AbstractDataParser implements MarketPriceParser {

    private final boolean isTest;

    public FirstRateDataParser(final boolean isTest) {
        this.isTest = isTest;
    }


    //  METHODS

    @Override
    public AggregatedMarketPrices parseMarketPrices(final String file, final RadicleTimeInterval interval) {

        final String sampleFile = getDataRoot() + "/" + file;
        if (!validateFile(sampleFile)) {
            System.out.printf("File %s was not found!%n", file);
            return new AggregatedMarketPrices(new TreeSet<>(), interval);
        }

        final DateTimeFormatter dateTimeFormatter;
        if (interval == RadicleTimeInterval.ONE_DAY) {
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
                                RadicleTimeInterval.ONE_DAY ? LocalDate.parse(lineComponents[0], dateTimeFormatter).atStartOfDay()
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
    public Map<LocalDate, AggregatedMarketPrices> parseMarketPricesByDate(final RadicleTimeInterval interval) {

        final AggregatedMarketPrices marketPrices;
        switch (interval) {
            case ONE_MINUTE -> marketPrices = parseMarketPrices(computeFileName(RadicleTimeInterval.ONE_MINUTE), RadicleTimeInterval.ONE_MINUTE);
            case FIVE_MINUTE -> marketPrices = parseMarketPrices(computeFileName(RadicleTimeInterval.FIVE_MINUTE), RadicleTimeInterval.FIVE_MINUTE);
            case TEN_MINUTE -> marketPrices = parseDynamicMarketPrices(RadicleTimeInterval.TEN_MINUTE);
            case FIFTEEN_MINUTE -> marketPrices = parseDynamicMarketPrices(RadicleTimeInterval.FIFTEEN_MINUTE);
            case THIRTY_MINUTE -> marketPrices = parseMarketPrices(computeFileName(RadicleTimeInterval.THIRTY_MINUTE), RadicleTimeInterval.THIRTY_MINUTE);
            case ONE_HOUR -> marketPrices = parseMarketPrices(computeFileName(RadicleTimeInterval.ONE_HOUR), RadicleTimeInterval.ONE_HOUR);
            case ONE_DAY -> marketPrices = parseMarketPrices(computeFileName(RadicleTimeInterval.ONE_DAY), RadicleTimeInterval.ONE_DAY);
            default -> marketPrices = new AggregatedMarketPrices(new TreeSet<>(), interval);
        }

        return generateMasterCollection(marketPrices, interval);
    }


    //  HELPERS

    /**
     * Computes the filename depending on whether this parser was instantiated as a testing instance
     *
     * @param interval {@link RadicleTimeInterval}
     * @return filename
     */
    private String computeFileName(final RadicleTimeInterval interval) {

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
     * @param interval {@link RadicleTimeInterval}
     * @return {@link AggregatedMarketPrices}
     */
    private AggregatedMarketPrices parseDynamicMarketPrices(final RadicleTimeInterval interval) {

        final AggregatedMarketPrices smallerPrices = parseMarketPrices(computeFileName(RadicleTimeInterval.ONE_MINUTE), RadicleTimeInterval.ONE_MINUTE);
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
                                localPrices.getFirst() != null ? localPrices.getFirst().open() : 0.0,
                                new ArrayList<>(localPrices).stream().filter(Objects::nonNull).mapToDouble(MarketPrice::high).max().orElse(0.0),
                                new ArrayList<>(localPrices).stream().filter(Objects::nonNull).mapToDouble(MarketPrice::low).min().orElse(0.0),
                                localPrices.getLast() != null ? localPrices.getLast().close() : 0.0
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
