package com.bluebell.planter.converters.action;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.action.ActionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link ActionDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.2.1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class ActionDTOConverterTest extends AbstractPlanterTest {

    @Autowired
    private ActionDTOConverter actionDTOConverter;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    void test_convert_success_emptyResult() {
        assertThat(this.actionDTOConverter.convert(null))
                .isNotNull()
                .satisfies(ActionDTO::isEmpty);

    }

    @Test
    void test_convert_success() {
        assertThat(this.actionDTOConverter.convert(generateTestAction()))
                .isNotNull()
                .extracting("priority", "name")
                .containsExactly(1, "Test Action");

    }


    //  ----------------- convertAll -----------------

    @Test
    void test_convertAll_success() {
        assertThat(this.actionDTOConverter.convertAll(List.of(generateTestAction())))
                .isNotEmpty()
                .first()
                .extracting("priority", "name")
                .containsExactly(1, "Test Action");
    }
}
