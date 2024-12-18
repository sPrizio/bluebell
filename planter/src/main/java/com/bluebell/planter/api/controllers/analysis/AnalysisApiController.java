package com.bluebell.planter.api.controllers.analysis;

import com.bluebell.planter.api.controllers.AbstractApiController;
import com.bluebell.planter.api.models.records.json.StandardJsonResponse;
import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.analysis.AnalysisFilter;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.nonentities.records.analysis.AnalysisResult;
import com.bluebell.planter.core.services.account.AccountService;
import com.bluebell.planter.core.services.analysis.AnalysisService;
import com.bluebell.planter.security.aspects.ValidateApiToken;
import com.bluebell.radicle.enums.RadicleTimeInterval;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.Optional;

/**
 * Controller for {@link AnalysisService}
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/analysis")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class AnalysisApiController extends AbstractApiController {

    @Resource(name = "accountService")
    private AccountService accountService;

    @Resource(name = "analysisService")
    private AnalysisService analysisService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing {@link java.util.List} of {@link AnalysisResult}s
     *
     * @param accountNumber account number
     * @param filter {@link AnalysisFilter}
     * @param isOpened trade opened or closed times
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/time-buckets")
    public StandardJsonResponse getTimBucketsAnalysis(
            final @RequestParam("accountNumber") long accountNumber,
            final @RequestParam("filter") String filter,
            final @RequestParam("isOpened") boolean isOpened,
            final HttpServletRequest request
    ) {

        if (AnalysisFilter.getAnalysisFilter(filter) == null) {
            return new StandardJsonResponse(false, null, String.format(CoreConstants.Validation.DataIntegrity.INVALID_FILTER, filter));
        }

        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account.map(value -> new StandardJsonResponse(true, this.analysisService.computeTimeBucketAnalysis(value, RadicleTimeInterval.THIRTY_MINUTE, AnalysisFilter.getAnalysisFilter(filter.toUpperCase()), isOpened), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse(true, null, CoreConstants.Validation.Account.ACCOUNT_NOT_FOUND));
    }

    /**
     * Returns a {@link StandardJsonResponse} containing {@link java.util.List} of {@link AnalysisResult}s
     *
     * @param accountNumber account number
     * @param filter {@link AnalysisFilter}
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/weekdays")
    public StandardJsonResponse getWeekdaysAnalysis(
            final @RequestParam("accountNumber") long accountNumber,
            final @RequestParam("filter") String filter,
            final HttpServletRequest request
    ) {

        if (AnalysisFilter.getAnalysisFilter(filter) == null) {
            return new StandardJsonResponse(false, null, String.format(CoreConstants.Validation.DataIntegrity.INVALID_FILTER, filter));
        }

        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account.map(value -> new StandardJsonResponse(true, this.analysisService.computeWeekdayAnalysis(value, AnalysisFilter.getAnalysisFilter(filter.toUpperCase())), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse(true, null, CoreConstants.Validation.Account.ACCOUNT_NOT_FOUND));
    }

    /**
     * Returns a {@link StandardJsonResponse} containing {@link java.util.List} of {@link AnalysisResult}s
     *
     * @param accountNumber account number
     * @param filter {@link AnalysisFilter}
     * @param weekday {@link DayOfWeek}
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/weekdays-time-buckets")
    public StandardJsonResponse getTimBucketsAnalysis(
            final @RequestParam("accountNumber") long accountNumber,
            final @RequestParam("filter") String filter,
            final @RequestParam("weekday") String weekday,
            final HttpServletRequest request
    ) {

        if (AnalysisFilter.getAnalysisFilter(filter) == null) {
            return new StandardJsonResponse(false, null, String.format(CoreConstants.Validation.DataIntegrity.INVALID_FILTER, filter));
        }

        if (!EnumUtils.isValidEnumIgnoreCase(DayOfWeek.class, weekday)) {
            return new StandardJsonResponse(false, null, String.format(CoreConstants.Validation.DataIntegrity.INVALID_WEEKDAY, weekday));
        }

        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account.map(value -> new StandardJsonResponse(true, this.analysisService.computeWeekdayTimeBucketAnalysis(value, DayOfWeek.valueOf(weekday.toUpperCase()), RadicleTimeInterval.THIRTY_MINUTE, AnalysisFilter.getAnalysisFilter(filter.toUpperCase())), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse(true, null, CoreConstants.Validation.Account.ACCOUNT_NOT_FOUND));
    }
}
