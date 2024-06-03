package com.bluebell.flowerpot.api.controllers.account;

import com.bluebell.flowerpot.api.controllers.AbstractApiController;
import com.bluebell.flowerpot.api.converters.account.AccountDTOConverter;
import com.bluebell.flowerpot.api.models.records.json.StandardJsonResponse;
import com.bluebell.flowerpot.api.models.records.platform.PairEntry;
import com.bluebell.flowerpot.core.enums.account.AccountType;
import com.bluebell.flowerpot.core.enums.account.Broker;
import com.bluebell.flowerpot.core.enums.account.Currency;
import com.bluebell.flowerpot.core.enums.trade.platform.TradePlatform;
import com.bluebell.flowerpot.core.models.entities.account.Account;
import com.bluebell.flowerpot.core.models.entities.security.User;
import com.bluebell.flowerpot.core.services.account.AccountService;
import com.bluebell.flowerpot.security.aspects.ValidateApiToken;
import com.bluebell.flowerpot.security.constants.SecurityConstants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.bluebell.flowerpot.core.validation.GenericValidator.validateJsonIntegrity;


/**
 * API Controller for {@link Account}
 *
 * @author Stephen Prizio
 * @version 0.0.3
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/account")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class AccountApiController extends AbstractApiController {

    @Resource(name = "accountDTOConverter")
    private AccountDTOConverter accountDTOConverter;

    @Resource(name = "accountService")
    private AccountService accountService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link Currency}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/currencies")
    public StandardJsonResponse getCurrencies() {
        return new StandardJsonResponse(true, Arrays.stream(Currency.values()).map(c -> new PairEntry(c.getIsoCode(), c.getLabel(), c.getSymbol())).toList(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link AccountType}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/account-types")
    public StandardJsonResponse getAccountTypes() {
        return new StandardJsonResponse(true, Arrays.stream(AccountType.values()).map(at -> new PairEntry(at.getLabel().toUpperCase(), at.getLabel(), StringUtils.EMPTY)).toList(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link Broker}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/brokers")
    public StandardJsonResponse getBrokers() {
        return new StandardJsonResponse(true, Arrays.stream(Broker.values()).map(b -> new PairEntry(b.getCode(), b.getName(), StringUtils.EMPTY)).toList(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link TradePlatform}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/trade-platforms")
    public StandardJsonResponse getTradePlatforms() {
        return new StandardJsonResponse(true, Arrays.stream(TradePlatform.values()).map(tp -> new PairEntry(tp.getCode(), tp.getLabel(), StringUtils.EMPTY)).toList(), StringUtils.EMPTY);
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
    @PostMapping("/create-account")
    public StandardJsonResponse postCreateNewAccount(final HttpServletRequest request, final @RequestBody Map<String, Object> requestBody) {
        validateJsonIntegrity(requestBody, List.of("account"), "json did not contain of the required keys : %s", List.of("account"));
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        return new StandardJsonResponse(true, this.accountDTOConverter.convert(this.accountService.createNewAccount(requestBody, user)), StringUtils.EMPTY);
    }
}
