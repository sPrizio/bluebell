package com.bluebell.planter.api.models.records.json;

import org.apache.commons.lang3.StringUtils;

/**
 * Class representation of a standard json response
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
public record StandardJsonResponse(
        boolean success,
        Object data,
        String message,
        String internalMessage
) {

    public StandardJsonResponse(final boolean success, final Object data, final String message) {
        this(success, data, message, StringUtils.EMPTY);
    }
}
