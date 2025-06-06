package com.bluebell.planter.controllers.portfolio;

import com.bluebell.planter.constants.ApiPaths;
import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.planter.converters.portfolio.PortfolioDTOConverter;
import com.bluebell.platform.models.api.dto.portfolio.CreateUpdatePortfolioDTO;
import com.bluebell.platform.models.api.dto.portfolio.PortfolioDTO;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioRecord;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.security.constants.SecurityConstants;
import com.bluebell.radicle.services.portfolio.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * API Controller for {@link PortfolioRecord}
 *
 * @author Stephen Prizio
 * @version 0.2.2
 */
@RestController
@RequestMapping("${bluebell.base.api.controller.endpoint}" + ApiPaths.Portfolio.BASE)
@Tag(name = "Portfolio", description = "Handles endpoints & operations related to obtaining portfolio information.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class PortfolioApiController extends AbstractApiController {

    @Resource(name = "portfolioDTOConverter")
    private PortfolioDTOConverter portfolioDTOConverter;

    @Resource(name = "portfolioService")
    private PortfolioService portfolioService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link PortfolioDTO}
     *
     * @param portfolioNumber portfolio number
     * @param request      {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Get the portfolio for the number", description = "Obtains the Portfolio for the given number")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully retrieves the portfolio.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the portfolio for the given number.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No portfolio was found for number 1234")
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
    @GetMapping(ApiPaths.Portfolio.GET)
    public StandardJsonResponse<PortfolioDTO> getPortfolioForNumber(
            @Parameter(name = "portfolioNumber", description = "The number for the portfolio", example = "1234")
            final @RequestParam("portfolioNumber") long portfolioNumber,
            final HttpServletRequest request
    ) {

        final Optional<Portfolio> portfolio = this.portfolioService.findPortfolioForPortfolioNumber(portfolioNumber);
        if (portfolio.isPresent()) {
            return StandardJsonResponse
                    .<PortfolioDTO>builder()
                    .success(true)
                    .data(this.portfolioDTOConverter.convert(portfolio.get()))
                    .build();
        } else {
            return StandardJsonResponse
                    .<PortfolioDTO>builder()
                    .success(false)
                    .message(String.format("No portfolio was found for number %d", portfolioNumber))
                    .build();
        }
    }


    //  ----------------- POST REQUESTS -----------------

    /**
     * Returns a {@link Portfolio}
     *
     * @param request {@link HttpServletRequest}
     * @param data    {@link CreateUpdatePortfolioDTO}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Creates a new portfolio", description = "Creates a new portfolio for the current user")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully creates a new portfolio.",
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
    @PostMapping(ApiPaths.Portfolio.CREATE_PORTFOLIO)
    public StandardJsonResponse<PortfolioDTO> postCreateNewPortfolio(
            @Parameter(name = "Portfolio Payload", description = "Payload for creating or updating portfolios")
            final @RequestBody CreateUpdatePortfolioDTO data,
            final HttpServletRequest request
    ) {
        if (data == null || StringUtils.isEmpty(data.name())) {
            throw new MissingRequiredDataException("The required data for creating a Portfolio entity was null or empty");
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        return StandardJsonResponse
                .<PortfolioDTO>builder()
                .success(true)
                .data(this.portfolioDTOConverter.convert(this.portfolioService.createPortfolio(data, user)))
                .build();
    }


    //  ----------------- PUT REQUESTS -----------------

    /**
     * Returns an updated {@link Portfolio}
     *
     * @param request {@link HttpServletRequest}
     * @param data {@link CreateUpdatePortfolioDTO}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Updates an existing portfolio", description = "Updates an existing portfolio for the current user.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully updates the portfolio.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot not find the requested portfolio",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Portfolio not found.")
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
    @PutMapping(ApiPaths.Portfolio.UPDATE_PORTFOLIO)
    public StandardJsonResponse<PortfolioDTO> putUpdatePortfolio(
            @Parameter(name = "Portfolio Payload", description = "Payload for creating or updating portfolios")
            final @RequestBody CreateUpdatePortfolioDTO data,
            @Parameter(name = "portfolioNumber", description = "Portfolio to update", example = "1234")
            final @RequestParam("portfolioNumber") long portfolioNumber,
            final HttpServletRequest request
    ) {
        if (data == null || StringUtils.isEmpty(data.name())) {
            throw new MissingRequiredDataException("The required data for updating a Portfolio entity was null or empty");
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final Optional<Portfolio> portfolio = this.portfolioService.findPortfolioForPortfolioNumber(portfolioNumber);

        if (portfolio.isPresent() && user.getActivePortfolios().contains(portfolio.get())) {
            return StandardJsonResponse
                    .<PortfolioDTO>builder()
                    .success(true)
                    .data(this.portfolioDTOConverter.convert(this.portfolioService.updatePortfolio(portfolio.get(), data, user)))
                    .build();
        } else {
            return StandardJsonResponse
                    .<PortfolioDTO>builder()
                    .success(false)
                    .message(String.format("Portfolio not found for number: %d", portfolioNumber))
                    .build();
        }
    }


    //  ----------------- DELETE REQUESTS -----------------

    /**
     * Deletes an {@link Portfolio} with the matching number
     *
     * @param portfolioNumber portfolio number
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Deletes a user portfolio", description = "Deletes the user portfolio with the matching number.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully deletes a portfolio.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the portfolio for the given number.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No portfolio was found for number 1234")
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
    @DeleteMapping(ApiPaths.Portfolio.DELETE_PORTFOLIO)
    public StandardJsonResponse<Boolean> deletePortfolio(
            @Parameter(name = "portfolioNumber", description = "Portfolio to delete", example = "1234")
            final @RequestParam("portfolioNumber") long portfolioNumber,
            final HttpServletRequest request
    ) {
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final Optional<Portfolio> portfolio = this.portfolioService.findPortfolioForPortfolioNumber(portfolioNumber);
        return portfolio.map(value -> {
            final boolean result = this.portfolioService.deletePortfolio(value);
            if (result) {
                this.portfolioService.reassignPortfolios(user.getUsername());
            }
            return StandardJsonResponse.<Boolean>builder().success(result).data(result).build();
        }).orElseGet(() -> StandardJsonResponse.<Boolean>builder().success(false).data(false).message(String.format("Portfolio not found for number %d", portfolioNumber)).build());
    }
}
