package com.bluebell.platform.enums.action;

import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import lombok.Getter;

/**
 * Enumeration representing the result of an {@link Action}
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Getter
public enum ActionStatus implements GenericEnum<ActionStatus> {
    SUCCESS("SUCCESS", "Success"),
    FAILURE("FAILURE", "Failure"),
    IN_PROGRESS("IN_PROGRESS", "In Progress"),
    NOT_STARTED("NOT_STARTED", "Not Started"),
    SKIPPED("SKIPPED", "Skipped");

    private final String code;

    private final String label;

    ActionStatus(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
