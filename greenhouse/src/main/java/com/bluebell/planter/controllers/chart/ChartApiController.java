package com.bluebell.planter.controllers.chart;

import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.nonentities.apexcharts.ApexChartCandleStick;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.services.chart.ChartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.bluebell.radicle.validation.GenericValidator.validateLocalDateFormat;


/**
 * API controller for providing charting capabilities based on historical data
 *
 * @author Stephen Prizio
 * @version 0.1.7
 */
@RestController
@RequestMapping("${bluebell.base.api.controller.endpoint}/chart")
@Tag(name = "Charting", description = "Handles endpoints & operations related to charting data points.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class ChartApiController extends AbstractApiController {

    @Resource(name = "apexChartService")
    private ChartService<ApexChartCandleStick> chartService;


    //  ----------------- GET REQUESTS -----------------

    /**
     * Generates a {@link List} of {@link ApexChartCandleStick}s for the given start and end interval
     *
     * @param start    start of time period
     * @param end      end of time period
     * @param interval interval of time
     * @param symbol symbol
     * @param dataSource {@link DataSource}
     * @param request  {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains the candlesticks for use with ApexCharts", description = "Returns a list of candlesticks in a format expected by ApexCharts.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid start date.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid Date")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid end date.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid Date")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the trade duration analysis.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Response when the api call made was unauthorized.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "The API token was invalid.")
            )
    )
    @GetMapping("/apex-data")
    public StandardJsonResponse<List<ApexChartCandleStick>> getApexChartData(
            @Parameter(name = "Start Date", description = "Start date of time period to analyze", example = "2025-01-01")
            final @RequestParam("start") String start,
            @Parameter(name = "End Date", description = "End date of time period to analyze", example = "2025-01-01")
            final @RequestParam("end") String end,
            @Parameter(name = "Intraday Interval", description = "Time interval to look at intra day", example = "ten-minute")
            final @RequestParam("interval") String interval,
            @Parameter(name = "Symbol", description = "Symbol to look at", example = "NDX")
            final @RequestParam("symbol") String symbol,
            @Parameter(name = "DataSource", description = "Which source to pull the market data from", example = "METATRADER4")
            final @RequestParam("dataSource") String dataSource,
            final HttpServletRequest request
    ) {
        validateLocalDateFormat(start, CorePlatformConstants.DATE_FORMAT, String.format(CorePlatformConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, start, CorePlatformConstants.DATE_FORMAT));
        validateLocalDateFormat(end, CorePlatformConstants.DATE_FORMAT, String.format(CorePlatformConstants.Validation.DateTime.END_DATE_INVALID_FORMAT, end, CorePlatformConstants.DATE_FORMAT));

        if (!validateSymbol(symbol)) {
            return StandardJsonResponse
                    .<List<ApexChartCandleStick>>builder()
                    .success(false)
                    .message(String.format("Invalid symbol: %s", symbol))
                    .build();
        }

        if (!EnumUtils.isValidEnumIgnoreCase(DataSource.class, dataSource)) {
            return StandardJsonResponse
                    .<List<ApexChartCandleStick>>builder()
                    .success(false)
                    .message(String.format("%s is not a valid data source", dataSource))
                    .build();
        }

        final MarketPriceTimeInterval marketPriceTimeInterval = GenericEnum.getByCode(MarketPriceTimeInterval.class, interval);
        if (marketPriceTimeInterval == MarketPriceTimeInterval.ONE_DAY) {
            return StandardJsonResponse
                    .<List<ApexChartCandleStick>>builder()
                    .success(true)
                    .data(this.chartService.getChartData(LocalDate.parse(start, DateTimeFormatter.ISO_DATE).minusMonths(1), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), marketPriceTimeInterval, symbol, GenericEnum.getByCode(DataSource.class, dataSource)))
                    .build();
        } else if (marketPriceTimeInterval == MarketPriceTimeInterval.ONE_HOUR) {
            return StandardJsonResponse
                    .<List<ApexChartCandleStick>>builder()
                    .success(true)
                    .data(this.chartService.getChartData(LocalDate.parse(start, DateTimeFormatter.ISO_DATE).minusDays(5), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), marketPriceTimeInterval, symbol, GenericEnum.getByCode(DataSource.class, dataSource)))
                    .build();
        }

        return StandardJsonResponse
                .<List<ApexChartCandleStick>>builder()
                .success(true)
                .data(this.chartService.getChartData(LocalDate.parse(start, DateTimeFormatter.ISO_DATE), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), marketPriceTimeInterval, symbol, GenericEnum.getByCode(DataSource.class, dataSource)))
                .build();
    }
}
