package com.bluebell.aurora.models.metadata;

import com.bluebell.aurora.models.price.MarketPrice;
import com.bluebell.aurora.services.MathService;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.javatuples.Triplet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.SortedSet;

/**
 * Class representation of metadata about trading prices
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
public class MetaData {

    private final MathService mathService = new MathService();

    private final LocalDate start;

    private final LocalDate end;

    private final double absoluteHigh;

    private final double absoluteLow;

    private final double averageChange;


    //  CONSTRUCTORS

    public MetaData(final LocalDate start, final LocalDate end, final SortedSet<MarketPrice> prices) {
        this.start = start;
        this.end = end;

        final Triplet<Double, Double, Double> values = calculate(prices);
        this.absoluteHigh = values.getValue0();
        this.absoluteLow = values.getValue1();
        this.averageChange = values.getValue2();
    }


    //  METHODS

    public Triplet<Double, Double, Double> calculate(final SortedSet<MarketPrice> prices) {

        if (CollectionUtils.isEmpty(prices)) {
            return Triplet.with(0.0, 0.0, 0.0);
        }

        double absoluteTop = 0.0;
        double absoluteBottom = 123456789;
        final double average = this.mathService.getDouble(prices.stream().mapToDouble(pr -> Math.abs(this.mathService.subtract(pr.high(), pr.low()))).average().orElse(0.0));

        for (MarketPrice price : prices) {
            if (price.high() > absoluteTop) {
                absoluteTop = price.high();
            }

            if (price.low() < absoluteBottom) {
                absoluteBottom = price.low();
            }
        }

        return Triplet.with(absoluteTop, prices.isEmpty() ? 0.0 : absoluteBottom, average);
    }


    @Override
    public String toString() {
        return """
                MetaData {
                \tstart = %s
                \tend = %s
                \tabsoluteHigh = %s
                \tabsoluteLow = %s
                \tAbsolute Change = %s
                \taverageChangePerBar = %s
                }'
                """
                .formatted(
                        this.start.format(DateTimeFormatter.ISO_DATE),
                        this.end.format(DateTimeFormatter.ISO_DATE),
                        this.absoluteHigh,
                        this.absoluteLow,
                        this.mathService.subtract(this.absoluteHigh, this.absoluteLow),
                        this.averageChange
                );
    }
}
