package com.bluebell.planter.controllers.analysis;

import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.analysis.AnalysisFilter;
import com.bluebell.platform.enums.analysis.TradeDurationFilter;
import com.bluebell.platform.enums.time.PlatformTimeInterval;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.nonentities.records.analysis.AnalysisResult;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.services.account.AccountService;
import com.bluebell.radicle.services.analysis.AnalysisService;
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

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

/**
 * Controller for {@link AnalysisService}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/analysis")
@Tag(name = "Analysis", description = "Handles endpoints & operations related to obtaining analysis about trading accounts.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class AnalysisApiController extends AbstractApiController {

    @Resource(name = "accountService")
    private AccountService accountService;

    @Resource(name = "analysisService")
    private AnalysisService analysisService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link List} of {@link AnalysisResult}s
     *
     * @param accountNumber account number
     * @param filter        {@link AnalysisFilter}
     * @param isOpened      trade opened or closed times
     * @param request       {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains the result of analysing time-bucket-based performance", description = "Returns a list of Analysis results that correspond to analysis results for each valid time bucket.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid filter",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid Filter")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the account for the given account number",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No account found for number 1234")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains time-bucket analysis.",
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
    @GetMapping("/time-buckets")
    public StandardJsonResponse<List<AnalysisResult>> getTimeBucketsAnalysis(
            @Parameter(name = "Account Number", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            @Parameter(name = "Analysis Filter", description = "Refers to the attribute on which to analyze. Supported values are currently: PROFIT, PERCENTAGE or POINTS", example = "PROFIT")
            final @RequestParam("filter") String filter,
            @Parameter(name = "Use Open Price", description = "If true, uses the trade's open time, else uses the close time (if applicable)", example = "true")
            final @RequestParam("isOpened") boolean isOpened,
            final HttpServletRequest request
    ) {

        if (AnalysisFilter.getAnalysisFilter(filter) == null) {
            return StandardJsonResponse
                    .<List<AnalysisResult>>builder()
                    .success(false)
                    .message(String.format(CorePlatformConstants.Validation.DataIntegrity.INVALID_FILTER, filter))
                    .build();
        }

        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account
                .map(value -> StandardJsonResponse.<List<AnalysisResult>>builder().success(true).data(this.analysisService.computeTimeBucketAnalysis(value, PlatformTimeInterval.THIRTY_MINUTE, AnalysisFilter.getAnalysisFilter(filter.toUpperCase()), isOpened)).build())
                .orElseGet(() -> StandardJsonResponse.<List<AnalysisResult>>builder().success(false).message(CorePlatformConstants.Validation.Account.ACCOUNT_NOT_FOUND).build());
    }

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link List} of {@link AnalysisResult}s
     *
     * @param accountNumber account number
     * @param filter        {@link AnalysisFilter}
     * @param request       {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */

    @ValidateApiToken
    @Operation(summary = "Obtains the result of analysing weekday-based performance", description = "Returns a list of Analysis results that correspond to analysis results for each valid weekday.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid filter.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid Filter")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the account for the given account number.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No account found for number 1234")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the weekday analysis.",
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
    @GetMapping("/weekdays")
    public StandardJsonResponse<List<AnalysisResult>> getWeekdaysAnalysis(
            @Parameter(name = "Account Number", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            @Parameter(name = "Analysis Filter", description = "Refers to the attribute on which to analyze. Supported values are currently: PROFIT, PERCENTAGE or POINTS", example = "PROFIT")
            final @RequestParam("filter") String filter,
            final HttpServletRequest request
    ) {

        if (AnalysisFilter.getAnalysisFilter(filter) == null) {
            return StandardJsonResponse
                    .<List<AnalysisResult>>builder()
                    .success(false)
                    .message(String.format(CorePlatformConstants.Validation.DataIntegrity.INVALID_FILTER, filter))
                    .build();
        }

        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account
                .map(value -> StandardJsonResponse.<List<AnalysisResult>>builder().success(true).data(this.analysisService.computeWeekdayAnalysis(value, AnalysisFilter.getAnalysisFilter(filter.toUpperCase()))).build())
                .orElseGet(() -> StandardJsonResponse.<List<AnalysisResult>>builder().success(false).message(CorePlatformConstants.Validation.Account.ACCOUNT_NOT_FOUND).build());
    }

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link List} of {@link AnalysisResult}s
     *
     * @param accountNumber account number
     * @param filter        {@link AnalysisFilter}
     * @param weekday       {@link DayOfWeek}
     * @param request       {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */

    @ValidateApiToken
    @Operation(summary = "Obtains the result of analysing time-bucket & weekday-based performance", description = "Returns a list of Analysis results that correspond to analysis results for each valid weekday and time-bucket.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid filter.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid Filter.")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid weekday.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid Weekday.")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the account for the given account number.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No account found for number 1234")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the time-bucket & weekday analysis.",
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
    @GetMapping("/weekdays-time-buckets")
    public StandardJsonResponse<List<AnalysisResult>> getWeekdayTimeBucketsAnalysis(
            @Parameter(name = "Account Number", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            @Parameter(name = "Analysis Filter", description = "Refers to the attribute on which to analyze. Supported values are currently: PROFIT, PERCENTAGE or POINTS", example = "PROFIT")
            final @RequestParam("filter") String filter,
            @Parameter(name = "Weekday", description = "Day of the week on which to analyze", example = "THURSDAY")
            final @RequestParam("weekday") String weekday,
            final HttpServletRequest request
    ) {

        if (AnalysisFilter.getAnalysisFilter(filter) == null) {
            return StandardJsonResponse
                    .<List<AnalysisResult>>builder()
                    .success(false)
                    .message(String.format(CorePlatformConstants.Validation.DataIntegrity.INVALID_FILTER, filter))
                    .build();
        }

        if (!EnumUtils.isValidEnumIgnoreCase(DayOfWeek.class, weekday)) {
            return StandardJsonResponse
                    .<List<AnalysisResult>>builder()
                    .success(false)
                    .message(String.format(CorePlatformConstants.Validation.DataIntegrity.INVALID_WEEKDAY, weekday))
                    .build();
        }

        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account
                .map(value -> StandardJsonResponse.<List<AnalysisResult>>builder().success(true).data(this.analysisService.computeWeekdayTimeBucketAnalysis(value, DayOfWeek.valueOf(weekday.toUpperCase()), PlatformTimeInterval.THIRTY_MINUTE, AnalysisFilter.getAnalysisFilter(filter.toUpperCase()))).build())
                .orElseGet(() -> StandardJsonResponse.<List<AnalysisResult>>builder().success(false).message(CorePlatformConstants.Validation.Account.ACCOUNT_NOT_FOUND).build());
    }

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link List} of {@link AnalysisResult}
     *
     * @param accountNumber       account number
     * @param filter              {@link AnalysisFilter}
     * @param tradeDurationFilter {@link TradeDurationFilter}
     * @param request             {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */

    @ValidateApiToken
    @Operation(summary = "Obtains the result of analysing performance based on trade duration", description = "Returns a list of Analysis results that correspond to analysis results trade durations broken down into buckets of time.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid filter.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid Filter")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the account for the given account number.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No account found for number 1234")
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
    @GetMapping("/trade-durations")
    public StandardJsonResponse<List<AnalysisResult>> getTradeDurationsAnalysis(
            @Parameter(name = "Account Number", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            @Parameter(name = "Analysis Filter", description = "Refers to the attribute on which to analyze. Supported values are currently: PROFIT, PERCENTAGE or POINTS", example = "PROFIT")
            final @RequestParam("filter") String filter,
            @Parameter(name = "Trade Type Filter", description = "Type of trade to compute durations. Examples are: ALL, WINS, LOSSES", example = "WINS")
            final @RequestParam("tradeDurationFilter") String tradeDurationFilter,
            final HttpServletRequest request
    ) {

        if (AnalysisFilter.getAnalysisFilter(filter) == null) {
            return StandardJsonResponse
                    .<List<AnalysisResult>>builder()
                    .success(false)
                    .message(String.format(CorePlatformConstants.Validation.DataIntegrity.INVALID_FILTER, filter))
                    .build();
        }

        if (TradeDurationFilter.getTradeDurationFilter(tradeDurationFilter) == null) {
            return StandardJsonResponse
                    .<List<AnalysisResult>>builder()
                    .success(false)
                    .message(String.format(CorePlatformConstants.Validation.DataIntegrity.INVALID_FILTER, tradeDurationFilter))
                    .build();
        }

        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account
                .map(value -> StandardJsonResponse.<List<AnalysisResult>>builder().success(true).data(this.analysisService.computeTradeDurationAnalysis(value, AnalysisFilter.getAnalysisFilter(filter.toUpperCase()), TradeDurationFilter.getTradeDurationFilter(tradeDurationFilter.toUpperCase()))).build())
                .orElseGet(() -> StandardJsonResponse.<List<AnalysisResult>>builder().success(false).message(CorePlatformConstants.Validation.Account.ACCOUNT_NOT_FOUND).build());
    }
}
