package com.bluebell.planter.api.controllers;

import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.exceptions.account.InvalidAccountNumberException;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.security.User;

import static com.bluebell.planter.core.validation.GenericValidator.validateLocalDateFormat;
import static com.bluebell.planter.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Parent-level controller providing common functionality
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public abstract class AbstractApiController {

    /**
     * Basic data integrity validation
     *
     * @param date date of format yyyy-MM-dd
     */
    public void validate(final String date) {
        validateLocalDateFormat(date, CoreConstants.DATE_FORMAT, CoreConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, date, CoreConstants.DATE_FORMAT);
    }

    /**
     * Basic data integrity validation
     *
     * @param start start date of format yyyy-MM-dd
     * @param end   end date of format yyyy-MM-dd
     */
    public void validate(final String start, final String end) {
        validateLocalDateFormat(start, CoreConstants.DATE_FORMAT, CoreConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, start, CoreConstants.DATE_FORMAT);
        validateLocalDateFormat(end, CoreConstants.DATE_FORMAT, CoreConstants.Validation.DateTime.END_DATE_INVALID_FORMAT, end, CoreConstants.DATE_FORMAT);
    }

    /**
     * Obtains the {@link Account} matching the given account number from the {@link Account}s associated to the given {@link User}
     *
     * @param user          {@link User}
     * @param accountNumber account number
     * @return {@link Account}
     */
    public Account getAccountForId(final User user, final long accountNumber) {
        validateParameterIsNotNull(user, CoreConstants.Validation.Security.User.USER_CANNOT_BE_NULL);
        return user.getAccounts().stream().filter(acc -> acc.getAccountNumber() == accountNumber).findFirst().orElseThrow(() -> new InvalidAccountNumberException("The given account number did not match and user accounts"));
    }
}
