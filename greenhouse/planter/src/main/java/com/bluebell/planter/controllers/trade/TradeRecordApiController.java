package com.bluebell.planter.controllers.trade;

import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.system.TradeRecordTimeInterval;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.nonentities.records.tradelog.TradeLog;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecord;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecordReport;
import com.bluebell.platform.models.core.nonentities.records.traderecord.controls.TradeRecordControls;
import com.bluebell.processing.api.examples.AccountApiExamples;
import com.bluebell.processing.api.examples.ApiErrorExamples;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.security.constants.SecurityConstants;
import com.bluebell.radicle.services.trade.TradeRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Api controller for {@link TradeRecord}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/trade-record")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class TradeRecordApiController extends AbstractApiController {

    @Resource(name = "tradeRecordService")
    private TradeRecordService tradeRecordService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link List} of {@link TradeRecord}
     *
     * @param request       {@link HttpServletRequest}
     * @param accountNumber account number
     * @param start         start date
     * @param end           end date
     * @param interval      interval
     * @param count         limit
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains trade records for the given interval of time", description = "Returns a list of trade records filtered by their start date.")
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
            description = "Response when the api was given an invalid trade record time interval.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = ApiErrorExamples.BAD_TRADE_RECORD_TIME_INTERVAL_EXAMPLE)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the trade records within the given time interval.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = AccountApiExamples.TRADE_RECORDS_EXAMPLE)
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
    @GetMapping("/for-interval")
    public StandardJsonResponse<TradeRecordReport> getTradeRecordsWithinInterval(
            final HttpServletRequest request,
            final @RequestParam("accountNumber") long accountNumber,
            final @RequestParam("start") String start,
            final @RequestParam("end") String end,
            final @RequestParam("interval") String interval,
            final @RequestParam(value = "count", defaultValue = "" + CorePlatformConstants.DEFAULT_TRADE_RECORD_COLLECTION_SIZE, required = false) int count
    ) {

        validate(start, end);
        if (!EnumUtils.isValidEnumIgnoreCase(TradeRecordTimeInterval.class, interval)) {
            return new StandardJsonResponse<>(false, null, String.format(CorePlatformConstants.Validation.DataIntegrity.INVALID_INTERVAL, interval));
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final TradeRecordReport records = this.tradeRecordService.getTradeRecords(LocalDate.parse(start, DateTimeFormatter.ISO_DATE), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), getAccountForId(user, accountNumber), TradeRecordTimeInterval.getInterval(interval), count);
        return new StandardJsonResponse<>(true, records, StringUtils.EMPTY);
    }

    /**
     * Returns a {@link List} of the most recent {@link TradeRecord}s for the given account
     *
     * @param request       {@link HttpServletRequest}
     * @param accountNumber account number
     * @param interval      interval
     * @param count         limit
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains recent trade records", description = "Returns a list of trade records for recent dates.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api was given an invalid trade record time interval.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = ApiErrorExamples.BAD_TRADE_RECORD_TIME_INTERVAL_EXAMPLE)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the trade records within the given time interval.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = AccountApiExamples.TRADE_RECORDS_EXAMPLE)
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
    @GetMapping("/recent")
    public StandardJsonResponse<TradeRecordReport> getRecentTradeRecords(
            final HttpServletRequest request,
            final @RequestParam("accountNumber") long accountNumber,
            final @RequestParam("interval") String interval,
            final @RequestParam(value = "count", defaultValue = "" + CorePlatformConstants.DEFAULT_TRADE_RECORD_COLLECTION_SIZE, required = false) int count
    ) {

        if (!EnumUtils.isValidEnumIgnoreCase(TradeRecordTimeInterval.class, interval)) {
            return new StandardJsonResponse<>(false, null, String.format(CorePlatformConstants.Validation.DataIntegrity.INVALID_INTERVAL, interval));
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final TradeRecordReport records = this.tradeRecordService.getRecentTradeRecords(getAccountForId(user, accountNumber), TradeRecordTimeInterval.getInterval(interval), count);
        return new StandardJsonResponse<>(true, records, StringUtils.EMPTY);
    }

    /**
     * Returns {@link TradeRecordControls} for the account
     *
     * @param request       {@link HttpServletRequest}
     * @param accountNumber account number
     * @param interval      interval
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains filters for an account's trade records", description = "Returns a collection of filters that can be used to filter trade records for the given account.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api was given an invalid trade record time interval.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = ApiErrorExamples.BAD_TRADE_RECORD_TIME_INTERVAL_EXAMPLE)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the trade record filters.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = AccountApiExamples.TRADE_RECORD_CONTROLS_EXAMPLE)
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
    @GetMapping("/trade-record-controls")
    public StandardJsonResponse<TradeRecordControls> getRecentTradeRecords(
            final HttpServletRequest request,
            final @RequestParam("accountNumber") long accountNumber,
            final @RequestParam("interval") String interval
    ) {

        if (!EnumUtils.isValidEnumIgnoreCase(TradeRecordTimeInterval.class, interval)) {
            return new StandardJsonResponse<>(false, null, String.format(CorePlatformConstants.Validation.DataIntegrity.INVALID_INTERVAL, interval));
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final TradeRecordControls controls = this.tradeRecordService.getTradeRecordControls(getAccountForId(user, accountNumber), TradeRecordTimeInterval.getInterval(interval));
        return new StandardJsonResponse<>(true, controls, StringUtils.EMPTY);
    }

    /**
     * Returns a {@link TradeLog}
     *
     * @param request  {@link HttpServletRequest}
     * @param start    start date
     * @param end      end date
     * @param interval interval
     * @param count    limit
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains a trade log for the given time period", description = "Returns a trade log for the specified start and end period.")
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
            description = "Response when the api was given an invalid trade record time interval.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = ApiErrorExamples.BAD_TRADE_RECORD_TIME_INTERVAL_EXAMPLE)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the trade log for the given time interval.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = AccountApiExamples.TRADE_LOG_EXAMPLE)
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
    @GetMapping("/trade-log")
    public StandardJsonResponse<TradeLog> getTradeLog(
            final HttpServletRequest request,
            final @RequestParam("start") String start,
            final @RequestParam("end") String end,
            final @RequestParam("interval") String interval,
            final @RequestParam(value = "count", defaultValue = "" + CorePlatformConstants.DEFAULT_TRADE_RECORD_COLLECTION_SIZE, required = false) int count) {

        validate(start, end);
        if (!EnumUtils.isValidEnumIgnoreCase(TradeRecordTimeInterval.class, interval)) {
            return new StandardJsonResponse<>(false, null, String.format(CorePlatformConstants.Validation.DataIntegrity.INVALID_INTERVAL, interval));
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        return new StandardJsonResponse<>(true, this.tradeRecordService.getTradeLog(user, LocalDate.parse(start, DateTimeFormatter.ISO_DATE), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), TradeRecordTimeInterval.getInterval(interval), count), StringUtils.EMPTY);
    }
}
