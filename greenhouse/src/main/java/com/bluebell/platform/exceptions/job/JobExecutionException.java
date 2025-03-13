package com.bluebell.platform.exceptions.job;

import com.bluebell.platform.models.core.entities.job.impl.Job;

/**
 * Exception when a {@link Job} is actioned without an execution time (job hasn't start yet)
 *
 * @author Stephen Prizoi
 * @version 0.1.1
 */
public class JobExecutionException extends RuntimeException {

    public JobExecutionException(final String message) {
        super(message);
    }

    public JobExecutionException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
