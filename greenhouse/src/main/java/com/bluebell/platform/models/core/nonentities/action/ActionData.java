package com.bluebell.platform.models.core.nonentities.action;

import com.bluebell.radicle.performable.ActionPerformable;
import lombok.Builder;
import lombok.Getter;

/**
 * Record storing the result of an {@link ActionPerformable} including any return data as well as log information
 *
 * @param data any object
 * @param logs logging information
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Builder
public record ActionData(
        @Getter Object data,
        @Getter String logs
) { }
