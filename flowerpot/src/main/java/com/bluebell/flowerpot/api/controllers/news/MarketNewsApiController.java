package com.bluebell.flowerpot.api.controllers.news;

import com.bluebell.flowerpot.api.controllers.AbstractApiController;
import com.bluebell.flowerpot.api.converters.news.MarketNewsDTOConverter;
import com.bluebell.flowerpot.api.models.records.json.StandardJsonResponse;
import com.bluebell.flowerpot.core.models.entities.news.MarketNews;
import com.bluebell.flowerpot.core.services.news.MarketNewsService;
import com.bluebell.flowerpot.security.aspects.ValidateApiToken;
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
 * @version 0.0.4
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/news")
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
    @GetMapping("/get")
    public StandardJsonResponse getNews(final @RequestParam("date") String date, final HttpServletRequest request) {
        validate(date);
        final Optional<MarketNews> news = this.marketNewsService.findMarketNewsForDate(LocalDate.parse(date));
        return news.map(marketNews -> new StandardJsonResponse(true, this.marketNewsDTOConverter.convert(marketNews), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse(false, null, String.format("No news for the given date %s", date)));
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
    @GetMapping("/get-for-interval")
    public StandardJsonResponse getNewsForInterval(final @RequestParam("start") String start, final @RequestParam("end") String end, final @RequestParam(required = false) String[] locales, final HttpServletRequest request) {
        validate(start, end);
        final List<MarketNews> news = this.marketNewsService.findNewsWithinInterval(LocalDate.parse(start), LocalDate.parse(end), locales);
        if (CollectionUtils.isNotEmpty(news)) {
            return new StandardJsonResponse(true, this.marketNewsDTOConverter.convertAll(news), StringUtils.EMPTY);
        }

        return new StandardJsonResponse(false, null, "No market news available");
    }


    //  ----------------- POST REQUESTS -----------------

    /**
     * Fetches and updates market news
     *
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @PostMapping("/fetch-news")
    public StandardJsonResponse postFetchNews(final HttpServletRequest request) {

        final boolean result = this.marketNewsService.fetchMarketNews();
        if (result) {
            return new StandardJsonResponse(true, null, StringUtils.EMPTY);
        }

        return new StandardJsonResponse(false, null, "There was an error fetching the market news");
    }
}
