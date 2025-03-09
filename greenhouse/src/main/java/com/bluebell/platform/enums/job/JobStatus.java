package com.bluebell.platform.enums.job;

import com.bluebell.platform.models.core.entities.job.impl.Job;

/**
 * Enum representing various states that a {@link Job} can be in
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
public enum JobStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    FAILED
}
