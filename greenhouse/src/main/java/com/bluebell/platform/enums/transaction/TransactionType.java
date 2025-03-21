package com.bluebell.platform.enums.transaction;

import lombok.Getter;

/**
 * Enum representation types of transactions
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
public enum TransactionType {
    DEPOSIT("DEPOSIT", "Deposit"),
    WITHDRAWAL("WITHDRAWAL", "Withdrawal"),;

    private final String code;

    private final String label;

    TransactionType(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
