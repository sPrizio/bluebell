package com.bluebell.platform.models.core.entities.action;

import com.bluebell.platform.models.core.entities.GenericEntity;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.nonentities.action.ActionResult;

/**
 * Generic action that all {@link Action}s will inherit
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
public interface GenericAction extends GenericEntity {

    /**
     * Performs the given {@link Action}
     *
     * @return {@link ActionResult}
     */
    ActionResult performAction();
}
