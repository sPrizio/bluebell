package com.bluebell.planter.controllers.portfolio;

import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioRecord;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.security.constants.SecurityConstants;
import com.bluebell.radicle.services.portfolio.PortfolioRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * API controller for {@link PortfolioRecord}
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@RestController
@RequestMapping("${bluebell.base.api.controller.endpoint}/portfolio-record")
@Tag(name = "Portfolio", description = "Handles endpoints & operations related to obtaining portfolio information.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class PortfolioRecordApiController extends AbstractApiController {

    @Resource(name = "portfolioRecordService")
    private PortfolioRecordService portfolioRecordService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link PortfolioRecord}
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains statistics about a user's portfolio", description = "Returns a single portfolio record, aggregated by accounts.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains portfolio information.",
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
    @GetMapping("/get")
    public StandardJsonResponse<PortfolioRecord> getPortfolioRecord(
            @Parameter(name = "Portfolio UID", description = "Portfolio UID to add the account to", example = "1234")
            final @RequestParam("portfolioUid") String portfolioUid,
            final HttpServletRequest request
    ) {
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        return StandardJsonResponse
                .<PortfolioRecord>builder()
                .success(true)
                .data(this.portfolioRecordService.getSinglePortfolioRecord(portfolioUid, user))
                .build();
    }

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link PortfolioRecord}
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains statistics about all user portfolios", description = "Returns all portfolio records, aggregated by accounts.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains portfolio information.",
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
    @GetMapping("/get-comprehensive")
    public StandardJsonResponse<PortfolioRecord> getComprehensivePortfolioRecords(final HttpServletRequest request) {
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        return StandardJsonResponse
                .<PortfolioRecord>builder()
                .success(true)
                .data(this.portfolioRecordService.getComprehensivePortfolioRecord(user))
                .build();
    }
}
