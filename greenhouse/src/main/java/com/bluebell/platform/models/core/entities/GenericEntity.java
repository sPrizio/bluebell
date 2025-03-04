package com.bluebell.platform.models.core.entities;

/**
 * A base entity that acts as the parent for all model entities in the system
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public interface GenericEntity {

    /**
     * Obtains the id for the given entity
     *
     * @return {@link Long}
     */
    Long getId();
}
