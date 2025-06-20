package com.bluebell.platform.enums.job;

import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Enum representing various states that a {@link Job} can be in
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Getter
@Schema(title = "JobStatus", name = "JobStatus", description = "The various states that a Job can be in.")
public enum JobStatus implements GenericEnum<JobStatus> {
    NOT_STARTED("NOT_STARTED", "Not Started"),
    IN_PROGRESS("IN_PROGRESS", "In Progress"),
    COMPLETED("COMPLETED", "Completed"),
    FAILED("FAILED", "Failed"),
    SKIPPED("SKIPPED", "Skipped");

    private final String code;

    private final String label;

    JobStatus(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
