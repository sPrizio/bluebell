package com.bluebell.radicle.services;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.core.entities.account.Account;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

import static com.bluebell.radicle.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Parent-level class for common entity service-layer operations
 *
 * @author Stephen Prizio
 * @version 0.2.5
 */
public abstract class AbstractEntityService {

    /**
     * Validates standard trade lookup parameters
     *
     * @param start start date
     * @param end end date
     * @param account {@link Account}
     * @param sort {@link Sort}
     */
    public void validateStandardParameters(final LocalDateTime start, final LocalDateTime end, final Account account, final Sort sort) {
        validateParameterIsNotNull(start, CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start, end, CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);
        validateParameterIsNotNull(sort, CorePlatformConstants.Validation.System.SORT_CANNOT_BE_NULL);
    }
}
