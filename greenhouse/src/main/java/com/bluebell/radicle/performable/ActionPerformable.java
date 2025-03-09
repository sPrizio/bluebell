package com.bluebell.radicle.performable;

import com.bluebell.platform.models.core.nonentities.action.ActionData;

/**
 * Describes the process of performing an action, i.e. doing something
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
public interface ActionPerformable {

    /**
     * Performs the performable action
     *
     * @return {@link ActionData}
     */
    ActionData perform();
}
