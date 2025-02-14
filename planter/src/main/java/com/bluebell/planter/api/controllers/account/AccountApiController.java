package com.bluebell.planter.api.controllers.account;

import com.bluebell.planter.api.controllers.AbstractApiController;
import com.bluebell.planter.api.converters.account.AccountDTOConverter;
import com.bluebell.planter.api.models.records.json.StandardJsonResponse;
import com.bluebell.planter.api.models.records.platform.PairEntry;
import com.bluebell.planter.core.enums.account.AccountType;
import com.bluebell.planter.core.enums.account.Broker;
import com.bluebell.planter.core.enums.account.Currency;
import com.bluebell.planter.core.enums.trade.platform.TradePlatform;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.entities.security.User;
import com.bluebell.planter.core.models.nonentities.records.account.AccountDetails;
import com.bluebell.planter.core.services.account.AccountService;
import com.bluebell.planter.security.aspects.ValidateApiToken;
import com.bluebell.planter.security.constants.SecurityConstants;
import com.bluebell.planterannotationprocessor.api.examples.AccountApiExamples;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.bluebell.planter.core.validation.GenericValidator.validateJsonIntegrity;

/**
 * API Controller for {@link Account}
 *
 * @author Stephen Prizio
 * @version 0.0.9
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
            description = "Successfully retrieved all currencies.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = AccountApiExamples.CURRENCY_EXAMPLE)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - API token is missing or invalid",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = "{}")
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
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all account types.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardJsonResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized - API token is missing or invalid")
    @GetMapping("/account-types")
    public StandardJsonResponse<List<PairEntry>> getAccountTypes(final HttpServletRequest request) {
        return new StandardJsonResponse<>(true, Arrays.stream(AccountType.values()).map(at -> new PairEntry(at.getLabel().toUpperCase(), at.getLabel(), StringUtils.EMPTY)).toList(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link Broker}s
     *
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Get all system brokers", description = "Fetches all brokers that are currently supported in bluebell.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all brokers.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardJsonResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized - API token is missing or invalid")
    @GetMapping("/brokers")
    public StandardJsonResponse getBrokers(final HttpServletRequest request) {
        return new StandardJsonResponse(true, Arrays.stream(Broker.values()).map(b -> new PairEntry(b.getCode(), b.getName(), StringUtils.EMPTY)).toList(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link TradePlatform}s
     *
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Get all system trading platforms", description = "Fetches all trading platforms that are currently supported in bluebell. Trading platforms include examples like Metatrader 4 & cTraders.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all trading platforms.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardJsonResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized - API token is missing or invalid")
    @GetMapping("/trade-platforms")
    public StandardJsonResponse getTradePlatforms(final HttpServletRequest request) {
        return new StandardJsonResponse(true, Arrays.stream(TradePlatform.values()).map(tp -> new PairEntry(tp.getCode(), tp.getLabel(), StringUtils.EMPTY)).toList(), StringUtils.EMPTY);
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
    @ApiResponse(responseCode = "200", description = "Successfully retrieved account details.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardJsonResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized - API token is missing or invalid")
    @GetMapping("/get-details")
    public StandardJsonResponse getDetails(final @RequestParam("accountNumber") Long accountNumber, final HttpServletRequest request) {
        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account.map(value -> new StandardJsonResponse(true, this.accountService.getAccountDetails(value), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse(false, null, String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, accountNumber)));
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
    @ApiResponse(responseCode = "200", description = "Successfully created a new account.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardJsonResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized - API token is missing or invalid")
    @PostMapping("/create-account")
    public StandardJsonResponse postCreateNewAccount(final HttpServletRequest request, final @RequestBody Map<String, Object> requestBody) {
        validateJsonIntegrity(requestBody, List.of(ACCOUNT), "json did not contain of the required keys : %s", List.of(ACCOUNT));
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        return new StandardJsonResponse(true, this.accountDTOConverter.convert(this.accountService.createNewAccount(requestBody, user)), StringUtils.EMPTY);
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
    @ApiResponse(responseCode = "200", description = "Successfully updated the account.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardJsonResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized - API token is missing or invalid")
    @PutMapping("/update-account")
    public StandardJsonResponse putUpdateAccount(final @RequestParam("accountNumber") long accountNumber, final HttpServletRequest request, final @RequestBody Map<String, Object> requestBody) {
        validateJsonIntegrity(requestBody, List.of(ACCOUNT), "json did not contain of the required keys : %s", List.of(ACCOUNT));
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account.map(value -> new StandardJsonResponse(true, this.accountDTOConverter.convert(this.accountService.updateAccount(value, requestBody, user)), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse(false, null, String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, accountNumber)));
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
    @ApiResponse(responseCode = "200", description = "Successfully deleted the account.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardJsonResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized - API token is missing or invalid")
    @DeleteMapping("/delete-account")
    public StandardJsonResponse deleteAccount(final @RequestParam("accountNumber") long accountNumber, final HttpServletRequest request) {
        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account.map(value -> new StandardJsonResponse(this.accountService.deleteAccount(account.get()), null, StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse(false, null, String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, accountNumber)));
    }
}

