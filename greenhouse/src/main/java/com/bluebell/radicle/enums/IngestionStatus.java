package com.bluebell.radicle.enums;

import com.bluebell.platform.enums.GenericEnum;
import lombok.Getter;

/**
 * Enum representing states of data ingestion
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@Getter
public enum IngestionStatus implements GenericEnum<IngestionStatus> {
    FAILED("FAILED", "Failed"),
    SUCCESS("SUCCESS", "Success"),
    SKIPPED("SKIPPED", "Skipped");

    private final String code;

    private final String label;

    IngestionStatus(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
