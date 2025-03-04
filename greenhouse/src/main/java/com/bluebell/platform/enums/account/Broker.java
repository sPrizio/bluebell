package com.bluebell.platform.enums.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Enum representing the different brokers supported by the system
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
@Schema(title = "Brokers", name = "Broker Enum", description = "List of Brokers supported in bluebell.")
public enum Broker {
    CMC_MARKETS("CMC_MARKETS", "CMC Markets"),
    FTMO("FTMO", "FTMO"),
    NA("N/A", "Demo");

    private final String code;

    private final String name;

    Broker(final String code, final String name) {
        this.code = code;
        this.name = name;
    }
}
