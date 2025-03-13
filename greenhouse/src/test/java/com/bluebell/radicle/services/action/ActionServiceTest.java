package com.bluebell.radicle.services.action;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.action.ActionStatus;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.nonentities.action.ActionData;
import com.bluebell.platform.models.core.nonentities.action.ActionResult;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.performable.impl.FetchMarketNewsActionPerformable;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link ActionService}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class ActionServiceTest extends AbstractGenericTest {

    @MockitoBean
    private FetchMarketNewsActionPerformable fetchMarketNewsActionPerformable;

    @Autowired
    private ActionService actionService;


    //  ----------------- performAction -----------------

    @Test
    void test_performAction_missingAction() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.actionService.performAction(null))
                .withMessageContaining(CorePlatformConstants.Validation.Action.ACTION_CANNOT_BE_NULL);
    }

    @Test
    void test_performAction_failed() {
        Mockito.when(this.fetchMarketNewsActionPerformable.perform()).thenReturn(ActionData.builder().success(false).build());
        assertThat(this.actionService.performAction(Action.builder().performableAction(this.fetchMarketNewsActionPerformable).build()))
                .extracting("status")
                .isEqualTo(ActionStatus.FAILURE);
    }

    @Test
    void test_performAction_success() {
        Mockito.when(this.fetchMarketNewsActionPerformable.perform()).thenReturn(ActionData.builder().success(true).build());
        final ActionResult actionResult = this.actionService.performAction(Action.builder().performableAction(this.fetchMarketNewsActionPerformable).build());
        assertThat(actionResult.getStatus()).isEqualTo(ActionStatus.SUCCESS);
        assertThat(actionResult.getData().success()).isTrue();
    }
}
