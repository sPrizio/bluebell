package com.bluebell.planter.controllers.news;

import com.bluebell.planter.constants.ApiPaths;
import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.planter.converters.news.MarketNewsDTOConverter;
import com.bluebell.platform.models.api.dto.news.MarketNewsDTO;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.news.MarketNews;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.services.news.MarketNewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * API controller for {@link MarketNews}
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@RestController
@RequestMapping("${bluebell.base.api.controller.endpoint}" + ApiPaths.MarketNews.BASE)
@Tag(name = "Market News", description = "Handles endpoints & operations related to obtaining and fetching market news.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class MarketNewsApiController extends AbstractApiController {

    @Resource(name = "marketNewsDTOConverter")
    private MarketNewsDTOConverter marketNewsDTOConverter;

    @Resource(name = "marketNewsService")
    private MarketNewsService marketNewsService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Finds {@link MarketNews} for the given date
     *
     * @param date    date
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains market news for a specific date", description = "Returns market news for the specified date.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid date",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid date")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find any news for the given date",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No market news found for date")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains market news.",
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
    @GetMapping(ApiPaths.MarketNews.GET)
    public StandardJsonResponse<MarketNewsDTO> getNews(
            @Parameter(name = "date", description = "The date to obtain market news", example = "2025-01-01")
            final @RequestParam("date") String date,
            final HttpServletRequest request
    ) {
        validate(date);
        final Optional<MarketNews> news = this.marketNewsService.findMarketNewsForDate(LocalDate.parse(date));
        return news
                .map(marketNews -> StandardJsonResponse.<MarketNewsDTO>builder().success(true).data(this.marketNewsDTOConverter.convert(marketNews)).build())
                .orElseGet(() -> StandardJsonResponse.<MarketNewsDTO>builder().success(false).message(String.format("No news for the given date %s", date)).build());
    }

    /**
     * Finds {@link MarketNews} for the given interval of time
     *
     * @param start   start date
     * @param end     end date
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains market news for an interval of time", description = "Returns market news for the specified interval of time.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid date",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid date")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find any news for the given time interval",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No news for the given date")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains market news for the specified time interval.",
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
    @GetMapping(ApiPaths.MarketNews.GET_FOR_INTERVAL)
    public StandardJsonResponse<List<MarketNewsDTO>> getNewsForInterval(
            @Parameter(name = "start", description = "Start date of time period to analyze", example = "2025-01-01")
            final @RequestParam("start") String start,
            @Parameter(name = "end", description = "End date of time period to analyze", example = "2025-01-01")
            final @RequestParam("end") String end,
            @Parameter(name = "locales", description = "Locales on which to obtain market news", example = "CAN, USA")
            final @RequestParam(required = false) String[] locales,
            final HttpServletRequest request
    ) {
        validate(start, end);
        final List<MarketNews> news = this.marketNewsService.findNewsWithinInterval(LocalDate.parse(start), LocalDate.parse(end), locales);
        if (CollectionUtils.isNotEmpty(news)) {
            return StandardJsonResponse
                    .<List<MarketNewsDTO>>builder()
                    .success(true)
                    .data(this.marketNewsDTOConverter.convertAll(news))
                    .build();
        }

        return StandardJsonResponse
                .<List<MarketNewsDTO>>builder()
                .success(false)
                .message("No market news available")
                .build();
    }


    //  ----------------- POST REQUESTS -----------------

    /**
     * Fetches and updates market news
     *
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Fetches market news from third-party providers", description = "Fetches market news from an external api and captures the data in bluebell.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains market news.",
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
    @PostMapping(ApiPaths.MarketNews.FETCH_NEWS)
    public StandardJsonResponse<Boolean> postFetchNews(final HttpServletRequest request) {

        final boolean result = this.marketNewsService.fetchMarketNews();
        if (result) {
            return StandardJsonResponse
                    .<Boolean>builder()
                    .success(true)
                    .build();
        }

        return StandardJsonResponse
                .<Boolean>builder()
                .success(false)
                .message("There was an error fetching the market news")
                .build();
    }
}
