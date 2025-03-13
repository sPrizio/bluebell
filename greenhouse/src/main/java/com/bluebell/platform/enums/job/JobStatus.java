package com.bluebell.platform.enums.job;

import com.bluebell.platform.models.core.entities.job.impl.Job;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Enum representing various states that a {@link Job} can be in
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
@Schema(title = "JobStatus", name = "JobStatus", description = "The various states that a Job can be in.")
public enum JobStatus {
    NOT_STARTED("not_started", "Not Started"),
    IN_PROGRESS("in_progress", "In Progress"),
    COMPLETED("completed", "Completed"),
    FAILED("failed", "Failed");

    private final String code;

    private final String label;

    JobStatus(final String code, final String label) {
        this.code = code;
        this.label = label;
    }


    //  METHODS

    /**
     * Get enum by code
     *
     * @param code input code
     * @return {@link JobStatus}
     */
    public static JobStatus getByCode(final String code) {
        return switch (code) {
            case "NOT_STARTED" -> NOT_STARTED;
            case "IN_PROGRESS" -> IN_PROGRESS;
            case "COMPLETED" -> COMPLETED;
            case "FAILED" -> FAILED;
            default -> null;
        };
    }
}
