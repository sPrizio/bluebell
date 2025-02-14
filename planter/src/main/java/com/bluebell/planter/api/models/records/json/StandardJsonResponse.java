package com.bluebell.planter.api.models.records.json;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;

/**
 * Class representation of a standard json response
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Schema(description = "Standard API response entity. All api calls will return this entity which includes a success flag, data, external & internal facing messages.")
public record StandardJsonResponse<T>(
        @Schema(description = "Indicates whether the response successfully completed") boolean success,
        @Schema(description = "The response data") T data,
        @Schema(description = "External, client-facing message. Successful calls will usually have an empty message") String message,
        @Schema(description = "Internal, server-facing message") String internalMessage
) {

    public StandardJsonResponse(final boolean success, final T data, final String message) {
        this(success, data, message, StringUtils.EMPTY);
    }
}
