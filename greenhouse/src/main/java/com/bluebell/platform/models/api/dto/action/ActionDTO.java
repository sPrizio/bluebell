package com.bluebell.platform.models.api.dto.action;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * DTO representation of {@link Action}
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
@Setter
@Builder
@Schema(title = "ActionDTO", name = "ActionDTO", description = "A client-facing reduction of the Action entity that displays key account information in a safe to read way.")
public class ActionDTO implements GenericDTO {

    @Schema(description = "Action uid")
    private @Builder.Default String uid = StringUtils.EMPTY;

    @Schema(description = "Action id")
    private String actionId;

    @Schema(description = "Action priority")
    private int priority;

    @Schema(description = "Action name")
    private String name;

    @Schema(description = "Action status")
    private EnumDisplay status;

    @Schema(description = "Name of the performable action that this action undertook")
    private String performableAction;
}
