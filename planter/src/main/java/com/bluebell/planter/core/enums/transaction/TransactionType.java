package com.bluebell.planter.core.enums.transaction;

import lombok.Getter;

/**
 * Enum representation types of transactions
 *
 * @author Stephen Prizio
 * @version 0.0.7
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
