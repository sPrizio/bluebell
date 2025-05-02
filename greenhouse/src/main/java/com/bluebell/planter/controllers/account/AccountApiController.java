package com.bluebell.planter.controllers.account;

import com.bluebell.planter.constants.ApiPaths;
import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.planter.converters.account.AccountDTOConverter;
import com.bluebell.platform.enums.account.AccountType;
import com.bluebell.platform.enums.account.Broker;
import com.bluebell.platform.enums.account.Currency;
import com.bluebell.platform.enums.security.UserRole;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.models.api.dto.account.AccountDTO;
import com.bluebell.platform.models.api.dto.account.CreateUpdateAccountDTO;
import com.bluebell.platform.models.api.dto.account.CreateUpdateAccountTradingDataDTO;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.nonentities.data.PairEntry;
import com.bluebell.platform.models.core.nonentities.records.account.AccountDetails;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.security.constants.SecurityConstants;
import com.bluebell.radicle.services.account.AccountService;
import com.bluebell.radicle.services.portfolio.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * API Controller for {@link Account}
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@RestController
@RequestMapping("${bluebell.base.api.controller.endpoint}" + ApiPaths.Account.BASE)
@Tag(name = "Account", description = "Handles endpoints & operations related to a user's account")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class AccountApiController extends AbstractApiController {

    private static final String NO_ACCOUNT_FOR_ACCOUNT_NUMBER = "No account was found for account number %d";

    @Resource(name = "accountDTOConverter")
    private AccountDTOConverter accountDTOConverter;

    @Resource(name = "accountService")
    private AccountService accountService;

    @Resource(name = "portfolioService")
    private PortfolioService portfolioService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link Currency}s
     *
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Get all system currencies", description = "Fetches all currencies that are currently supported in bluebell, including isoCodes and display labels")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully retrieves all currencies.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = StandardJsonResponse.class,
                            type = "object"
                    )
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
    @GetMapping(ApiPaths.Account.CURRENCIES)
    public StandardJsonResponse<List<PairEntry>> getCurrencies(final HttpServletRequest request) {
        return StandardJsonResponse
                .<List<PairEntry>>builder()
                .success(true)
                .data(Arrays.stream(Currency.values()).map(c -> PairEntry.builder().code(c.getIsoCode()).label(c.getLabel()).symbol(c.getSymbol()).build()).toList())
                .build();
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link AccountType}s
     *
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Get all system account types", description = "Fetches all account types that are currently supported in bluebell. Account types refer to types such as CFD, Shares, Options, etc; i.e. different financial derivatives.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully retrieves all account types.",
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
    @GetMapping(ApiPaths.Account.ACCOUNT_TYPES)
    public StandardJsonResponse<List<PairEntry>> getAccountTypes(final HttpServletRequest request) {
        return StandardJsonResponse
                .<List<PairEntry>>builder()
                .success(true)
                .data(Arrays.stream(AccountType.values()).map(at -> PairEntry.builder().code(at.getLabel().toUpperCase()).label(at.getLabel()).build()).toList())
                .build();
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link Broker}s
     *
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Get all system brokers", description = "Fetches all brokers that are currently supported in bluebell.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully retrieves all brokers.",
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
    @GetMapping(ApiPaths.Account.BROKERS)
    public StandardJsonResponse<List<PairEntry>> getBrokers(final HttpServletRequest request) {
        return StandardJsonResponse.
                <List<PairEntry>>builder()
                .success(true)
                .data(Arrays.stream(Broker.values()).map(b -> PairEntry.builder().code(b.getCode()).label(b.getLabel()).build()).toList())
                .build();
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link TradePlatform}s
     *
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Get all system trading platforms", description = "Fetches all trading platforms that are currently supported in bluebell. Trading platforms include examples like MetaTrader 4 & cTraders.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully retrieves all trade platforms.",
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
    @GetMapping(ApiPaths.Account.TRADE_PLATFORMS)
    public StandardJsonResponse<List<PairEntry>> getTradePlatforms(final HttpServletRequest request) {
        return StandardJsonResponse
                .<List<PairEntry>>builder()
                .success(true)
                .data(Arrays.stream(TradePlatform.values()).map(tp -> PairEntry.builder().code(tp.getCode()).label(tp.getLabel()).build()).toList())
                .build();
    }

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link AccountDetails} object
     *
     * @param accountNumber account number
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Get the account's details", description = "Computes and returns details about the given account's trading performance.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully retrieves the account's details.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the account for the given account number.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No account was found for account 1234")
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
    @GetMapping(ApiPaths.Account.GET_DETAILS)
    public StandardJsonResponse<AccountDetails> getDetails(
            @Parameter(name = "accountNumber", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") Long accountNumber,
            final HttpServletRequest request
    ) {
        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account
                .map(value -> StandardJsonResponse.<AccountDetails>builder().success(true).data(this.accountService.getAccountDetails(value)).build())
                .orElseGet(() -> StandardJsonResponse.<AccountDetails>builder().success(false).message(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, accountNumber)).build());
    }


    //  ----------------- POST REQUESTS -----------------

    /**
     * Creates a new {@link Account}
     *
     * @param portfolioUid portfolio uid
     * @param data {@link CreateUpdateAccountDTO}
     * @param request     {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Creates a new account", description = "Creates a new account for the current user")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully creates a new account.",
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
    @PostMapping(ApiPaths.Account.CREATE_ACCOUNT)
    public StandardJsonResponse<AccountDTO> postCreateNewAccount(
            @Parameter(name = "Account Payload", description = "Payload for creating or updating accounts")
            final @RequestBody CreateUpdateAccountDTO data,
            @Parameter(name = "portfolioUid", description = "Portfolio UID to add the account to", example = "1234")
            final @RequestParam("portfolioUid") String portfolioUid,
            final HttpServletRequest request
    ) {
        if (data == null || data.number() == null) {
            throw new MissingRequiredDataException("The required data for creating an Account entity was null or empty");
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final Optional<Portfolio> portfolio = this.portfolioService.findPortfolioByUid(portfolioUid);
        if (portfolio.isPresent() && user.getActivePortfolios().contains(portfolio.get())) {
            return StandardJsonResponse
                    .<AccountDTO>builder()
                    .success(true)
                    .data(this.accountDTOConverter.convert(this.accountService.createNewAccount(data, portfolio.get())))
                    .build();
        }

        return StandardJsonResponse
                .<AccountDTO>builder()
                .success(false)
                .message(String.format("Portfolio not found for uid: %s", portfolioUid))
                .build();
    }

    /**
     * Takes in account trading data and updates the associated data on the account within the payload. This endpoint is designed to be used by the system
     *
     * @param data  {@link CreateUpdateAccountTradingDataDTO}
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken(role = UserRole.SYSTEM)
    @Operation(summary = "Updates account trading data", description = "System endpoint that creates/updates trades & transactions from the given payload")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully updates the account's trading information.",
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
    @PostMapping(ApiPaths.Account.UPDATE_TRADE_DATA)
    public StandardJsonResponse<Boolean> postUpdateAccountTradingData(
            @Parameter(name = "Trading Data Payload", description = "Payload for creating or updating account trading data")
            final @RequestBody CreateUpdateAccountTradingDataDTO data,
            final HttpServletRequest request
    ) {
        final boolean result = this.accountService.updateAccountTradingData(data);
        return StandardJsonResponse
                .<Boolean>builder()
                .success(result)
                .data(result)
                .message(result ? "Trading information updated successfully" : "Trading information updates failed")
                .build();
    }


    //  ----------------- PUT REQUESTS -----------------

    /**
     * Returns an updated {@link Account}
     *
     * @param data {@link CreateUpdateAccountDTO}
     * @param accountNumber account number
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Updates an existing account", description = "Updates an existing account for the current user.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully updates the account.",
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
            responseCode = "200",
            description = "Response when the api cannot find the account for the given account number.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No account was found for number 1234")
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
    @PutMapping(ApiPaths.Account.UPDATE_ACCOUNT)
    public StandardJsonResponse<AccountDTO> putUpdateAccount(
            @Parameter(name = "accountNumber", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            @Parameter(name = "Account Payload", description = "Payload for creating or updating accounts")
            final @RequestBody CreateUpdateAccountDTO data,
            @Parameter(name = "portfolioUid", description = "Portfolio UID to add the account to", example = "1234")
            final @RequestParam("portfolioUid") String portfolioUid,
            final HttpServletRequest request
    ) {
        if (data == null || data.number() == null) {
            throw new MissingRequiredDataException("The required data for updating an Account entity was null or empty");
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        final Optional<Portfolio> portfolio = this.portfolioService.findPortfolioByUid(portfolioUid);

        if (portfolio.isPresent() && user.getActivePortfolios().contains(portfolio.get())) {
            return account
                    .map(value -> StandardJsonResponse.<AccountDTO>builder().success(true).data(this.accountDTOConverter.convert(this.accountService.updateAccount(value, data, portfolio.get()))).build())
                    .orElseGet(() -> StandardJsonResponse.<AccountDTO>builder().success(false).message(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, accountNumber)).build());
        } else {
            return StandardJsonResponse
                    .<AccountDTO>builder()
                    .success(false)
                    .message(String.format("Portfolio not found for uid: %s", portfolioUid))
                    .build();
        }
    }


    //  ----------------- DELETE REQUESTS -----------------

    /**
     * Deletes an {@link Account} with the matching account number
     *
     * @param accountNumber account number
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Deletes a user account", description = "Deletes the user account with the matching account number.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully deletes an account.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the account for the given account number.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No account was found for number 1234")
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
    @DeleteMapping(ApiPaths.Account.DELETE_ACCOUNT)
    public StandardJsonResponse<Boolean> deleteAccount(
            @Parameter(name = "portfolioUid", description = "Portfolio UID to add the account to", example = "1234")
            final @RequestParam("portfolioUid") String portfolioUid,
            @Parameter(name = "Account Number", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            final HttpServletRequest request
    ) {
        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account.map(value -> {
            final boolean result = this.accountService.deleteAccount(value);
            return StandardJsonResponse.<Boolean>builder().success(result).data(result).build();
        }).orElseGet(() -> StandardJsonResponse.<Boolean>builder().success(false).data(false).message(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, accountNumber)).build());
    }
}

