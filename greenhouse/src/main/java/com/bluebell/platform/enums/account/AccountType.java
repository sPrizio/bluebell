package com.bluebell.platform.enums.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Enum representing the type of account. Here type of account represents the type of securities being traded
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
@Schema(title = "AccountType", name = "AccountType Enum", description = "List of AccountTypes supported in bluebell.")
public enum AccountType {
    SHARES("SHARES", "Shares"),
    OPTIONS("OPTIONS", "Options"),
    CFD("CFD", "CFD"),
    FOREX("FOREX", "Forex"),
    DEMO("DEMO", "Demo");

    private final String code;

    private final String label;

    AccountType(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
