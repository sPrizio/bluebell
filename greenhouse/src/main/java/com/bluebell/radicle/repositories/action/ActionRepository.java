package com.bluebell.radicle.repositories.action;

import com.bluebell.platform.models.core.entities.action.Action;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link Action} entities
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Repository
public interface ActionRepository extends PagingAndSortingRepository<Action, Long>, CrudRepository<Action, Long> {
}
