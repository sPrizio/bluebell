package com.bluebell.planter.controllers.security;

import com.bluebell.planter.constants.ApiPaths;
import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.planter.converters.security.UserDTOConverter;
import com.bluebell.planter.converters.transaction.TransactionDTOConverter;
import com.bluebell.platform.enums.account.Currency;
import com.bluebell.platform.enums.system.Country;
import com.bluebell.platform.enums.system.Language;
import com.bluebell.platform.enums.system.PhoneType;
import com.bluebell.platform.models.api.dto.security.CreateUpdateUserDTO;
import com.bluebell.platform.models.api.dto.security.UserDTO;
import com.bluebell.platform.models.api.dto.transaction.TransactionDTO;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.security.constants.SecurityConstants;
import com.bluebell.radicle.services.security.UserService;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * API controller for {@link User}
 *
 * @author Stephen Prizio
 * @version 0.1.9
 */
@RestController
@RequestMapping("${bluebell.base.api.controller.endpoint}" + ApiPaths.User.BASE)
@Tag(name = "User", description = "Handles endpoints & operations related to user information.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class UserApiController extends AbstractApiController {

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
    @GetMapping(ApiPaths.User.GET)
    public StandardJsonResponse<UserDTO> getUser(
            @Parameter(name = "Username", description = "User's username", example = "test.test")
            final @RequestParam("username") String username,
            final HttpServletRequest request
    ) {
        final Optional<User> user = this.userService.findUserByUsername(username);
        return user
                .map(value -> StandardJsonResponse.<UserDTO>builder().success(true).data(this.userDTOConverter.convert(value)).build())
                .orElseGet(() -> StandardJsonResponse.<UserDTO>builder().success(false).message(String.format("No user found for username %s", username)).build());
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
    @GetMapping(ApiPaths.User.COUNTRY_CODES)
    public StandardJsonResponse<TreeSet<String>> getCountryCodes() {
        return StandardJsonResponse
                .<TreeSet<String>>builder()
                .success(true)
                .data(Arrays.stream(Country.values()).map(Country::getPhoneCode).collect(Collectors.toCollection(TreeSet::new)))
                .build();
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
    @GetMapping(ApiPaths.User.PHONE_TYPES)
    public StandardJsonResponse<PhoneType[]> getPhoneTypes() {
        return StandardJsonResponse
                .<PhoneType[]>builder()
                .success(true)
                .data(PhoneType.values())
                .build();
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
    @GetMapping(ApiPaths.User.CURRENCIES)
    public StandardJsonResponse<TreeSet<String>> getCurrencies() {
        return StandardJsonResponse
                .<TreeSet<String>>builder()
                .success(true)
                .data(Arrays.stream(Currency.values()).map(Currency::getIsoCode).collect(Collectors.toCollection(TreeSet::new)))
                .build();
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
    @GetMapping(ApiPaths.User.COUNTRIES)
    public StandardJsonResponse<Country[]> getCountries() {
        return StandardJsonResponse
                .<Country[]>builder()
                .success(true)
                .data(Country.values())
                .build();
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
    @GetMapping(ApiPaths.User.LANGUAGES)
    public StandardJsonResponse<Language[]> getLanguages() {
        return StandardJsonResponse
                .<Language[]>builder()
                .success(true)
                .data(Language.values())
                .build();
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
    @GetMapping(ApiPaths.User.RECENT_TRANSACTIONS)
    public StandardJsonResponse<List<TransactionDTO>> getRecentTransactions(final HttpServletRequest request) {
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        return StandardJsonResponse
                .<List<TransactionDTO>>builder()
                .success(true)
                .data(this.transactionDTOConverter.convertAll(user.getActivePortfolios().stream().map(Portfolio::getActiveAccounts).flatMap(List::stream).map(Account::getTransactions).filter(Objects::nonNull).flatMap(List::stream).filter(Objects::nonNull).sorted(Comparator.comparing(Transaction::getTransactionDate)).limit(5).toList()))
                .build();
    }


    //  ----------------- POST REQUESTS -----------------

    /**
     * Creates a new {@link User}
     *
     * @param data {@link CreateUpdateUserDTO}
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
    @PostMapping(ApiPaths.User.CREATE)
    public StandardJsonResponse<UserDTO> postCreateUser(
            @Parameter(name = "User Payload", description = "Request body for creating and updating users")
            final @RequestBody CreateUpdateUserDTO data
    ) {
        if (data == null || StringUtils.isEmpty(data.username())) {
            throw new MissingRequiredDataException("The required data for creating a User was null or empty");
        }

        return StandardJsonResponse
                .<UserDTO>builder()
                .success(true)
                .data(this.userDTOConverter.convert(this.userService.createUser(data)))
                .build();
    }


    //  ----------------- PUT REQUESTS -----------------

    /**
     * Updates an existing {@link User}
     *
     * @param request {@link HttpServletRequest}
     * @param data    {@link CreateUpdateUserDTO}
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
    @PutMapping(ApiPaths.User.UPDATE)
    public StandardJsonResponse<UserDTO> putUpdateUser(
            @Parameter(name = "User Payload", description = "Request body for creating and updating users")
            final @RequestBody CreateUpdateUserDTO data,
            final HttpServletRequest request
    ) {
        if (data == null || StringUtils.isEmpty(data.username())) {
            throw new MissingRequiredDataException("The required data for creating a User was null or empty");
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        return StandardJsonResponse
                .<UserDTO>builder()
                .success(true)
                .data(this.userDTOConverter.convert(this.userService.updateUser(user, data)))
                .build();
    }
}