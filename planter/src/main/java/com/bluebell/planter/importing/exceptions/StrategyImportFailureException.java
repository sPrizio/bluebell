package com.bluebell.planter.importing.exceptions;

/**
 * Exception for failures during the strategy import process
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
public class StrategyImportFailureException extends RuntimeException {

    public StrategyImportFailureException(final String message) {
        super(message);
    }

    public StrategyImportFailureException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}