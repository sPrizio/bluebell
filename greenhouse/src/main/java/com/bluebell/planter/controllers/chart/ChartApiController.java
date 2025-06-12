package com.bluebell.planter.controllers.chart;

import com.bluebell.planter.constants.ApiPaths;
import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.models.core.nonentities.apexcharts.ApexChartCandleStick;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.security.constants.SecurityConstants;
import com.bluebell.radicle.services.chart.ChartService;
import com.bluebell.radicle.services.trade.TradeService;
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

import java.util.List;
import java.util.Optional;


/**
 * API controller for providing charting capabilities based on historical data
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@RestController
@RequestMapping("${bluebell.base.api.controller.endpoint}" + ApiPaths.Chart.BASE)
@Tag(name = "Charting", description = "Handles endpoints & operations related to charting data points.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class ChartApiController extends AbstractApiController {

    @Resource(name = "apexChartService")
    private ChartService<ApexChartCandleStick> chartService;

    @Resource(name = "tradeService")
    private TradeService tradeService;


    //  ----------------- GET REQUESTS -----------------

    /**
     * Generates a {@link List} of {@link ApexChartCandleStick}s for the given start and end interval
     *
     * @param tradeId trade id
     * @param accountNumber account number
     * @param interval interval of time
     * @param request  {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains the candlesticks for use with ApexCharts", description = "Returns a list of candlesticks in a format expected by ApexCharts.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api could not find the trade for the given trade id.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Trade not found for id <bad_id>.")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api was given an invalid time interval.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Time interval <bad_interval> was invalid.")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the chart info for the given trade.",
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
    @GetMapping(ApiPaths.Chart.APEX_DATA)
    public StandardJsonResponse<List<ApexChartCandleStick>> getApexChartData(
            @Parameter(name = "tradeId", description = "Id of the trade to chart", example = "1234")
            final @RequestParam("tradeId") String tradeId,
            @Parameter(name = "accountNumber", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            @Parameter(name = "interval", description = "Time interval to look at intra day", example = "ten-minute")
            final @RequestParam("interval") String interval,
            final HttpServletRequest request
    ) {
        if (!EnumUtils.isValidEnumIgnoreCase(MarketPriceTimeInterval.class, interval)) {
            return StandardJsonResponse
                    .<List<ApexChartCandleStick>>builder()
                    .success(false)
                    .message(String.format("%s is not a valid time interval", interval))
                    .build();
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final Optional<Trade> trade = this.tradeService.findTradeByTradeId(tradeId, getAccountForId(user, accountNumber));

        if (trade.isEmpty()) {
            return StandardJsonResponse
                    .<List<ApexChartCandleStick>>builder()
                    .success(false)
                    .message(String.format("Trade with id %s not found", tradeId))
                    .build();
        }

        final MarketPriceTimeInterval marketPriceTimeInterval = GenericEnum.getByCode(MarketPriceTimeInterval.class, interval);
        return StandardJsonResponse
                .<List<ApexChartCandleStick>>builder()
                .success(true)
                .data(this.chartService.getChartDataForTrade(trade.get(), marketPriceTimeInterval))
                .build();
    }
}
