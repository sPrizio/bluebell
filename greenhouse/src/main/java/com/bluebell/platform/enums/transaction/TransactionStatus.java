package com.bluebell.platform.enums.transaction;

import com.bluebell.platform.enums.GenericEnum;
import lombok.Getter;

/**
 * Enum representing the status that a transaction can be in
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
public enum TransactionStatus implements GenericEnum<TransactionStatus> {
    FAILED("FAILED", "Failed"),
    IN_PROGRESS("IN_PROGRESS", "In Progress"),
    PENDING("PENDING", "Pending"),
    COMPLETED("COMPLETED", "Completed");

    private final String code;

    private final String label;

    TransactionStatus(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
