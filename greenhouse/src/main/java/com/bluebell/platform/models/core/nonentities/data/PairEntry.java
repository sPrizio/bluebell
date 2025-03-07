package com.bluebell.platform.models.core.nonentities.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * A record that represents a pair of values with option symbol
 *
 * @param code   code
 * @param label  display value
 * @param symbol display symbol
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Builder
@Schema(description = "This entity represents items that possess a specific code, display label and some king of symbol. Examples include currencies & languages.")
public record PairEntry(
        @Getter @Schema(description = "Unique identifier of the object") Object code,
        @Getter @Schema(description = "The display string") Object label,
        @Getter @Schema(description = "Unique symbol, if applicable") String symbol
) { }

