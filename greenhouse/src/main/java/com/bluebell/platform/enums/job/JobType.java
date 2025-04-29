package com.bluebell.platform.enums.job;

import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Enumeration representing different types of {@link Job}s that can be performed
 *
 * @author Stephen Prizio
 * @version 0.1.8
 */
@Getter
@Schema(title = "JobType", name = "JobType", description = "Types of Jobs implemented in bluebell.")
public enum JobType implements GenericEnum<JobType> {
    FETCH_MARKET_NEWS("FETCH_MARKET_NEWS", "Fetch Market News Job"),
    CLEANUP_STALE_JOBS("CLEANUP_STALE_JOBS", "Cleanup Stale Jobs"),
    INGEST_MARKET_DATA("INGEST_MARKET_DATA", "Ingest Market Data Job"),
    CLEANUP_INGESTED_DATA("CLEANUP_INGESTED_DATA", "Cleanup Ingested Data Job"),
    FETCH_MARKET_DATA_FROM_FIRST_DATA("FETCH_MARKET_DATA_FROM_FIRST_DATA", "Fetch Market Data from FirstData Job"),
    FETCH_MARKET_DATA_FROM_MT4("FETCH_MARKET_DATA_FROM_MT4", "Fetch Market Data From MT4 Job"),
    PING_MIDDLEWARE("PING_MIDDLEWARE", "Ping Middleware Job"),
    INVALIDATE_STALE_ACCOUNTS("INVALIDATE_STALE_ACCOUNTS", "Invalidate Stale Accounts"),;

    private final String code;

    private final String label;

    JobType(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
