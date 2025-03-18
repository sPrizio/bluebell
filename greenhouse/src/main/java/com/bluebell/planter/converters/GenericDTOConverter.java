package com.bluebell.planter.converters;


import com.bluebell.platform.models.api.dto.GenericDTO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Generic converter for entities into {@link GenericDTO}s
 *
 * @author Stephen Prizio
 * @version 0.1.2
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

        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }

        return new ArrayList<>(entities.stream().map(this::convert).toList());
    }
}
