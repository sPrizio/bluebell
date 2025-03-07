package com.bluebell.planter.controllers;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.radicle.exceptions.account.InvalidAccountNumberException;

import static com.bluebell.radicle.validation.GenericValidator.validateLocalDateFormat;
import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;


/**
 * Parent-level controller providing common functionality
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
public abstract class AbstractApiController {

    /**
     * Basic data integrity validation
     *
     * @param date date of format yyyy-MM-dd
     */
    public void validate(final String date) {
        validateLocalDateFormat(date, CorePlatformConstants.DATE_FORMAT, CorePlatformConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, date, CorePlatformConstants.DATE_FORMAT);
    }

    /**
     * Basic data integrity validation
     *
     * @param start start date of format yyyy-MM-dd
     * @param end   end date of format yyyy-MM-dd
     */
    public void validate(final String start, final String end) {
        validateLocalDateFormat(start, CorePlatformConstants.DATE_FORMAT, CorePlatformConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, start, CorePlatformConstants.DATE_FORMAT);
        validateLocalDateFormat(end, CorePlatformConstants.DATE_FORMAT, CorePlatformConstants.Validation.DateTime.END_DATE_INVALID_FORMAT, end, CorePlatformConstants.DATE_FORMAT);
    }

    /**
     * Obtains the {@link Account} matching the given account number from the {@link Account}s associated to the given {@link User}
     *
     * @param user          {@link User}
     * @param accountNumber account number
     * @return {@link Account}
     */
    public Account getAccountForId(final User user, final long accountNumber) {
        validateParameterIsNotNull(user, CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);
        return user.getAccounts().stream().filter(acc -> acc.getAccountNumber() == accountNumber).findFirst().orElseThrow(() -> new InvalidAccountNumberException("The given account number did not match and user accounts"));
    }
}
