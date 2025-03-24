package com.bluebell.planter.converters.action;

import com.bluebell.planter.converters.GenericDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.action.ActionDTO;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Converts {@link Action}s into {@link ActionDTO}s
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Component("actionDTOConverter")
public class ActionDTOConverter implements GenericDTOConverter<Action, ActionDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public ActionDTO convert(final Action entity) {

        if (entity == null) {
            return ActionDTO.builder().build();
        }

        return ActionDTO
                .builder()
                .uid(this.uniqueIdentifierService.generateUid(entity))
                .actionId(entity.getActionId())
                .priority(entity.getPriority())
                .name(entity.getName())
                .status(EnumDisplay.builder().code(entity.getStatus().getCode()).label(entity.getStatus().getLabel()).build())
                .performableAction(entity.getPerformableAction().getActionPerformableName())
                .build();
    }
}
