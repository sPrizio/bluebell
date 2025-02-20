package com.bluebell.planter.api.controllers.security;

import com.bluebell.planter.api.controllers.AbstractApiController;
import com.bluebell.planter.api.converters.security.UserDTOConverter;
import com.bluebell.planter.api.models.records.json.StandardJsonResponse;
import com.bluebell.planter.core.enums.account.Currency;
import com.bluebell.planter.core.enums.system.Country;
import com.bluebell.planter.core.enums.system.Language;
import com.bluebell.planter.core.enums.system.PhoneType;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.entities.security.User;
import com.bluebell.planter.core.models.entities.transaction.Transaction;
import com.bluebell.planter.core.services.security.UserService;
import com.bluebell.planter.security.aspects.ValidateApiToken;
import com.bluebell.planter.security.constants.SecurityConstants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.bluebell.planter.core.validation.GenericValidator.validateJsonIntegrity;


/**
 * API controller for {@link User}
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/user")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class UserApiController extends AbstractApiController {

    private static final List<String> REQUIRED_JSON_VALUES = List.of("user");

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
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/get")
    public StandardJsonResponse getUser(final @RequestParam String username, final HttpServletRequest request) {
        final Optional<User> user = this.userService.findUserByUsername(username);
        return user.map(value -> new StandardJsonResponse(true, this.userDTOConverter.convert(value), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse(false, null, String.format("No user found for username %s", username)));
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all of the {@link Country} codes
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/country-codes")
    public StandardJsonResponse getCountryCodes() {
        return new StandardJsonResponse(
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
    @GetMapping("/phone-types")
    public StandardJsonResponse getPhoneTypes() {
        return new StandardJsonResponse(true, PhoneType.values(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all of the {@link Currency}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/currencies")
    public StandardJsonResponse getCurrencies() {
        return new StandardJsonResponse(true, Arrays.stream(Currency.values()).map(Currency::getIsoCode).collect(Collectors.toCollection(TreeSet::new)), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all of the {@link Country}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/countries")
    public StandardJsonResponse getCountries() {
        return new StandardJsonResponse(true, Country.values(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all of the {@link Language}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/languages")
    public StandardJsonResponse getLanguages() {
        return new StandardJsonResponse(true, Language.values(), StringUtils.EMPTY);
    }

    @ValidateApiToken
    @GetMapping("/recent-transactions")
    public StandardJsonResponse getRecentTransactions(final HttpServletRequest request) {
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        return new StandardJsonResponse(true, user.getAccounts().stream().map(Account::getTransactions).flatMap(List::stream).sorted(Comparator.comparing(Transaction::getTransactionDate)).limit(5).toList(), StringUtils.EMPTY);
    }


    //  ----------------- POST REQUESTS -----------------

    /**
     * Creates a new {@link User}
     *
     * @param data json data
     * @return {@link StandardJsonResponse}
     */
    @PostMapping("/create")
    public StandardJsonResponse postCreateUser(final @RequestBody Map<String, Object> data) {
        validateJsonIntegrity(data, REQUIRED_JSON_VALUES, "json did not contain of the required keys : %s", REQUIRED_JSON_VALUES.toString());
        return new StandardJsonResponse(true, this.userDTOConverter.convert(this.userService.createUser(data)), StringUtils.EMPTY);
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
    @PutMapping("/update")
    public StandardJsonResponse putUpdateUser(final HttpServletRequest request, final @RequestBody Map<String, Object> data) {
        validateJsonIntegrity(data, REQUIRED_JSON_VALUES, "json did not contain of the required keys : %s", REQUIRED_JSON_VALUES.toString());
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        return new StandardJsonResponse(true, this.userDTOConverter.convert(this.userService.updateUser(user, data)), StringUtils.EMPTY);
    }
}
