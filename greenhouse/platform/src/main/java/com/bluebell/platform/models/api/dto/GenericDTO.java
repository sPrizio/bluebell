package com.bluebell.platform.models.api.dto;

import org.apache.commons.lang3.StringUtils;

/**
 * Global DTO parent across the app
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public interface GenericDTO {

    /**
     * Obtains the unique identifier for this DTO
     *
     * @return {@link String}
     */
    String getUid();

    /**
     * Returns true if the DTO is empty. Used in place of returning nulls
     *
     * @return true if it is empty
     */
    default boolean isEmpty() {
        return StringUtils.isEmpty(getUid());
    }
}
