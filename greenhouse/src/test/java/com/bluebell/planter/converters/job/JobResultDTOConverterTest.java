package com.bluebell.planter.converters.job;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.job.JobResultDTO;
import org.assertj.core.api.InstanceOfAssertFactories;
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
 * Testing class for {@link JobResultDTO}
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class JobResultDTOConverterTest extends AbstractPlanterTest {

    @Autowired
    private JobResultDTOConverter jobResultDTOConverter;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    void test_convert_success_emptyResult() {
        assertThat(this.jobResultDTOConverter.convert(null))
                .isNotNull()
                .satisfies(JobResultDTO::isEmpty);

    }

    @Test
    void test_convert_success() {
        assertThat(this.jobResultDTOConverter.convert(generateTestJobResult()))
                .isNotNull()
                .extracting("entries")
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .element(0)
                .extracting("success", "data", "logs")
                .containsExactly(true, "Test Data", "This is a log message");

    }


    //  ----------------- convertAll -----------------

    @Test
    void test_convertAll_success() {
        assertThat(this.jobResultDTOConverter.convertAll(List.of(generateTestJobResult())))
                .isNotEmpty()
                .first()
                .extracting("entries")
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .element(0)
                .extracting("success", "data", "logs")
                .containsExactly(true, "Test Data", "This is a log message");
    }
}
