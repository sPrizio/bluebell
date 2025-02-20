package com.bluebell.planter.api.controllers.portfolio;

import com.bluebell.planter.api.controllers.AbstractApiController;
import com.bluebell.planter.core.services.portfolio.PortfolioService;
import com.bluebell.planter.security.aspects.ValidateApiToken;
import com.bluebell.planter.security.constants.SecurityConstants;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.nonentities.records.portfolio.Portfolio;
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

/**
 * API Controller for {@link Portfolio}
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/portfolio")
@Tag(name = "Portoflio", description = "Handles endpoints & operations related to obtaining portfolio information.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class PortfolioApiController extends AbstractApiController {

    @Resource(name = "portfolioService")
    private PortfolioService portfolioService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link Portfolio}
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains the user's portfolio", description = "Returns a portfolio which is essentially a list of aggregated accuonts.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains time-bucket analysis.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = AccountApiExamples.PORTFOLIO_EXAMPLE)
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
    public StandardJsonResponse<Portfolio> getPortfolio(final HttpServletRequest request) {
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        return new StandardJsonResponse<>(true, this.portfolioService.getPortfolio(user), StringUtils.EMPTY);
    }
}
