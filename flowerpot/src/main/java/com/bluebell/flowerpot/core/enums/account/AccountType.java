package com.bluebell.flowerpot.core.enums.account;

import lombok.Getter;

/**
 * Enum representing the type of account. Here type of account represents the type of securities being traded
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Getter
public enum AccountType {
    SHARES("Shares"),
    OPTIONS("Options"),
    CFD("CFD"),
    FOREX("Forex"),
    DEMO("Demo");

    private final String label;

    AccountType(final String label) {
        this.label = label;
    }
}
