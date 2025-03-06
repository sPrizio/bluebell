package com.bluebell.platform.models.core.nonentities.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * Displays enum information as a front-end object dto
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Builder
@Schema(title = "EnumDisplay", name = "EnumDisplay", description = "Data representation of an enum in the bluebell system")
public record EnumDisplay(
        @Getter @Schema(description = "Enum code") String code,
        @Getter @Schema(description = "Display value for enum") String label
) { }
