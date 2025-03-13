package com.bluebell.radicle.scheduled;

import com.bluebell.platform.enums.job.JobType;

/**
 * Defines a scheduled job that will run at periodic intervals
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
public interface GenericScheduledJob<T extends Enum<JobType>> {

    /**
     * Executes the scheduled job
     */
    void execute();

    /**
     * Returns the type of job. This method id introduced in order to enforce the appropriate {@link JobType}s and their existence
     *
     * @return {@link T}
     */
    T getJobType();
}
