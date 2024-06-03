package com.bluebell.flowerpot.api.controllers.advice;

import com.bluebell.flowerpot.api.constants.ApiConstants;
import com.bluebell.flowerpot.api.exceptions.InvalidEnumException;
import com.bluebell.flowerpot.api.models.records.json.StandardJsonResponse;
import com.bluebell.flowerpot.core.exceptions.calculator.UnexpectedNegativeValueException;
import com.bluebell.flowerpot.core.exceptions.calculator.UnexpectedZeroValueException;
import com.bluebell.flowerpot.core.exceptions.system.EntityCreationException;
import com.bluebell.flowerpot.core.exceptions.system.EntityModificationException;
import com.bluebell.flowerpot.core.exceptions.system.GenericSystemException;
import com.bluebell.flowerpot.core.exceptions.validation.*;
import com.bluebell.flowerpot.importing.exceptions.FileExtensionNotSupportedException;
import com.bluebell.flowerpot.importing.exceptions.TradeImportFailureException;
import com.bluebell.flowerpot.security.exceptions.InvalidApiTokenException;
import com.bluebell.flowerpot.security.exceptions.NoValidUserForTokenException;
import org.hibernate.boot.beanvalidation.IntegrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.sql.SQLSyntaxErrorException;
import java.time.DateTimeException;

/**
 * Handles the exceptions thrown by the application
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    //  METHODS

    /**
     * Method that handles client-side errors
     *
     * @param exception {@link Exception}
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @ExceptionHandler(value = {
            DateTimeException.class,
            FileExtensionNotSupportedException.class,
            IllegalParameterException.class,
            InvalidEnumException.class,
            InvalidApiTokenException.class,
            FileExtensionNotSupportedException.class,
            JsonMissingPropertyException.class,
            MissingRequiredDataException.class,
            NonUniqueItemFoundException.class,
            NoResultFoundException.class,
            NoValidUserForTokenException.class,
            UnexpectedNegativeValueException.class,
            UnexpectedZeroValueException.class,
            UnsupportedOperationException.class
    })
    public StandardJsonResponse handleClientError(final Exception exception) {
        LOGGER.error("Bad Request by the client. Please try again: ", exception);
        return generateResponse(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE, exception.getMessage());
    }

    /**
     * Method that handles server-side errors
     *
     * @param exception {@link Exception}
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @ExceptionHandler({
            EntityCreationException.class,
            EntityModificationException.class,
            FileNotFoundException.class,
            GenericSystemException.class,
            IllegalArgumentException.class,
            IntegrationException.class,
            SQLSyntaxErrorException.class,
            TradeImportFailureException.class
    })
    public StandardJsonResponse handleServerError(final Exception exception) {
        LOGGER.error("An internal server error occurred. ", exception);
        return generateResponse(ApiConstants.SERVER_ERROR_DEFAULT_MESSAGE, exception.getMessage());
    }


    //  HELPERS

    /**
     * Generates a {@link StandardJsonResponse}
     *
     * @param message         message
     * @param internalMessage internal reporting message
     */
    private StandardJsonResponse generateResponse(final String message, final String internalMessage) {
        return new StandardJsonResponse(false, null, message, internalMessage);
    }
}
