package com.bluebell.radicle.services.action;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.action.ActionStatus;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.nonentities.action.ActionData;
import com.bluebell.platform.models.core.nonentities.action.ActionResult;
import com.bluebell.radicle.repositories.action.ActionRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link Action}s
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Slf4j
@Service
public class ActionService {

    @Resource(name = "actionRepository")
    private ActionRepository actionRepository;


    //  METHODS

    /**
     * Performs an {@link Action}
     *
     * @param action {@link Action}
     * @return {@link ActionResult}
     */
    public ActionResult performAction(final Action action) {

        validateParameterIsNotNull(action, CorePlatformConstants.Validation.Action.ACTION_CANNOT_BE_NULL);

        LOGGER.info("Performing action {}", action.getName());

        final ActionResult result = ActionResult
                .builder()
                .status(ActionStatus.IN_PROGRESS)
                .build();

        final ActionData actionData = action.getPerformableAction().perform();
        if (actionData != null && actionData.isSuccess()) {
            result.setData(actionData);
            result.setStatus(ActionStatus.SUCCESS);
            LOGGER.info("Action {} completed successfully", action.getName());
        } else {
            result.setStatus(ActionStatus.FAILURE);
            LOGGER.info("Action {} failed. Consult logs for further information", action.getName());
        }

        return result;
    }
}
