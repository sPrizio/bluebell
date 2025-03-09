package com.bluebell.radicle.exceptions.job;

import com.bluebell.platform.models.core.entities.job.Job;

/**
 * Exception when a {@link Job} is actioned without an execution time (job hasn't start yet)
 *
 * @author Stephen Prizoi
 * @version 0.1.1
 */
public class JobExecutionTimeException extends RuntimeException {

    public JobExecutionTimeException(final String message) {
        super(message);
    }

    public JobExecutionTimeException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
