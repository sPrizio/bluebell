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
 * @version 0.0.7
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/account")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class AccountApiController extends AbstractApiController {

    private static final String ACCOUNT = "account";

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
    public StandardJsonResponse getCurrencies(final HttpServletRequest request) {
        return new StandardJsonResponse(true, Arrays.stream(Currency.values()).map(c -> new PairEntry(c.getIsoCode(), c.getLabel(), c.getSymbol())).toList(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link AccountType}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/account-types")
    public StandardJsonResponse getAccountTypes(final HttpServletRequest request) {
        return new StandardJsonResponse(true, Arrays.stream(AccountType.values()).map(at -> new PairEntry(at.getLabel().toUpperCase(), at.getLabel(), StringUtils.EMPTY)).toList(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link Broker}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/brokers")
    public StandardJsonResponse getBrokers(final HttpServletRequest request) {
        return new StandardJsonResponse(true, Arrays.stream(Broker.values()).map(b -> new PairEntry(b.getCode(), b.getName(), StringUtils.EMPTY)).toList(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link TradePlatform}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
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
    @GetMapping("/get-details")
    public StandardJsonResponse getDetails(final @RequestParam("accountNumber") Long accountNumber, final HttpServletRequest request) {
        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account.map(value -> new StandardJsonResponse(true, this.accountService.getAccountDetails(value), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse(false, null, String.format("No account was found for account number %d", accountNumber)));
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
    @PutMapping("/update-account")
    public StandardJsonResponse putUpdateAccount(final @RequestParam("accountNumber") long accountNumber, final HttpServletRequest request, final @RequestBody Map<String, Object> requestBody) {
        validateJsonIntegrity(requestBody, List.of(ACCOUNT), "json did not contain of the required keys : %s", List.of(ACCOUNT));
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account.map(value -> new StandardJsonResponse(true, this.accountDTOConverter.convert(this.accountService.updateAccount(value, requestBody, user)), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse(false, null, String.format("No account was found for account number %d", accountNumber)));
    }


    //  ----------------- DELETE REQUESTS -----------------

    @ValidateApiToken
    @DeleteMapping("/delete-account")
    public StandardJsonResponse deleteAccount(final @RequestParam("accountNumber") long accountNumber, final HttpServletRequest request) {
        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account.map(value -> new StandardJsonResponse(this.accountService.deleteAccount(account.get()), null, StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse(false, null, String.format("No account was found for account number %d", accountNumber)));
    }
}
