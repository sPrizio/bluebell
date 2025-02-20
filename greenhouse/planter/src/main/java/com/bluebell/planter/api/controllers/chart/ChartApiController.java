package com.bluebell.planter.api.controllers.chart;

import com.bluebell.planter.api.controllers.AbstractApiController;
import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.chart.IntradayInterval;
import com.bluebell.planter.core.services.chart.ChartService;
import com.bluebell.planter.security.aspects.ValidateApiToken;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.nonentities.apexcharts.ApexChartCandleStick;
import com.bluebell.processing.api.examples.AccountApiExamples;
import com.bluebell.processing.api.examples.ApiErrorExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.bluebell.planter.core.validation.GenericValidator.validateLocalDateFormat;

/**
 * API controller for providing charting capabilities based on historical data
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/chart")
@Tag(name = "Charting", description = "Handles endpoints & operations related to charting data points.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class ChartApiController extends AbstractApiController {

    @Resource(name = "apexChartService")
    private ChartService<ApexChartCandleStick> chartService;


    //  ----------------- GET REQUESTS -----------------

    /**
     * Generates a {@link List} of {@link ApexChartCandleStick}s for the given start and end interval
     *
     * @param start start of time period
     * @param end end of time period
     * @param interval interval of time
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains the candlesticks for use with ApexCharts", description = "Returns a list of candlesticks in a format expected by ApexCharts.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid start date.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = ApiErrorExamples.BAD_START_INTERVAL_EXAMPLE)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid end date.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = ApiErrorExamples.BAD_END_INTERVAL_EXAMPLE)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the trade duration analysis.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = AccountApiExamples.APEX_EXAMPLE)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Response when the api call made was unauthorized.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = ApiErrorExamples.UNAUTHORIZED_REQUEST)
            )
    )
    @GetMapping("/apex-data")
    public StandardJsonResponse<List<ApexChartCandleStick>> getApexChartData(final @RequestParam("start") String start, final @RequestParam("end") String end, final @RequestParam("interval") String interval, final HttpServletRequest request) {

        validateLocalDateFormat(start, CoreConstants.DATE_FORMAT, String.format(CoreConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, start, CoreConstants.DATE_FORMAT));
        validateLocalDateFormat(end, CoreConstants.DATE_FORMAT, String.format(CoreConstants.Validation.DateTime.END_DATE_INVALID_FORMAT, end, CoreConstants.DATE_FORMAT));

        final IntradayInterval intradayInterval = IntradayInterval.getByLabel(interval);
        if (intradayInterval == IntradayInterval.ONE_DAY) {
            return new StandardJsonResponse<>(true, this.chartService.getChartData(LocalDate.parse(start, DateTimeFormatter.ISO_DATE).minusMonths(1), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), intradayInterval), StringUtils.EMPTY);
        } else if (intradayInterval == IntradayInterval.ONE_HOUR) {
            return new StandardJsonResponse<>(true, this.chartService.getChartData(LocalDate.parse(start, DateTimeFormatter.ISO_DATE).minusDays(5), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), intradayInterval), StringUtils.EMPTY);
        }

        return new StandardJsonResponse<>(true, this.chartService.getChartData(LocalDate.parse(start, DateTimeFormatter.ISO_DATE), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), intradayInterval), StringUtils.EMPTY);
    }
}
