package com.bluebell.flowerpot.api.controllers.security;

import com.bluebell.flowerpot.api.controllers.AbstractApiController;
import com.bluebell.flowerpot.api.converters.security.UserDTOConverter;
import com.bluebell.flowerpot.api.models.records.json.StandardJsonResponse;
import com.bluebell.flowerpot.core.enums.account.Currency;
import com.bluebell.flowerpot.core.enums.system.Country;
import com.bluebell.flowerpot.core.enums.system.Language;
import com.bluebell.flowerpot.core.enums.system.PhoneType;
import com.bluebell.flowerpot.core.models.entities.security.User;
import com.bluebell.flowerpot.core.services.security.UserService;
import com.bluebell.flowerpot.security.aspects.ValidateApiToken;
import com.bluebell.flowerpot.security.constants.SecurityConstants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.bluebell.flowerpot.core.validation.GenericValidator.validateJsonIntegrity;


/**
 * API controller for {@link User}
 *
 * @author Stephen Prizio
 * @version 0.0.2
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
