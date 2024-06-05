package com.bluebell.radicle.parsers.impl;

import com.bluebell.radicle.enums.RadicleTimeInterval;
import com.bluebell.radicle.models.MarketPrice;
import com.bluebell.radicle.parsers.MarketPriceParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Parses data from FirstData
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public class FirstRateDataParser implements MarketPriceParser {


    //  METHODS

    @Override
    public TreeSet<MarketPrice> parseMarketPrices(final String file, final RadicleTimeInterval interval) {

        final String sampleFile = getDataRoot() + "/" + file;
        if (!validateFile(sampleFile)) {
            System.out.printf("File %s was not found!%n", file);
            return new TreeSet<>();
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
            System.out.printf(e.getMessage(), e);
        }

        return marketPrices;
    }

    @Override
    public Map<LocalDate, TreeSet<MarketPrice>> parseMarketPricesByDate(final String file, final RadicleTimeInterval interval) {

        final TreeSet<MarketPrice> marketPrices = parseMarketPrices(file, interval);
        final Map<LocalDate, TreeSet<MarketPrice>> masterCollection = new HashMap<>();
        marketPrices.forEach(marketPrice -> {
            final TreeSet<MarketPrice> mapPrices;
            if (masterCollection.containsKey(marketPrice.date().toLocalDate())) {
                mapPrices = masterCollection.get(marketPrice.date().toLocalDate());
            } else {
                mapPrices = new TreeSet<>();
            }

            mapPrices.add(marketPrice);
            masterCollection.put(marketPrice.date().toLocalDate(), mapPrices);
        });

        return masterCollection;
    }
}
