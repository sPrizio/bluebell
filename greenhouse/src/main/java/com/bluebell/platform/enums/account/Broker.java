package com.bluebell.platform.enums.account;

import com.bluebell.platform.enums.GenericEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Enum representing the different brokers supported by the system
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Getter
@Schema(title = "Brokers", name = "Broker Enum", description = "List of Brokers supported in bluebell.")
public enum Broker implements GenericEnum<Broker> {
    CMC_MARKETS("CMC_MARKETS", "CMC Markets"),
    FTMO("FTMO", "FTMO"),
    DEMO("DEMO", "Demo"),
    NA("N/A", "N/A");

    private final String code;

    private final String label;

    Broker(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
