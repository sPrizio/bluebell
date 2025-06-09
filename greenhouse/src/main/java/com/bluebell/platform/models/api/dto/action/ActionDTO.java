package com.bluebell.platform.models.api.dto.action;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * DTO representation of {@link Action}
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Getter
@Setter
@Builder
@Schema(title = "ActionDTO", name = "ActionDTO", description = "A client-facing reduction of the Action entity that displays key account information in a safe to read way.")
public class ActionDTO implements GenericDTO, Comparable<ActionDTO> {

    @Schema(description = "Action uid")
    private @Builder.Default String uid = StringUtils.EMPTY;

    @Schema(description = "Action id")
    private String actionId;

    @Schema(description = "Action priority")
    private int priority;

    @Schema(description = "Action name")
    private String name;

    @Schema(description = "Action display/pretty Name")
    private String displayName;

    @Schema(description = "Action status")
    private EnumDisplay status;


    //  METHODS

    @Override
    public int compareTo(final @NonNull ActionDTO o) {
        return Integer.compare(this.priority, o.priority);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;

        if (StringUtils.isEmpty(this.actionId)) return false;

        ActionDTO action = (ActionDTO) object;
        if (StringUtils.isEmpty(action.getActionId())) return false;
        return this.actionId.equals(action.actionId);
    }

    @Override
    public int hashCode() {
        return this.actionId.hashCode();
    }
}
