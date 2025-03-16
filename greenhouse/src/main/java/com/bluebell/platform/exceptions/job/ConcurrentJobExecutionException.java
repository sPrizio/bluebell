package com.bluebell.platform.exceptions.job;

import com.bluebell.platform.models.core.entities.job.impl.Job;

/**
 * Exception when a {@link Job} is executed while the same job type is still running
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
public class ConcurrentJobExecutionException extends RuntimeException {

    public ConcurrentJobExecutionException(final String message) {
        super(message);
    }

    public ConcurrentJobExecutionException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
