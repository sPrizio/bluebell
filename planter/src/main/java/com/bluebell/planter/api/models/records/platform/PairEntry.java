package com.bluebell.planter.api.models.records.platform;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A record that represents a pair of values with option symbol
 *
 * @param code   code
 * @param label  display value
 * @param symbol display symbol
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Schema(description = "This entity represents items that possess a specific code, display label and some king of symbol. Examples include currencies & languages.")
public record PairEntry(Object code, Object label, String symbol) {
}
