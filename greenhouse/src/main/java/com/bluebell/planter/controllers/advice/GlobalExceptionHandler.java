package com.bluebell.planter.controllers.advice;

import com.bluebell.planter.constants.ApiConstants;
import com.bluebell.planter.exceptions.InvalidEnumException;
import com.bluebell.platform.exceptions.calculator.UnexpectedNegativeValueException;
import com.bluebell.platform.exceptions.calculator.UnexpectedZeroValueException;
import com.bluebell.platform.exceptions.system.GenericSystemException;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.radicle.exceptions.system.EntityCreationException;
import com.bluebell.radicle.exceptions.system.EntityModificationException;
import com.bluebell.radicle.exceptions.system.NoResultFoundException;
import com.bluebell.radicle.exceptions.system.NonUniqueItemFoundException;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.exceptions.validation.JsonMissingPropertyException;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.importing.exceptions.FileExtensionNotSupportedException;
import com.bluebell.radicle.importing.exceptions.TradeImportFailureException;
import com.bluebell.radicle.integration.exceptions.IntegrationException;
import com.bluebell.radicle.security.exceptions.InvalidApiTokenException;
import com.bluebell.radicle.security.exceptions.NoValidUserForTokenException;
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
 * @version 0.1.1
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
    public StandardJsonResponse<String> handleClientError(final Exception exception) {
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
    @ExceptionHandler(value = {
            EntityCreationException.class,
            EntityModificationException.class,
            FileNotFoundException.class,
            GenericSystemException.class,
            IllegalArgumentException.class,
            IntegrationException.class,
            SQLSyntaxErrorException.class,
            TradeImportFailureException.class
    })
    public StandardJsonResponse<String> handleServerError(final Exception exception) {
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
    private StandardJsonResponse<String> generateResponse(final String message, final String internalMessage) {
        return StandardJsonResponse.
                <String>builder()
                .success(false)
                .message(message)
                .internalMessage(internalMessage)
                .build();
    }
}