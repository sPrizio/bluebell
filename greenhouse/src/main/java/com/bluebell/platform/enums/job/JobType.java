package com.bluebell.platform.enums.job;

import com.bluebell.platform.models.core.entities.job.impl.Job;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Enumeration representing different types of {@link Job}s that can be performed
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
@Schema(title = "JobType", name = "JobType", description = "Types of Jobs implemented in bluebell.")
public enum JobType {
    FETCH_MARKET_NEWS("fetch_market_news", "Fetch Market News Job");

    private final String code;

    private final String label;

    JobType(final String code, final String label) {
        this.code = code;
        this.label = label;
    }

    //  METHODS

    /**
     * Get enum by code
     *
     * @param code input code
     * @return {@link JobType}
     */
    public static JobType getByCode(final String code) {
        return switch (code) {
            case "fetch_market_news" -> FETCH_MARKET_NEWS;
            default -> null;
        };
    }
}
