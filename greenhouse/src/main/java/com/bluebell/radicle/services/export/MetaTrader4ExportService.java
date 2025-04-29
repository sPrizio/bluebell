package com.bluebell.radicle.services.export;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.util.DirectoryUtil;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.services.market.MarketPriceService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.bluebell.radicle.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Exports data for consumption in MetaTrader4
 *
 * @author Stephen Prizio
 * @version 0.1.8
 */
@Slf4j
@Service
public class MetaTrader4ExportService {

    @Resource(name = "marketPriceService")
    private MarketPriceService marketPriceService;


    //  METHODS

    /**
     * Exports {@link MarketPrice}s for consumption within MT4
     *
     * @param symbol       symbol
     * @param timeInterval {@link MarketPriceTimeInterval}
     * @param start        {@link LocalDateTime}
     * @param end          {@link LocalDateTime}
     * @param dataSource   {@link DataSource}
     */
    public void exportMarketPrices(final String symbol, final MarketPriceTimeInterval timeInterval, final LocalDateTime start, final LocalDateTime end, final DataSource dataSource) {

        validateParameterIsNotNull(timeInterval, CorePlatformConstants.Validation.MarketPrice.MARKET_PRICE_TIME_INTERVAL_CANNOT_BE_NULL);
        validateParameterIsNotNull(symbol, CorePlatformConstants.Validation.MarketPrice.SYMBOL_CANNOT_BE_NULL);
        validateParameterIsNotNull(start, CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start, end, CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        validateParameterIsNotNull(dataSource, CorePlatformConstants.Validation.MarketPrice.DATA_SOURCE_CANNOT_BE_NULL);

        final List<MarketPrice> marketPrices = this.marketPriceService.findMarketPricesWithinTimespan(symbol, timeInterval, start, end, dataSource);
        if (CollectionUtils.isEmpty(marketPrices)) {
            LOGGER.info("Could not find any market data for time span {} : {} for symbol {}, interval {} and data source {}", start, end, symbol, timeInterval.getCode(), dataSource.getCode());
            return;
        }

        final String fileName = String.format("%s_%s_%s_%s_%s.csv", symbol, timeInterval.getCode(), start.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), end.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), dataSource.getCode());
        final File outputFile = new File(String.format("%s%s%s", DirectoryUtil.getOutputDirectory(), File.separator, fileName));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (final MarketPrice marketPrice : marketPrices) {
                final LocalDateTime converted = convertLocalDateTimeToMT4Timezone(marketPrice.getDate());
                writer.write(
                        String.format(
                                "%s,%s,%.2f,%.2f,%.2f,%.2f,%d",
                                converted.toLocalDate().format(DateTimeFormatter.ofPattern(CorePlatformConstants.MT4_DATE_FORMAT)),
                                converted.toLocalTime().format(DateTimeFormatter.ofPattern(CorePlatformConstants.MT4_TIME_FORMAT)),
                                marketPrice.getOpen(),
                                marketPrice.getHigh(),
                                marketPrice.getLow(),
                                marketPrice.getClose(),
                                marketPrice.getVolume()
                        )
                );
                writer.newLine();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


    //  HELPERS

    /**
     * Converts the local date time to the mt4 timezone
     *
     * @param toConvert {@link LocalDateTime}
     * @return converted {@link LocalDateTime}
     */
    private LocalDateTime convertLocalDateTimeToMT4Timezone(final LocalDateTime toConvert) {
        ZoneId zone = ZoneId.of(CorePlatformConstants.METATRADER4_TIMEZONE);
        ZonedDateTime zoned = toConvert.atZone(ZoneId.of(CorePlatformConstants.EASTERN_TIMEZONE)).withZoneSameInstant(zone);

        return zoned.toLocalDateTime();
    }
}
