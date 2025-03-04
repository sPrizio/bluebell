package com.bluebell.planter.converters;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bluebell.platform.models.api.dto.GenericDTO;

/**
 * Generic converter for entities into {@link GenericDTO}s
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public interface GenericDTOConverter<E, D extends GenericDTO> {

    /**
     * Converts a {@link E} into a {@link D}
     *
     * @param entity {@link E}
     * @return {@link D}
     */
    D convert(final E entity);

    /**
     * Converts a {@link List} of {@link E} into a {@link List} of {@link D}
     *
     * @param entities {@link List} of {@link E}
     * @return {@link List} of {@link D}
     */
    default List<D> convertAll(final Collection<E> entities) {
        return new ArrayList<>(entities.stream().map(this::convert).toList());
    }
}
