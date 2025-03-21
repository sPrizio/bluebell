package com.bluebell.platform.enums.job;

import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Enumeration representing different types of {@link Job}s that can be performed
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
@Schema(title = "JobType", name = "JobType", description = "Types of Jobs implemented in bluebell.")
public enum JobType implements GenericEnum<JobType> {
    FETCH_MARKET_NEWS("FETCH_MARKET_NEWS", "Fetch Market News Job"),
    CLEANUP_STALE_JOBS("CLEANUP_STALE_JOBS", "Cleanup Stale Jobs");

    private final String code;

    private final String label;

    JobType(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
