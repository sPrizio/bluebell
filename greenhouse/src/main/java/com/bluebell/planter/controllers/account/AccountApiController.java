package com.bluebell.planter.controllers.account;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.bluebell.radicle.validation.GenericValidator.validateJsonIntegrity;

import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.planter.converters.account.AccountDTOConverter;
import com.bluebell.platform.enums.account.AccountType;
import com.bluebell.platform.enums.account.Broker;
import com.bluebell.platform.enums.account.Currency;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.models.api.dto.account.AccountDTO;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.nonentities.data.PairEntry;
import com.bluebell.platform.models.core.nonentities.records.account.AccountDetails;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.security.constants.SecurityConstants;
import com.bluebell.radicle.services.account.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * API Controller for {@link Account}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/account")
@Tag(name = "Account", description = "Handles endpoints & operations related to a user's account")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class AccountApiController extends AbstractApiController {

    private static final String ACCOUNT = "account";
    private static final String NO_ACCOUNT_FOR_ACCOUNT_NUMBER = "No account was found for account number %d";

    @Resource(name = "accountDTOConverter")
    private AccountDTOConverter accountDTOConverter;

    @Resource(name = "accountService")
    private AccountService accountService;


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
    @GetMapping("/currencies")
    public StandardJsonResponse<List<PairEntry>> getCurrencies(final HttpServletRequest request) {
        return new StandardJsonResponse<>(true, Arrays.stream(Currency.values()).map(c -> new PairEntry(c.getIsoCode(), c.getLabel(), c.getSymbol())).toList(), StringUtils.EMPTY);
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
    @GetMapping("/account-types")
    public StandardJsonResponse<List<PairEntry>> getAccountTypes(final HttpServletRequest request) {
        return StandardJsonResponse
                .<List<PairEntry>>builder()
                .success(true)
                .data(Arrays.stream(AccountType.values()).map(at -> new PairEntry(at.getLabel().toUpperCase(), at.getLabel(), StringUtils.EMPTY)).toList())
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
    @GetMapping("/brokers")
    public StandardJsonResponse<List<PairEntry>> getBrokers(final HttpServletRequest request) {
        return new StandardJsonResponse<>(true, Arrays.stream(Broker.values()).map(b -> new PairEntry(b.getCode(), b.getName(), StringUtils.EMPTY)).toList(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link TradePlatform}s
     *
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Get all system trading platforms", description = "Fetches all trading platforms that are currently supported in bluebell. Trading platforms include examples like Metatrader 4 & cTraders.")
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
    @GetMapping("/trade-platforms")
    public StandardJsonResponse<List<PairEntry>> getTradePlatforms(final HttpServletRequest request) {
        return new StandardJsonResponse<>(true, Arrays.stream(TradePlatform.values()).map(tp -> new PairEntry(tp.getCode(), tp.getLabel(), StringUtils.EMPTY)).toList(), StringUtils.EMPTY);
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
    @GetMapping("/get-details")
    public StandardJsonResponse<AccountDetails> getDetails(final @RequestParam("accountNumber") Long accountNumber, final HttpServletRequest request) {
        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account.map(value -> new StandardJsonResponse<>(true, this.accountService.getAccountDetails(value), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse<>(false, null, String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, accountNumber)));
    }


    //  ----------------- POST REQUESTS -----------------

    /**
     * Returns a {@link Account}
     *
     * @param request     {@link HttpServletRequest}
     * @param requestBody json request
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
            responseCode = "401",
            description = "Response when the api call made was unauthorized.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "The API token was invalid.")
            )
    )
    @PostMapping("/create-account")
    public StandardJsonResponse<AccountDTO> postCreateNewAccount(final HttpServletRequest request, final @RequestBody Map<String, Object> requestBody) {
        validateJsonIntegrity(requestBody, List.of(ACCOUNT), "json did not contain of the required keys : %s", List.of(ACCOUNT));
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        return new StandardJsonResponse<>(true, this.accountDTOConverter.convert(this.accountService.createNewAccount(requestBody, user)), StringUtils.EMPTY);
    }


    //  ----------------- PUT REQUESTS -----------------

    /**
     * Returns an updated {@link Account}
     *
     * @param accountNumber account number
     * @param request {@link HttpServletRequest}
     * @param requestBody json request
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
    @PutMapping("/update-account")
    public StandardJsonResponse<AccountDTO> putUpdateAccount(final @RequestParam("accountNumber") long accountNumber, final HttpServletRequest request, final @RequestBody Map<String, Object> requestBody) {
        validateJsonIntegrity(requestBody, List.of(ACCOUNT), "json did not contain of the required keys : %s", List.of(ACCOUNT));
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account.map(value -> new StandardJsonResponse<>(true, this.accountDTOConverter.convert(this.accountService.updateAccount(value, requestBody, user)), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse<>(false, null, String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, accountNumber)));
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
    @DeleteMapping("/delete-account")
    public StandardJsonResponse<Boolean> deleteAccount(final @RequestParam("accountNumber") long accountNumber, final HttpServletRequest request) {
        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account.map(value -> {
            final boolean result = this.accountService.deleteAccount(value);
            return new StandardJsonResponse<>(result, result, StringUtils.EMPTY);
        }).orElseGet(() -> new StandardJsonResponse<>(false, false, String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, accountNumber)));
    }
}

