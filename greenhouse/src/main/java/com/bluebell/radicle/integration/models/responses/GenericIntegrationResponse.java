package com.bluebell.radicle.integration.models.responses;

/**
 * Generic response from a client integration
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public interface GenericIntegrationResponse {

    /**
     * Flag to return whether this response is empty, used in place of returning null
     *
     * @return true if a certain condition is met (usually some key data being null/empty)
     */
    boolean isEmpty();
}
