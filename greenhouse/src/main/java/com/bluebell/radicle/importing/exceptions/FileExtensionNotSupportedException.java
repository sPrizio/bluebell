package com.bluebell.radicle.importing.exceptions;

/**
 * Exception for file extensions that are not supported by the system
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class FileExtensionNotSupportedException extends RuntimeException {

    public FileExtensionNotSupportedException(final String message) {
        super(message);
    }

    public FileExtensionNotSupportedException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
