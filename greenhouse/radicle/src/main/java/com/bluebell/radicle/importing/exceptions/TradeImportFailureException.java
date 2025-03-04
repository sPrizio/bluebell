package com.bluebell.radicle.importing.exceptions;

/**
 * Exception for failures during the trade import process
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class TradeImportFailureException extends RuntimeException {

    public TradeImportFailureException(final String message) {
        super(message);
    }

    public TradeImportFailureException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
