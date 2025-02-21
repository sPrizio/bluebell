package com.bluebell.planter.controllers.news;

import com.bluebell.processing.api.examples.AccountApiExamples;
import com.bluebell.processing.api.examples.ApiErrorExamples;
import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.planter.converters.news.MarketNewsDTOConverter;
import com.bluebell.platform.models.api.dto.news.MarketNewsDTO;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.news.MarketNews;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.services.news.MarketNewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * API controller for {@link MarketNews}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/news")
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
     * @param date date
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
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = ApiErrorExamples.BAD_DATE_EXAMPLE)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find any news for the given date",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = ApiErrorExamples.NO_NEWS_EXAMPLE)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains market news.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = AccountApiExamples.NEWS_EXAMPLE)
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
    @GetMapping("/get")
    public StandardJsonResponse<MarketNewsDTO> getNews(final @RequestParam("date") String date, final HttpServletRequest request) {
        validate(date);
        final Optional<MarketNews> news = this.marketNewsService.findMarketNewsForDate(LocalDate.parse(date));
        return news.map(marketNews -> new StandardJsonResponse<>(true, this.marketNewsDTOConverter.convert(marketNews), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse<>(false, null, String.format("No news for the given date %s", date)));
    }

    /**
     * Finds {@link MarketNews} for the given interval of time
     *
     * @param start start date
     * @param end end date
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains market news for an interval of time", description = "Returns market news for the specified interval of time.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an date",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = ApiErrorExamples.BAD_DATE_EXAMPLE)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find any news for the given time interval",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = ApiErrorExamples.NO_NEWS_EXAMPLE)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains market news for the specified time interval.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = AccountApiExamples.NEWS_LIST_EXAMPLE)
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
    @GetMapping("/get-for-interval")
    public StandardJsonResponse<List<MarketNewsDTO>> getNewsForInterval(final @RequestParam("start") String start, final @RequestParam("end") String end, final @RequestParam(required = false) String[] locales, final HttpServletRequest request) {
        validate(start, end);
        final List<MarketNews> news = this.marketNewsService.findNewsWithinInterval(LocalDate.parse(start), LocalDate.parse(end), locales);
        if (CollectionUtils.isNotEmpty(news)) {
            return new StandardJsonResponse<>(true, this.marketNewsDTOConverter.convertAll(news), StringUtils.EMPTY);
        }

        return new StandardJsonResponse<>(false, null, "No market news available");
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
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = ApiPrimitiveExamples.BOOLEAN_EXAMPLE)
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
    @PostMapping("/fetch-news")
    public StandardJsonResponse<Boolean> postFetchNews(final HttpServletRequest request) {

        final boolean result = this.marketNewsService.fetchMarketNews();
        if (result) {
            return new StandardJsonResponse<>(true, null, StringUtils.EMPTY);
        }

        return new StandardJsonResponse<>(false, null, "There was an error fetching the market news");
    }
}
