package com.bluebell.platform.enums.action;

import com.bluebell.platform.models.core.entities.action.impl.Action;

/**
 * Enumeration representing the result of an {@link Action}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
public enum ActionStatus {
    SUCCESS,
    FAILURE,
    IN_PROGRESS,
    NOT_STARTED,
}
