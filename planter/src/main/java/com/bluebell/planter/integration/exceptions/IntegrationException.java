package com.bluebell.planter.integration.exceptions;

/**
 * Custom exception for issues during external RESTful calls
 *
 * @author Stephen Prizio
 * @version 0.0.4
 */
public class IntegrationException extends RuntimeException {

    public IntegrationException(final String message) {
        super(message);
    }

    public IntegrationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
