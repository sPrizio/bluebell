package com.bluebell.radicle.integration.translators;


import java.util.Collection;
import java.util.List;

import com.bluebell.radicle.integration.models.dto.GenericIntegrationDTO;
import com.bluebell.radicle.integration.models.responses.GenericIntegrationResponse;

/**
 * General translator that translates a {@link GenericIntegrationResponse} into a {@link GenericIntegrationDTO}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public interface GenericTranslator<R extends GenericIntegrationResponse, D extends GenericIntegrationDTO> {

    /**
     * Converts an {@link R} into a {@link D}
     *
     * @param response {@link R}
     * @return {@link D}
     */
    D translate(final R response);

    /**
     * Converts a {@link List} of {@link R} into a {@link List} of {@link D}
     *
     * @param responses {@link List} of {@link R}
     * @return {@link List} of {@link D}
     */
    default List<D> translateAll(final Collection<R> responses) {
        return responses.stream().map(this::translate).toList();
    }
}
