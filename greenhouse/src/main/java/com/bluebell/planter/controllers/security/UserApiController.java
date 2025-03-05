package com.bluebell.planter.controllers.security;

import java.util.*;
import java.util.stream.Collectors;

import static com.bluebell.radicle.validation.GenericValidator.validateJsonIntegrity;

import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.planter.converters.security.UserDTOConverter;
import com.bluebell.planter.converters.transaction.TransactionDTOConverter;
import com.bluebell.platform.enums.account.Currency;
import com.bluebell.platform.enums.system.Country;
import com.bluebell.platform.enums.system.Language;
import com.bluebell.platform.enums.system.PhoneType;
import com.bluebell.platform.models.api.dto.security.UserDTO;
import com.bluebell.platform.models.api.dto.transaction.TransactionDTO;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.security.constants.SecurityConstants;
import com.bluebell.radicle.services.security.UserService;
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
 * API controller for {@link User}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/user")
@Tag(name = "User", description = "Handles endpoints & operations related to user information.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class UserApiController extends AbstractApiController {

    private static final List<String> REQUIRED_JSON_VALUES = List.of("user");

    @Resource(name = "transactionDTOConverter")
    private TransactionDTOConverter transactionDTOConverter;

    @Resource(name = "userDTOConverter")
    private UserDTOConverter userDTOConverter;

    @Resource(name = "userService")
    private UserService userService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link User} with the given username
     *
     * @param username username
     * @param request  {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains the user for the given username", description = "Returns user information for the given username.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the user for the given username",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "User not found for username <bad_username>")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the user for the given username.",
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
    public StandardJsonResponse<UserDTO> getUser(final @RequestParam("username") String username, final HttpServletRequest request) {
        final Optional<User> user = this.userService.findUserByUsername(username);
        return user.map(value -> new StandardJsonResponse<>(true, this.userDTOConverter.convert(value), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse<>(false, null, String.format("No user found for username %s", username)));
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all of the {@link Country} codes
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains the supported country codes", description = "Returns a sorted list of country codes supported by bluebell.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the list of supported country codes.",
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
    @GetMapping("/country-codes")
    public StandardJsonResponse<TreeSet<String>> getCountryCodes() {
        return new StandardJsonResponse<>(
                true,
                Arrays.stream(Country.values())
                        .map(Country::getPhoneCode)
                        .collect(Collectors.toCollection(TreeSet::new)),
                StringUtils.EMPTY
        );
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all of the {@link PhoneType}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains the supported phone types", description = "Returns a list of phone types supported by bluebell.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the list of supported phone types.",
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
    @GetMapping("/phone-types")
    public StandardJsonResponse<PhoneType[]> getPhoneTypes() {
        return new StandardJsonResponse<>(true, PhoneType.values(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all of the {@link Currency}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains the supported currency codes", description = "Returns a list of currency codes supported by bluebell.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the list of supported currency codes.",
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
    @GetMapping("/currencies")
    public StandardJsonResponse<TreeSet<String>> getCurrencies() {
        return new StandardJsonResponse<>(true, Arrays.stream(Currency.values()).map(Currency::getIsoCode).collect(Collectors.toCollection(TreeSet::new)), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all of the {@link Country}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains the supported countries", description = "Returns a list of countries supported by bluebell.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the list of supported countries.",
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
    @GetMapping("/countries")
    public StandardJsonResponse<Country[]> getCountries() {
        return new StandardJsonResponse<>(true, Country.values(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all of the {@link Language}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains the supported languages", description = "Returns a list of languages supported by bluebell.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the list of supported languages.",
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
    @GetMapping("/languages")
    public StandardJsonResponse<Language[]> getLanguages() {
        return new StandardJsonResponse<>(true, Language.values(), StringUtils.EMPTY);
    }

    /**
     * Returns a list of the recent transactions for all of the {@link User}'s {@link Account}s
     *
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains the user's recent transactions for all accounts", description = "Returns a list of recent transactions, included from every user account.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the list of recent transactions.",
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
    @GetMapping("/recent-transactions")
    public StandardJsonResponse<List<TransactionDTO>> getRecentTransactions(final HttpServletRequest request) {
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        return new StandardJsonResponse<>(true, this.transactionDTOConverter.convertAll(user.getAccounts().stream().map(Account::getTransactions).filter(Objects::nonNull).flatMap(List::stream).filter(Objects::nonNull).sorted(Comparator.comparing(Transaction::getTransactionDate)).limit(5).toList()), StringUtils.EMPTY);
    }


    //  ----------------- POST REQUESTS -----------------

    /**
     * Creates a new {@link User}
     *
     * @param data json data
     * @return {@link StandardJsonResponse}
     */
    @Operation(summary = "Creates a new user", description = "Creates a new user in bluebell")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully creates a new user.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @PostMapping("/create")
    public StandardJsonResponse<UserDTO> postCreateUser(final @RequestBody Map<String, Object> data) {
        validateJsonIntegrity(data, REQUIRED_JSON_VALUES, "json did not contain of the required keys : %s", REQUIRED_JSON_VALUES.toString());
        return new StandardJsonResponse<>(true, this.userDTOConverter.convert(this.userService.createUser(data)), StringUtils.EMPTY);
    }


    //  ----------------- PUT REQUESTS -----------------

    /**
     * Updates an existing {@link User}
     *
     * @param request {@link HttpServletRequest}
     * @param data    json data
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Updates an existing user", description = "Updates an existing user.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully updates the user.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the user for the given username",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "User not found for username <bad_username>")
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
    @PutMapping("/update")
    public StandardJsonResponse<UserDTO> putUpdateUser(final HttpServletRequest request, final @RequestBody Map<String, Object> data) {
        validateJsonIntegrity(data, REQUIRED_JSON_VALUES, "json did not contain of the required keys : %s", REQUIRED_JSON_VALUES.toString());
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        return new StandardJsonResponse<>(true, this.userDTOConverter.convert(this.userService.updateUser(user, data)), StringUtils.EMPTY);
    }
}