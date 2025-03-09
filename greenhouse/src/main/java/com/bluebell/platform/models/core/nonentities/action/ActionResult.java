package com.bluebell.platform.models.core.nonentities.action;

import com.bluebell.platform.enums.action.ActionStatus;
import lombok.Builder;
import lombok.Getter;

/**
 * Stores the result of performing an action
 *
 * @param data {@link ActionData}
 * @param status {@link ActionStatus}
 */
@Builder
public record ActionResult(
        @Getter ActionData data,
        @Getter ActionStatus status
) { }
