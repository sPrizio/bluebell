package com.bluebell.radicle.repositories.action;

import com.bluebell.platform.models.core.entities.action.impl.Action;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link Action} entities
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Repository
public interface ActionRepository extends PagingAndSortingRepository<Action, Long>, CrudRepository<Action, Long> {

    /**
     * Returns an {@link Action} for the given action id
     *
     * @param actionId action id
     * @return {@link Action}
     */
    Action findActionByActionId(final String actionId);
}
