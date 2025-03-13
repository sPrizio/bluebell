package com.bluebell.platform.models.core.nonentities.action;

import com.bluebell.platform.enums.action.ActionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Stores the result of performing an action
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
@Setter
@Builder
public class ActionResult {

    private ActionData data;

    private @Builder.Default ActionStatus status = ActionStatus.NOT_STARTED;
}
