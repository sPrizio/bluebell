package com.bluebell.platform.enums.transaction;

import com.bluebell.platform.enums.GenericEnum;
import lombok.Getter;

/**
 * Enum representation types of transactions
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
public enum TransactionType implements GenericEnum<TransactionType> {
    DEPOSIT("DEPOSIT", "Deposit"),
    WITHDRAWAL("WITHDRAWAL", "Withdrawal"),;

    private final String code;

    private final String label;

    TransactionType(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
