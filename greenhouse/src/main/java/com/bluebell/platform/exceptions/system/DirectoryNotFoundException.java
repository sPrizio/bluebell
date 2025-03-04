package com.bluebell.platform.exceptions.system;

/**
 * An exception that is thrown when directory's are not found
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
public class DirectoryNotFoundException extends RuntimeException {

    public DirectoryNotFoundException(final String message) {
        super(message);
    }

    public DirectoryNotFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
